package ru.itsyn.jmix.menu_editor.screen.menu_item;

import io.jmix.ui.UiComponents;
import io.jmix.ui.menu.MenuConfig;
import io.jmix.ui.menu.MenuItem;
import org.springframework.stereotype.Component;
import ru.itsyn.jmix.menu_editor.entity.MenuItemEntity;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Component("menu_MenuItemHelper")
public class MenuItemHelper {

    @Inject
    MenuConfig menuConfig;

    public List<MenuItemEntity> buildItemList(MenuItemEntity rootItem) {
        var items = new ArrayList<MenuItemEntity>();
        rootItem.visitItems(items::add);
        return items;
    }

    public String getItemCaption(MenuItemEntity item) {
        var mi = new MenuItem(item.getItemId());
        mi.setCaption(item.getCaptionKey());
        return menuConfig.getItemCaption(mi);
    }

}
