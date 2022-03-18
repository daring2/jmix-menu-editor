package ru.itsyn.jmix.menu_editor.screen.menu;

import com.vaadin.shared.ui.dnd.EffectAllowed;
import com.vaadin.shared.ui.grid.DropLocation;
import com.vaadin.shared.ui.grid.DropMode;
import com.vaadin.ui.TreeGrid;
import com.vaadin.ui.components.grid.TreeGridDragSource;
import com.vaadin.ui.components.grid.TreeGridDropEvent;
import com.vaadin.ui.components.grid.TreeGridDropTarget;
import io.jmix.core.LoadContext;
import io.jmix.core.Messages;
import io.jmix.core.common.util.Dom4j;
import io.jmix.security.model.BaseRole;
import io.jmix.security.role.ResourceRoleRepository;
import io.jmix.ui.Notifications;
import io.jmix.ui.RemoveOperation;
import io.jmix.ui.RemoveOperation.AfterActionPerformedEvent;
import io.jmix.ui.ScreenBuilders;
import io.jmix.ui.action.Action.ActionPerformedEvent;
import io.jmix.ui.action.list.RemoveAction;
import io.jmix.ui.component.ComboBox;
import io.jmix.ui.component.DataGrid.ColumnGeneratorEvent;
import io.jmix.ui.component.TabSheet;
import io.jmix.ui.component.TreeDataGrid;
import io.jmix.ui.model.CollectionContainer;
import io.jmix.ui.model.CollectionLoader;
import io.jmix.ui.model.DataContext;
import io.jmix.ui.model.InstanceContainer.ItemChangeEvent;
import io.jmix.ui.navigation.Route;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;
import ru.itsyn.jmix.menu_editor.entity.MenuEntity;
import ru.itsyn.jmix.menu_editor.entity.MenuItemEntity;
import ru.itsyn.jmix.menu_editor.screen.menu_item.MenuConfigBuilder;
import ru.itsyn.jmix.menu_editor.screen.menu_item.MenuItemFilterHelper;
import ru.itsyn.jmix.menu_editor.util.MenuItemHelper;
import ru.itsyn.jmix.menu_editor.screen.menu_item.MenuItemLoader;
import ru.itsyn.jmix.menu_editor.util.DialogHelper;

import javax.inject.Named;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static com.vaadin.shared.ui.dnd.DragSourceState.DATA_TYPE_TEXT_PLAIN;
import static ru.itsyn.jmix.menu_editor.screen.menu_item.MenuItemFactory.ROOT_ITEM_ID;

@Route(path = "MenuEntity/edit", parentPrefix = "MenuEntity")
@UiController("menu_MenuEntity.edit")
@UiDescriptor("menu-entity-editor.xml")
@EditedEntityContainer("editDc")
public class MenuEntityEditor extends StandardEditor<MenuEntity> {

    static final String ITEMS_TAB = "itemsTab";
    static final String CONFIG_TAB = "configTab";

    @Autowired
    Messages messages;
    @Autowired
    Notifications notifications;
    @Autowired
    ResourceRoleRepository roleRepository;
    @Autowired
    ScreenBuilders screenBuilders;
    @Autowired
    RemoveOperation removeOperation;
    @Autowired
    DialogHelper dialogHelper;
    @Autowired
    MenuItemLoader menuItemLoader;
    @Autowired
    MenuItemHelper menuItemHelper;
    @Autowired
    MenuItemFilterHelper menuItemFilterHelper;
    @Autowired
    MenuConfigBuilder menuConfigBuilder;
    @Autowired
    DataContext dataContext;
    @Autowired
    TabSheet tabSheet;
    @Autowired
    ComboBox<String> roleField;
    @Autowired
    CollectionContainer<MenuItemEntity> itemsDc;
    @Autowired
    CollectionLoader<MenuItemEntity> itemsDl;
    @Autowired
    TreeDataGrid<MenuItemEntity> itemsTable;
    @Named("itemsTable.remove")
    RemoveAction<MenuItemEntity> itemRemoveAction;

    @Subscribe
    public void onInit(InitEvent event) {
        initTabSheet();
        initRoleField();
        initItemDragAndDrop();
        initRemoveItemAction();
    }

    protected void initTabSheet() {
        tabSheet.addSelectedTabChangeListener(event -> {
            if (!event.isUserOriginated())
                return;
            var tabName = event.getSelectedTab().getName();
            if (ITEMS_TAB.equals(tabName)) {
                itemsDl.load();
            } else if (CONFIG_TAB.equals(tabName)) {
                updateMenuConfig(getRootItem());
            }
        });
    }

    protected void initRoleField() {
        var roleNames = roleRepository.getAllRoles().stream()
                .collect(Collectors.toMap(BaseRole::getName, BaseRole::getCode));
        roleField.setOptionsMap(roleNames);
    }

    protected void initItemDragAndDrop() {
        TreeGrid<MenuItemEntity> grid = itemsTable.unwrap(TreeGrid.class);
        var dragSource = new TreeGridDragSource<>(grid);
        dragSource.setEffectAllowed(EffectAllowed.MOVE);
        dragSource.setDragDataGenerator(DATA_TYPE_TEXT_PLAIN, MenuItemEntity::getId);
        var dropTarget = new TreeGridDropTarget<>(grid, DropMode.ON_TOP_OR_BETWEEN);
        //TODO add DropCriteriaScript
        dropTarget.addTreeGridDropListener(this::onDropItem);
    }

