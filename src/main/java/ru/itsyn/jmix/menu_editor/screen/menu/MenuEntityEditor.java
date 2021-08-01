package ru.itsyn.jmix.menu_editor.screen.menu;

import io.jmix.ui.screen.*;
import ru.itsyn.jmix.menu_editor.entity.MenuEntity;

@UiController("menu_MenuEntity.edit")
@UiDescriptor("menu-entity-editor.xml")
@EditedEntityContainer("editDc")
public class MenuEntityEditor extends StandardEditor<MenuEntity> {
}