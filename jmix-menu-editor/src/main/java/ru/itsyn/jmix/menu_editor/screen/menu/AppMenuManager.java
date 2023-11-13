package ru.itsyn.jmix.menu_editor.screen.menu;

import com.vaadin.flow.component.UI;
import io.jmix.flowui.app.main.StandardMainView;
import io.jmix.flowui.component.main.JmixListMenu;
import io.jmix.flowui.kit.component.main.ListMenu;
import io.jmix.flowui.kit.component.main.ListMenu.MenuBarItem;
import io.jmix.flowui.menu.MenuConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static io.jmix.flowui.component.UiComponentUtils.getComponents;

@Component("menu_AppMenuManager")
public class AppMenuManager {

    @Autowired
    protected MenuConfig menuConfig;

    public void reloadAppMenu() {
        menuConfig.reset();
        var appMenu = (JmixListMenu) UI.getCurrent().getChildren()
                .filter(c -> c instanceof StandardMainView)
                .flatMap(c -> getComponents(c).stream())
                .filter(c -> c instanceof JmixListMenu)
                .findFirst()
                .orElse(null);
        if (appMenu != null) {
            removeAllMenuItems(appMenu);
            appMenu.loadMenuConfig();
        }
    }

    protected void removeAllMenuItems(ListMenu menu) {
        for (var item : menu.getMenuItems()) {
            removeMenuItem(item);
            menu.removeMenuItem(item);
        }
    }

    protected void removeMenuItem(ListMenu.MenuItem item) {
        if (!(item instanceof MenuBarItem menu))
            return;
        for (var child : menu.getChildren()) {
            removeMenuItem(child);
            menu.removeChildItem(child);
        }
    }

}
