package ru.itsyn.jmix.menu_editor.screen.menu;

import io.jmix.flowui.menu.MenuConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("menu_AppMenuManager")
public class AppMenuManager {

    @Autowired
    protected MenuConfig menuConfig;

    public void reloadAppMenu() {
        menuConfig.reset();
//        var topWindow = AppUI.getCurrent().getTopLevelWindowNN();
//        var appMenu = (AppMenu) ObjectUtils.firstNonNull(
//                topWindow.getComponent("appMenu"),
//                topWindow.getComponent("mainMenu")
//        );
//        if (appMenu != null) {
//            removeAllMenuItems(appMenu);
//            appMenu.loadMenu();
//        }
//        var sideMenu = (SideMenu) topWindow.getComponent("sideMenu");
//        if (sideMenu != null) {
//            sideMenu.removeAllMenuItems();
//            sideMenu.loadMenuConfig();
//        }
    }

//    protected void removeAllMenuItems(AppMenu menu) {
//        for (var mi : menu.getMenuItems()) {
//            removeMenuItem(mi);
//            menu.removeMenuItem(mi);
//        }
//    }
//
//    protected void removeMenuItem(AppMenu.MenuItem menuItem) {
//        for (var ci : menuItem.getChildren()) {
//            removeMenuItem(ci);
//            menuItem.removeChildItem(ci);
//        }
//    }

}
