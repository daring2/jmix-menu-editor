package ru.itsyn.jmix.menu_editor.util;

import io.jmix.ui.menu.MenuConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.itsyn.jmix.menu_editor.entity.MenuItemEntity;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class MenuItemHelperTest {

    MenuItemHelper helper;

    @BeforeEach
    public void init() {
        helper = new MenuItemHelper();
        helper.menuConfig = mock(MenuConfig.class);
    }

    @Test
    public void testBuildItemList() {
        var ri = newItem("root", null);
        var i1 = newItem("i1", ri);
        var i2 = newItem("i2", ri);
        assertEquals(newArrayList(ri, i1, i2), helper.buildItemList(ri));
    }

    MenuItemEntity newItem(String id, MenuItemEntity parent) {
        var i = new MenuItemEntity();
        i.setId(id);
        if (parent != null) {
            i.setParent(parent);
            parent.getChildren().add(i);
        }
        return i;
    }

}