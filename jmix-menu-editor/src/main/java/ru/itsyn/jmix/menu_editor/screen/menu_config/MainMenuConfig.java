package ru.itsyn.jmix.menu_editor.screen.menu_config;

import io.jmix.core.security.CurrentAuthentication;
import io.jmix.flowui.menu.MenuConfig;
import io.jmix.flowui.menu.MenuItem;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;

@Primary
@Component("menu_MainMenuConfig")
public class MainMenuConfig extends BaseMenuConfig {

    @Autowired
    protected BeanFactory beanFactory;
    @Autowired
    protected CurrentAuthentication currentAuthentication;

    public MainMenuConfig(MenuConfigDependencies dependencies) {
        super(dependencies);
    }

    @Override
    public List<MenuItem> getRootItems() {
        var userConfig = getUserMenuConfig();
        if (userConfig != null)
            return userConfig.getRootItems();
        return super.getRootItems();
    }

    protected MenuConfig getUserMenuConfig() {
        var auth = currentAuthentication.getAuthentication();
        if (auth == null || auth.getPrincipal() == null)
            return null;
        return beanFactory.getBean(UserMenuConfig.class);
    }

    @Override
    public void reset() {
        super.reset();
        var userConfig = getUserMenuConfig();
        if (userConfig != null)
            userConfig.reset();
    }

}
