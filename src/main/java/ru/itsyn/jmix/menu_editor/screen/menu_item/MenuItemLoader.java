package ru.itsyn.jmix.menu_editor.screen.menu_item;

import io.jmix.core.common.util.Dom4j;
import io.jmix.ui.menu.MenuConfig;
import io.jmix.ui.menu.MenuItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.itsyn.jmix.menu_editor.entity.MenuEntity;
import ru.itsyn.jmix.menu_editor.entity.MenuItemEntity;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@Component("menu_MenuItemLoader")
@Scope(SCOPE_PROTOTYPE)
public class MenuItemLoader {

    @Autowired
    ApplicationContext appContext;
    @Autowired
    MenuItemFactory menuItemFactory;

    ConfigLoader configLoader = new ConfigLoader();

    @PostConstruct
    public void init() {
        appContext.getAutowireCapableBeanFactory().autowireBean(configLoader);
    }

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
        var e = menuItemFactory.createItem(item);
        e.setParent(parent);
        parent.getChildren().add(e);
        item.getChildren().forEach(i -> buildEntities(i, e));
    }

    public MenuItemEntity loadDefaultMenu() {
        var items = configLoader.loadDefaultConfig();
        return buildMenu(items);
    }

    static class ConfigLoader extends MenuConfig {

        public List<MenuItem> loadConfig(String xml) {
            rootItems = new ArrayList<>();
            if (xml == null)
                return rootItems;
            var re = Dom4j.readDocument(xml).getRootElement();
            loadMenuItems(re, null);
            return rootItems;
        }

        public List<MenuItem> loadDefaultConfig() {
            super.init();
            return rootItems;
        }

    }

}
