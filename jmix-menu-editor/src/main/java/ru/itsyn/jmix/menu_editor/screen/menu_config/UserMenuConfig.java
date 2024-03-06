package ru.itsyn.jmix.menu_editor.screen.menu_config;

import com.vaadin.flow.spring.annotation.UIScope;
import io.jmix.core.DataManager;
import io.jmix.core.security.CurrentAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.itsyn.jmix.menu_editor.entity.MenuEntity;

import javax.annotation.Nullable;
import java.util.HashMap;

@UIScope
@Component("menu_UserMenuConfig")
public class UserMenuConfig extends BaseMenuConfig {

    @Autowired
    protected DataManager dataManager;
    @Autowired
    protected CurrentAuthentication currentAuthentication;
    @Autowired
    protected MenuConfigLoader configLoader;

    public UserMenuConfig(MenuConfigDependencies dependencies) {
        super(dependencies);
    }

    @Override
    protected void init() {
        var userMenu = loadMenuEntity();
        if (userMenu != null) {
            rootItems.clear();
            var d = dom4JTools.readDocument(userMenu.getConfig());
            loadMenuItems(d.getRootElement(), null, new HashMap<>());
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
