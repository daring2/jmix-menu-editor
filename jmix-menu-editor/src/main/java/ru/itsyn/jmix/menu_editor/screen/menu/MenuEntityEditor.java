package ru.itsyn.jmix.menu_editor.screen.menu;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.ItemDoubleClickEvent;
import com.vaadin.flow.component.grid.dnd.GridDropEvent;
import com.vaadin.flow.component.grid.dnd.GridDropLocation;
import com.vaadin.flow.component.grid.dnd.GridDropMode;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.router.Route;
import io.jmix.core.LoadContext;
import io.jmix.core.Messages;
import io.jmix.core.common.util.Dom4j;
import io.jmix.flowui.DialogWindows;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.action.list.EditAction;
import io.jmix.flowui.component.grid.TreeDataGrid;
import io.jmix.flowui.component.tabsheet.JmixTabSheet;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.model.CollectionContainer;
import io.jmix.flowui.model.CollectionLoader;
import io.jmix.flowui.model.DataContext;
import io.jmix.flowui.model.InstanceContainer.ItemChangeEvent;
import io.jmix.flowui.util.RemoveOperation;
import io.jmix.flowui.util.RemoveOperation.AfterActionPerformedEvent;
import io.jmix.flowui.view.*;
import io.jmix.security.model.ResourceRole;
import io.jmix.security.role.ResourceRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import ru.itsyn.jmix.menu_editor.entity.MenuEntity;
import ru.itsyn.jmix.menu_editor.entity.MenuItemEntity;
import ru.itsyn.jmix.menu_editor.screen.menu_item.MenuConfigBuilder;
import ru.itsyn.jmix.menu_editor.screen.menu_item.MenuItemFilterHelper;
import ru.itsyn.jmix.menu_editor.screen.menu_item.MenuItemLoader;
import ru.itsyn.jmix.menu_editor.util.DialogHelper;
import ru.itsyn.jmix.menu_editor.util.MenuItemHelper;

import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;

import static ru.itsyn.jmix.menu_editor.screen.menu_item.MenuItemFactory.ROOT_ITEM_ID;

@Route(value = "MenuEntity/:id", layout = DefaultMainViewParent.class)
@ViewController("menu_MenuEntity.detail")
@ViewDescriptor("menu-entity-editor.xml")
@EditedEntityContainer("editDc")
@DialogMode(width = "1024px", height = "768px", resizable = true)
public class MenuEntityEditor extends StandardDetailView<MenuEntity> {

    protected static final String ITEMS_TAB = "itemsTab";
    protected static final String CONFIG_TAB = "configTab";
    protected static final String TEXT_PLAIN = "text/plain";

    @Autowired
    protected Messages messages;
    @Autowired
    protected Notifications notifications;
    @Autowired
    protected ResourceRoleRepository roleRepository;
    @Autowired
    protected DialogWindows dialogWindows;
    @Autowired
    protected RemoveOperation removeOperation;
    @Autowired
    protected DialogHelper dialogHelper;
    @Autowired
    protected MenuItemLoader menuItemLoader;
    @Autowired
    protected MenuItemHelper menuItemHelper;
    @Autowired
    protected MenuItemFilterHelper menuItemFilterHelper;
    @Autowired
    protected MenuConfigBuilder menuConfigBuilder;

    @ViewComponent
    protected DataContext dataContext;
    @ViewComponent
    protected JmixTabSheet tabSheet;
    @ViewComponent
    protected ComboBox<String> roleField;
    @ViewComponent
    protected CollectionContainer<MenuItemEntity> itemsDc;
    @ViewComponent
    protected CollectionLoader<MenuItemEntity> itemsDl;
    @ViewComponent
    protected TreeDataGrid<MenuItemEntity> itemsTable;
    @ViewComponent("itemsTable.edit")
    protected EditAction<MenuItemEntity> itemsTableEditAction;

    @Subscribe
    public void onInit(InitEvent event) {
        initTabSheet();
        initRoleField();
        initItemDragAndDrop();
    }

    protected void initTabSheet() {
        tabSheet.addSelectedChangeListener(event -> {
            if (!event.isFromClient())
                return;
            var tabName = event.getSelectedTab().getId().orElse(null);
            if (ITEMS_TAB.equals(tabName)) {
                itemsDl.load();
            } else if (CONFIG_TAB.equals(tabName)) {
                updateMenuConfig(getRootItem());
            }
        });
    }

    protected void initRoleField() {
        var roleNames = new TreeMap<String, String>();
        for (ResourceRole role : roleRepository.getAllRoles()) {
            roleNames.put(role.getCode(), role.getName());
        }
        roleField.setItems(roleNames.keySet());
        roleField.setItemLabelGenerator(roleNames::get);
    }

    protected void initItemDragAndDrop() {
        itemsTable.setRowsDraggable(true);
        itemsTable.setDragDataGenerator(TEXT_PLAIN, MenuItemEntity::getId);
        itemsTable.setDropMode(GridDropMode.ON_TOP_OR_BETWEEN);
        //TODO add DropCriteriaScript
        itemsTable.addDropListener(this::onDropItem);
    }

