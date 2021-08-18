package ru.itsyn.jmix.menu_editor.screen.menu_item;

import io.jmix.ui.component.CheckBox;
import io.jmix.ui.component.ComboBox;
import io.jmix.ui.component.TextField;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;
import ru.itsyn.jmix.menu_editor.entity.MenuItemEntity;
import ru.itsyn.jmix.menu_editor.entity.MenuItemType;
import ru.itsyn.jmix.menu_editor.util.MenuItemHelper;

import javax.inject.Inject;

@UiController("menu_MenuItemEntity.edit")
@UiDescriptor("menu-item-editor.xml")
@EditedEntityContainer("editDc")
public class MenuItemEditor extends StandardEditor<MenuItemEntity> {

    @Autowired
    private MenuItemHelper menuItemHelper;
    @Autowired
    private TextField<String> idField;
    @Autowired
    private TextField<String> captionKeyField;
    @Inject
    private ComboBox<MenuItemType> itemTypeField;
    @Inject
    private CheckBox expandedField;

    @Subscribe
    public void onInit(InitEvent event) {
        initValueChangeListeners();
    }

    protected void initValueChangeListeners() {
        idField.addValueChangeListener(e -> updateCaption());
        captionKeyField.addValueChangeListener(e -> updateCaption());
        itemTypeField.addValueChangeListener(e -> {
            expandedField.setEditable(e.getValue() == MenuItemType.MENU);
        });
    }

    protected void updateCaption() {
        menuItemHelper.updateItemCaption(getEditedEntity());
    }

}
