package ru.itsyn.jmix.menu_editor.screen.menu;

import com.vaadin.flow.component.grid.ItemDoubleClickEvent;
import com.vaadin.flow.router.Route;
import io.jmix.core.MetadataTools;
import io.jmix.core.UuidProvider;
import io.jmix.flowui.DialogWindows;
import io.jmix.flowui.action.list.EditAction;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;
import ru.itsyn.jmix.menu_editor.entity.MenuEntity;

@Route(value = "MenuEntity", layout = DefaultMainViewParent.class)
@ViewController("menu_MenuEntity.list")
@ViewDescriptor("menu-entity-browser.xml")
@LookupComponent("table")
@DialogMode(width = "1024px", height = "768px", resizable = true)
public class MenuEntityBrowser extends StandardListView<MenuEntity> {

    @Autowired
    protected MetadataTools metadataTools;
    @Autowired
    protected DialogWindows dialogWindows;
    @Autowired
    protected AppMenuManager appMenuManager;

    @ViewComponent
    protected DataGrid<MenuEntity> table;
    @ViewComponent("table.edit")
    protected EditAction<MenuEntity> tableEditAction;

    @Subscribe("table.copy")
    protected void onMenuCopy(ActionPerformedEvent event) {
        var entity = table.getSingleSelectedItem();
        if (entity == null)
            return;
        var ce = metadataTools.copy(entity);
        ce.setId(UuidProvider.createUuid());
        ce.setName(entity.getName() + " - Copy");
        dialogWindows.detail(table)
                .newEntity(ce)
                .open();
    }

    @Subscribe("table.apply")
    protected void onMenuApply(ActionPerformedEvent event) {
        appMenuManager.reloadAppMenu();
    }

    @Subscribe("table")
    protected void onTableItemDoubleClick(ItemDoubleClickEvent<MenuEntity> event) {
        table.select(event.getItem());
        tableEditAction.actionPerform(table);
    }

}