    protected void onDropItem(GridDropEvent<MenuItemEntity> event) {
        var item = event.getDataTransferData(TEXT_PLAIN)
                .map(itemsDc::getItemOrNull)
                .orElse(null);
        if (item == null || getRootItem().equals(item))
            return;
        var targetItem = event.getDropTargetItem().orElse(null);
        if (targetItem == null || targetItem.getParent() == null)
            return;
        var dropLocation = event.getDropLocation();
        if (dropLocation == GridDropLocation.ON_TOP && targetItem.isMenu()) {
            moveItem(item, targetItem, 0);
        } else {
            var parent = targetItem.getParent();
            var index = parent.getChildIndex(targetItem);
            if (dropLocation != GridDropLocation.ABOVE)
                index += 1;
            moveItem(item, parent, index);
        }
    }

    protected void moveItem(MenuItemEntity item, MenuItemEntity parent, int index) {
        if (parent.equals(item) || parent.hasAncestor(item)) {
            var warning = messages.getMessage(getClass(), "cyclicDependencyWarning");
            notifications.create(warning)
                    .show();
            return;
        }
        var parentIndex = item.getParent();
        if (parentIndex == parent && parentIndex.getChildIndex(item) < index)
            index -= 1;
        parentIndex.removeChild(item);
        parent.addChild(item, index);
        refreshItems();
    }

    protected void refreshItems() {
        var rootItem = getRootItem();
        itemsDc.setItems(menuItemHelper.buildItemList(rootItem));
    }

    @Install(to = "itemsTable.remove", subject = "enabledRule")
    protected Boolean isItemRemoveEnabled() {
        var items = itemsTable.getSelectedItems();
        return !items.contains(getRootItem());
    }

    @Install(to = "itemsDl", target = Target.DATA_LOADER)
    protected List<MenuItemEntity> loadMenuItems(LoadContext<MenuItemEntity> loadContext) {
        var rootItem = menuItemLoader.loadMenu(getEditedEntity());
        var items = menuItemHelper.buildItemList(rootItem);
        return menuItemFilterHelper.filterItems(items, loadContext);
    }

    @Subscribe(id = "itemsDc", target = Target.DATA_CONTAINER)
    protected void onItemsDcItemChange(ItemChangeEvent<MenuItemEntity> event) {
//        if (event.getPrevItem() == null)
//            refreshItems();
    }

//    @Supply(to = "itemsTable.caption", subject = "renderer")
    protected Renderer<MenuItemEntity> newItemCaptionRenderer() {
        return new TextRenderer<>(menuItemHelper::getItemCaption);
    }

    @Subscribe("itemsTable.create")
    protected void onItemCreate(ActionPerformedEvent event) {
        var item = itemsTable.getSingleSelectedItem();
        if (item == null)
            item = getRootItem();
        var parent = item.isMenu() ? item : item.getParent();
        var index = parent != item ? parent.getChildIndex(item) + 1 : 0;
        dialogWindows.detail(itemsTable)
                .newEntity()
                .withParentDataContext(dataContext)
                .withInitializer(i -> i.setParent(parent))
                .withTransformation(i -> {
                    parent.addChild(i, index);
                    return i;
                })
                .open();
    }

    @Subscribe("itemsTable.edit")
    protected void onItemEdit(ActionPerformedEvent event) {
        dialogWindows.detail(itemsTable)
                .withParentDataContext(dataContext)
                .open();
    }

    @Subscribe("itemsTable.remove")
    void onItemRemove(ActionPerformedEvent event) {
        removeOperation.builder(itemsTable)
                .afterActionPerformed(this::afterItemRemove)
                .remove();
    }

    protected void afterItemRemove(AfterActionPerformedEvent<MenuItemEntity> event) {
        var items = itemsDc.getMutableItems();
        for (MenuItemEntity item : event.getItems()) {
            item.getParent().removeChild(item);
            item.visitItems(items::remove);
        }
    }

    @Subscribe("itemsTable.resetMenu")
    protected void onResetMenu(ActionPerformedEvent event) {
        dialogHelper.newConfirmationDialog(
                messages.getMessage(getClass(), "resetConfirmation"),
                this::resetMenu
        ).open();
    }

    protected void resetMenu(ActionPerformedEvent event) {
        var rootItem = menuItemLoader.loadDefaultMenu();
        updateMenuConfig(rootItem);
        itemsDl.load();
    }

    @Subscribe("itemsTable")
    protected void onItemDoubleClick(ItemDoubleClickEvent<MenuItemEntity> event) {
        itemsTable.select(event.getItem());
        itemsTableEditAction.actionPerform(itemsTable);
    }

    @Subscribe
    protected void onBeforeSave(BeforeSaveEvent event) {
        var rootItem = getRootItem();
        if (rootItem == null) return;
        var selectedTabId = tabSheet.getSelectedTab().getId().orElse(null);
        if (ITEMS_TAB.equals(selectedTabId))
            updateMenuConfig(rootItem);
        // optimization
        new HashSet<>(dataContext.getModified()).forEach(e -> {
            if (e instanceof MenuItemEntity)
                dataContext.evict(e);
        });
    }

    protected void updateMenuConfig(MenuItemEntity rootItem) {
        var document = menuConfigBuilder.buildMenuConfig(rootItem.getChildren());
        var config = Dom4j.writeDocument(document, true);
        getEditedEntity().setConfig(config);
    }

    protected MenuItemEntity getRootItem() {
        return itemsDc.getItem(ROOT_ITEM_ID);
    }

}