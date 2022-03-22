package ru.itsyn.jmix.menu_editor.util;

import io.jmix.ui.menu.MenuConfig;
import io.jmix.ui.menu.MenuItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.itsyn.jmix.menu_editor.entity.MenuItemEntity;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MenuItemHelperTest {

    MenuItemHelper helper;

    @BeforeEach
    public void init() {
        helper = new MenuItemHelper();
        helper.menuConfig = mock(MenuConfig.class);
    }

    @Test
    public void testBuildItemList() {
        var rootItem = newItem("root", null);
        var item1 = newItem("item1", rootItem);
        var item2 = newItem("item2", rootItem);
        assertEquals(newArrayList(rootItem, item1, item2), helper.buildItemList(rootItem));
    }

    @Test
    public void testGetItemCaption() {
        when(helper.menuConfig.getItemCaption(any(MenuItem.class)))
                .thenAnswer(i -> {
                    var item = i.getArgument(0, MenuItem.class);
                    assertEquals("key1", item.getCaption());
                    return "caption1";
                });
        var item1 = newItem("item1", null);
        item1.setCaptionKey("key1");
        assertEquals("caption1", helper.getItemCaption(item1));
    }

    MenuItemEntity newItem(String id, MenuItemEntity parent) {
        var item = new MenuItemEntity();
        item.setId(id);
        if (parent != null) {
            item.setParent(parent);
            parent.getChildren().add(item);
        }
        return item;
    }

}