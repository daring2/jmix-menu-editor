package ru.itsyn.jmix.menu_editor.screen.menu_config;

import io.jmix.core.security.CurrentAuthentication;
import io.jmix.ui.menu.MenuConfig;
import io.jmix.ui.menu.MenuItem;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("menu_MainMenuConfig")
@Primary
public class MainMenuConfig extends MenuConfig {

    @Autowired
    BeanFactory beanFactory;
    @Autowired
    CurrentAuthentication currentAuthentication;

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