    protected void onDropItem(TreeGridDropEvent<MenuItemEntity> event) {
        var item = event.getDataTransferData(DATA_TYPE_TEXT_PLAIN)
                .map(itemsDc::getItemOrNull)
                .orElse(null);
        if (item == null || getRootItem().equals(item))
            return;
        var targetItem = event.getDropTargetRow().orElse(null);
        if (targetItem == null || targetItem.getParent() == null)
            return;
        var dropLoc = event.getDropLocation();
        if (dropLoc == DropLocation.ON_TOP && targetItem.isMenu()) {
            moveItem(item, targetItem, 0);
        } else {
            var parent = targetItem.getParent();
            var index = parent.getChildIndex(targetItem);
            if (dropLoc != DropLocation.ABOVE)
                index += 1;
            moveItem(item, parent, index);
        }
    }

    protected void moveItem(MenuItemEntity item, MenuItemEntity parent, int index) {
        if (parent.equals(item) || parent.hasAncestor(item)) {
            var warning = messages.getMessage(getClass(), "cyclicDependencyWarning");
            notifications.create()
                    .withCaption(warning)
                    .show();
            return;
        }
        var pi = item.getParent();
        if (pi == parent && pi.getChildIndex(item) < index)
            index -= 1;
        pi.removeChild(item);
        parent.addChild(item, index);
        refreshItems();
    }

    protected void refreshItems() {
        var rootItem = getRootItem();
        itemsDc.setItems(menuItemHelper.buildItemList(rootItem));
    }

    protected void initRemoveItemAction() {
        itemRemoveAction.addEnabledRule(() -> {
            var items = itemsTable.getSelected();
            return !items.contains(getRootItem());
        });
    }

    @Install(to = "itemsDl", target = Target.DATA_LOADER)
    protected List<MenuItemEntity> loadMenuItems(LoadContext<MenuItemEntity> loadContext) {
        var rootItem = menuItemLoader.loadMenu(getEditedEntity());
        var items = menuItemHelper.buildItemList(rootItem);
        return menuItemFilterHelper.filterItems(items, loadContext);
    }

    @Subscribe(id = "itemsDc", target = Target.DATA_CONTAINER)
    public void onItemsDcItemChange(ItemChangeEvent<MenuItemEntity> event) {
        if (event.getPrevItem() == null)
            refreshItems();
    }

    @Install(to = "itemsTable.caption", subject = "columnGenerator")
    protected String newItemCaptionCell(ColumnGeneratorEvent<MenuItemEntity> event) {
        return menuItemHelper.getItemCaption(event.getItem());
    }

    @Subscribe("itemsTable.create")
    void onItemCreate(ActionPerformedEvent event) {
        var si = itemsTable.getSingleSelected();
        if (si == null) si = getRootItem();
        var parent = si.isMenu() ? si : si.getParent();
        var index = parent != si ? parent.getChildIndex(si) + 1 : 0;
        screenBuilders.editor(itemsTable)
                .newEntity()
                .withOpenMode(OpenMode.DIALOG)
                .withParentDataContext(dataContext)
                .withInitializer(i -> i.setParent(parent))
                .withTransformation(i -> {
                    parent.addChild(i, index);
                    return i;
                })
                .show();
    }

    @Subscribe("itemsTable.edit")
    void onItemEdit(ActionPerformedEvent event) {
        screenBuilders.editor(itemsTable)
                .withOpenMode(OpenMode.DIALOG)
                .withParentDataContext(dataContext)
                .show();
    }

    @Subscribe("itemsTable.remove")
    void onItemRemove(ActionPerformedEvent event) {
        removeOperation.builder(itemsTable)
                .afterActionPerformed(this::afterItemRemove)
                .remove();
    }

    void afterItemRemove(AfterActionPerformedEvent<MenuItemEntity> event) {
        var items = itemsDc.getMutableItems();
        for (MenuItemEntity item : event.getItems()) {
            item.getParent().removeChild(item);
            item.visitItems(items::remove);
        }
    }

    @Subscribe("itemsTable.resetMenu")
    public void onResetMenu(ActionPerformedEvent event) {
        dialogHelper.newConfirmationDialog(
                messages.getMessage(getClass(), "resetConfirmation"),
                this::resetMenu
        ).show();
    }

    protected void resetMenu(ActionPerformedEvent event) {
        var rootItem = menuItemLoader.loadDefaultMenu();
        updateMenuConfig(rootItem);
        itemsDl.load();
    }

    @Subscribe
    public void onBeforeCommitChanges(BeforeCommitChangesEvent event) {
        var rootItem = getRootItem();
        if (rootItem == null) return;
        if (ITEMS_TAB.equals(tabSheet.getSelectedTab().getName()))
            updateMenuConfig(rootItem);
        // optimization
        new HashSet<>(dataContext.getModified()).forEach(e -> {
            if (e instanceof MenuItemEntity)
                dataContext.evict(e);
        });
    }

    protected void updateMenuConfig(MenuItemEntity rootItem) {
        var doc = menuConfigBuilder.buildMenuConfig(rootItem.getChildren());
        var config = Dom4j.writeDocument(doc, true);
        getEditedEntity().setConfig(config);
    }

    protected MenuItemEntity getRootItem() {
        return itemsDc.getItem(ROOT_ITEM_ID);
    }

}