package ru.itsyn.jmix.menu_editor.util;

import io.jmix.ui.menu.MenuConfig;
import io.jmix.ui.menu.MenuItem;
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

    public String getItemCaption(MenuItemEntity item) {
        var mi = new MenuItem(item.getId());
        mi.setCaption(item.getCaptionKey());
        return menuConfig.getItemCaption(mi);
    }

    public void updateItemCaption(MenuItemEntity item) {
        item.setCaption(getItemCaption(item));
    }

}
