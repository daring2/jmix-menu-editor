package ru.itsyn.jmix.menu_editor.util;

import io.jmix.flowui.menu.MenuConfig;
import io.jmix.flowui.menu.MenuItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.itsyn.jmix.menu_editor.entity.MenuItemEntity;

import java.util.ArrayList;
import java.util.List;

@Component("menu_MenuItemHelper")
public class MenuItemHelper {

    @Autowired
    MenuConfig menuConfig;

    public List<MenuItemEntity> buildItemList(MenuItemEntity rootItem) {
        var items = new ArrayList<MenuItemEntity>();
        rootItem.visitItems(items::add);
        return items;
    }

    public String getItemCaption(MenuItemEntity entity) {
        var item = new MenuItem(entity.getId());
        item.setTitle(entity.getCaptionKey());
        return menuConfig.getItemTitle(item);
    }

    public void updateItemCaption(MenuItemEntity entity) {
        entity.setCaption(getItemCaption(entity));
    }

}
