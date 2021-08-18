package ru.itsyn.jmix.menu_editor.screen.menu_item;

import io.jmix.ui.component.CheckBox;
import io.jmix.ui.component.ComboBox;
import io.jmix.ui.screen.*;
import ru.itsyn.jmix.menu_editor.entity.MenuItemEntity;
import ru.itsyn.jmix.menu_editor.entity.MenuItemType;

import javax.inject.Inject;

@UiController("menu_MenuItemEntity.edit")
@UiDescriptor("menu-item-editor.xml")
@EditedEntityContainer("editDc")
public class MenuItemEditor extends StandardEditor<MenuItemEntity> {

    @Inject
    protected ComboBox<MenuItemType> itemTypeField;
    @Inject
    protected CheckBox expandedField;

    @Subscribe
    public void onInit(InitEvent event) {
        itemTypeField.addValueChangeListener(e -> updateFields());
    }

    protected void updateFields() {
        var itemType= itemTypeField.getValue();
        expandedField.setEditable(itemType == MenuItemType.MENU);
    }

}
