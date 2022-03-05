package ru.itsyn.jmix.menu_editor.screen.menu_config;

import com.vaadin.spring.annotation.UIScope;
import io.jmix.core.DataManager;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.ui.menu.MenuConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.itsyn.jmix.menu_editor.entity.MenuEntity;

import javax.annotation.Nullable;

@UIScope
@Component("menu_UserMenuConfig")
public class UserMenuConfig extends MenuConfig {

    @Autowired
    DataManager dataManager;
    @Autowired
    CurrentAuthentication currentAuthentication;
    @Autowired
    MenuConfigLoader configLoader;

    @Override
    protected void init() {
        var userMenu = loadMenuEntity();
        if (userMenu != null) {
            rootItems.clear();
            var d = dom4JTools.readDocument(userMenu.getConfig());
            loadMenuItems(d.getRootElement(), null);
            return;
        }
        rootItems = configLoader.loadDefaultConfig();
    }

    @Nullable
    protected MenuEntity loadMenuEntity() {
        var user = currentAuthentication.getUser();
        var query = "select e from menu_MenuEntity e" +
                " where e.roleCode in (select ra.roleCode" +
                "   from sec_RoleAssignmentEntity ra" +
                "   where ra.username = :username" +
                " ) order by e.priority desc";
        return dataManager.load(MenuEntity.class)
                .query(query)
                .parameter("username", user.getUsername())
                .maxResults(1)
                .optional()
                .orElse(null);
    }

}
