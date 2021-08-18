package ru.itsyn.jmix.menu_editor.screen.menu_item;

import ru.itsyn.jmix.menu_editor.entity.MenuItemEntity;
import ru.itsyn.jmix.menu_editor.util.MenuItemHelper;

public class TestMenuItemHelper extends MenuItemHelper {

    @Override
    public String getItemCaption(MenuItemEntity item) {
        return item.getId() + ".caption";
    }

}
