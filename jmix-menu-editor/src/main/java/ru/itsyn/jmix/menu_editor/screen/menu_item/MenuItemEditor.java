package ru.itsyn.jmix.menu_editor.screen.menu_item;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import io.jmix.core.TimeSource;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;
import ru.itsyn.jmix.menu_editor.entity.MenuItemEntity;
import ru.itsyn.jmix.menu_editor.entity.MenuItemType;
import ru.itsyn.jmix.menu_editor.util.MenuItemHelper;

@Route(value = "MenuItemEntity/:id", layout = DefaultMainViewParent.class)
@ViewController("menu_MenuItemEntity.detail")
@ViewDescriptor("menu-item-editor.xml")
@EditedEntityContainer("editDc")
@DialogMode(width = "1024px", height = "768px", resizable = true)
public class MenuItemEditor extends StandardDetailView<MenuItemEntity> {

    @Autowired
    protected TimeSource timeSource;
    @Autowired
    protected CurrentAuthentication currentAuthentication;
    @Autowired
    protected MenuItemHelper menuItemHelper;

    @ViewComponent
    protected TextField idField;
    @ViewComponent
    protected TextField captionKeyField;
    @ViewComponent
    protected ComboBox<MenuItemType> itemTypeField;
    @ViewComponent
    protected Checkbox expandedField;

    @Subscribe
    public void onInit(InitEvent event) {
        initValueChangeListeners();
    }

    protected void initValueChangeListeners() {
        idField.addValueChangeListener(e -> updateCaption());
        captionKeyField.addValueChangeListener(e -> updateCaption());
        itemTypeField.addValueChangeListener(e -> {
            expandedField.setReadOnly(e.getValue() != MenuItemType.MENU);
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
            idField.setReadOnly(true);
    }

    protected void updateCaption() {
        menuItemHelper.updateItemCaption(getEditedEntity());
    }

    @Subscribe
    public void onBeforeSave(BeforeSaveEvent event) {
        var entity = getEditedEntity();
        entity.setLastModifiedDate(timeSource.currentTimestamp());
        entity.setLastModifiedBy(currentAuthentication.getUser().getUsername());
    }

}
