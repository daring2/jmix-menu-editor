package ru.itsyn.jmix.menu_editor.screen.menu;

import io.jmix.ui.screen.*;
import ru.itsyn.jmix.menu_editor.entity.MenuEntity;

@UiController("menu_MenuEntity.browse")
@UiDescriptor("menu-entity-browser.xml")
@LookupComponent("table")
public class MenuEntityBrowser extends StandardLookup<MenuEntity> {
}