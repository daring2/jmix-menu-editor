package ru.itsyn.jmix.menu_editor.screen.menu_item;

import io.jmix.flowui.menu.MenuItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.itsyn.jmix.menu_editor.entity.MenuEntity;
import ru.itsyn.jmix.menu_editor.entity.MenuItemEntity;
import ru.itsyn.jmix.menu_editor.screen.menu_config.MenuConfigLoader;

import java.util.List;

@Component("menu_MenuItemLoader")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MenuItemLoader {

    @Autowired
    protected ApplicationContext appContext;
    @Autowired
    protected MenuConfigLoader configLoader;
    @Autowired
    protected MenuItemFactory menuItemFactory;

    public MenuItemEntity loadMenu(MenuEntity menu) {
        var items = configLoader.loadConfig(menu.getConfig());
        return buildMenu(items);
    }

    public MenuItemEntity buildMenu(List<MenuItem> items) {
        var rootItem = menuItemFactory.createRootItem();
        items.forEach(i -> buildEntities(i, rootItem));
        return rootItem;
    }

    void buildEntities(MenuItem item, MenuItemEntity parent) {
        var entity = menuItemFactory.createItem(item);
        entity.setParent(parent);
        parent.getChildren().add(entity);
        item.getChildren().forEach(i -> buildEntities(i, entity));
    }

    public MenuItemEntity loadDefaultMenu() {
        var items = configLoader.loadDefaultConfig();
        return buildMenu(items);
    }

}
