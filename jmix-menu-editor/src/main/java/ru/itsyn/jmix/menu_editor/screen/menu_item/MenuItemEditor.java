package ru.itsyn.jmix.menu_editor.screen.menu_item;

import io.jmix.core.TimeSource;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.ui.component.CheckBox;
import io.jmix.ui.component.ComboBox;
import io.jmix.ui.component.TextField;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;
import ru.itsyn.jmix.menu_editor.entity.MenuItemEntity;
import ru.itsyn.jmix.menu_editor.entity.MenuItemType;
import ru.itsyn.jmix.menu_editor.util.MenuItemHelper;

@UiController("menu_MenuItemEntity.edit")
@UiDescriptor("menu-item-editor.xml")
@EditedEntityContainer("editDc")
public class MenuItemEditor extends StandardEditor<MenuItemEntity> {

    @Autowired
    TimeSource timeSource;
    @Autowired
    CurrentAuthentication currentAuthentication;
    @Autowired
    MenuItemHelper menuItemHelper;
    @Autowired
    TextField<String> idField;
    @Autowired
    TextField<String> captionKeyField;
    @Autowired
    ComboBox<MenuItemType> itemTypeField;
    @Autowired
    CheckBox expandedField;

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

    @Subscribe
    public void onInitEntity(InitEntityEvent<MenuItemEntity> event) {
        var entity = event.getEntity();
        entity.setCreatedDate(timeSource.currentTimestamp());
        entity.setCreatedBy(currentAuthentication.getUser().getUsername());
    }

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        var entity = getEditedEntity();
        if (entity.getId() != null)
            idField.setEditable(false);
    }

    protected void updateCaption() {
        menuItemHelper.updateItemCaption(getEditedEntity());
    }

    @Subscribe
    public void onBeforeCommitChanges(BeforeCommitChangesEvent event) {
        var entity = getEditedEntity();
        entity.setLastModifiedDate(timeSource.currentTimestamp());
        entity.setLastModifiedBy(currentAuthentication.getUser().getUsername());
    }

}
