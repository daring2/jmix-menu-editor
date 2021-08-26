package ru.itsyn.jmix.menu_editor.screen.menu;

import io.jmix.core.MetadataTools;
import io.jmix.core.UuidProvider;
import io.jmix.ui.ScreenBuilders;
import io.jmix.ui.action.Action.ActionPerformedEvent;
import io.jmix.ui.component.Table;
import io.jmix.ui.navigation.Route;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;
import ru.itsyn.jmix.menu_editor.entity.MenuEntity;

@Route("MenuEntity")
@UiController("menu_MenuEntity.browse")
@UiDescriptor("menu-entity-browser.xml")
@LookupComponent("table")
public class MenuEntityBrowser extends StandardLookup<MenuEntity> {

    @Autowired
    MetadataTools metadataTools;
    @Autowired
    ScreenBuilders screenBuilders;
    @Autowired
    Table<MenuEntity> table;

    @Subscribe("table.copy")
    public void onMenuCopy(ActionPerformedEvent event) {
        var entity = table.getSingleSelected();
        if (entity == null)
            return;
        var ce = metadataTools.copy(entity);
        ce.setId(UuidProvider.createUuid());
        ce.setName(entity.getName() + " - Copy");
        screenBuilders.editor(table)
                .newEntity(ce)
                .show();
    }

}