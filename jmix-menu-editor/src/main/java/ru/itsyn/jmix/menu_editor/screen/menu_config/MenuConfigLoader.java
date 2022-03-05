package ru.itsyn.jmix.menu_editor.screen.menu_config;

import io.jmix.core.common.util.Dom4j;
import io.jmix.ui.menu.MenuConfig;
import io.jmix.ui.menu.MenuItem;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("menu_MenuConfigLoader")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MenuConfigLoader extends MenuConfig {

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
