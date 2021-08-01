package ru.itsyn.jmix.menu_editor.screen.menu;

import io.jmix.core.LoadContext;
import io.jmix.ui.component.DataGrid.ColumnGeneratorEvent;
import io.jmix.ui.screen.*;
import ru.itsyn.jmix.menu_editor.entity.MenuEntity;
import ru.itsyn.jmix.menu_editor.entity.MenuItemEntity;
import ru.itsyn.jmix.menu_editor.screen.menu_item.MenuItemHelper;
import ru.itsyn.jmix.menu_editor.screen.menu_item.MenuItemLoader;

import javax.inject.Inject;
import java.util.List;

@UiController("menu_MenuEntity.edit")
@UiDescriptor("menu-entity-editor.xml")
@EditedEntityContainer("editDc")
public class MenuEntityEditor extends StandardEditor<MenuEntity> {

    @Inject
    MenuItemLoader menuItemLoader;
    @Inject
    MenuItemHelper menuItemHelper;

    @Install(to = "itemsDl", target = Target.DATA_LOADER)
    protected List<MenuItemEntity> loadMenuItems(LoadContext<MenuItemEntity> loadContext) {
        var rootItem = menuItemLoader.loadMenu(getEditedEntity());
        return menuItemHelper.buildItemList(rootItem);
    }

    @Install(to = "itemsTable.caption", subject = "columnGenerator")
    protected String newItemCaptionCell(ColumnGeneratorEvent<MenuItemEntity> event) {
        return menuItemHelper.getItemCaption(event.getItem());
    }

}