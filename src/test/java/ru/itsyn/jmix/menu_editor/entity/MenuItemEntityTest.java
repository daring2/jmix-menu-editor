package ru.itsyn.jmix.menu_editor.entity;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.jupiter.api.Assertions.*;

class MenuItemEntityTest {

    @Test
    public void testHasAncestor() {
        var ri = newItem("root", null);
        assertFalse(ri.hasAncestor(ri));
        var i1 = newItem("i1", ri);
        assertFalse(ri.hasAncestor(i1));
        assertTrue(i1.hasAncestor(ri));
        var i2 = newItem("i2", ri);
        assertTrue(i2.hasAncestor(ri));
        assertFalse(i2.hasAncestor(i1));
        var i21 = newItem("i21", i2);
        assertTrue(i21.hasAncestor(i2));
        assertTrue(i21.hasAncestor(ri));
    }

    @Test
    public void testVisitItems() {
        var ri = newItem("root", null);
        var i1 = newItem("i1", ri);
        var i2 = newItem("i2", ri);
        var i21 = newItem("i21", i2);
        var items = new ArrayList<MenuItemEntity>();
        ri.visitItems(items::add);
        assertEquals(newArrayList(ri, i1, i2, i21), items);
    }

    @Test
    public void testAddChild() {
        var pi1 = newItem("pi1", null);
        var i1 = newItem("i1", pi1);
        var i2 = newItem("i2", null);
        assertEquals(newArrayList(i1), pi1.getChildren());
        assertNull(i2.getParent());

        pi1.addChild(i2, -1);
        assertEquals(newArrayList(i1, i2), pi1.getChildren());
        assertEquals(pi1, i2.getParent());

        var i3 = newItem("i3", null);
        pi1.addChild(i3, 0);
        assertEquals(newArrayList(i3, i1, i2), pi1.getChildren());
        assertEquals(pi1, i3.getParent());

        var pi2 = newItem("pi2", null);
        pi2.addChild(i3, 0);
        assertEquals(newArrayList(i1, i2), pi1.getChildren());
        assertEquals(newArrayList(i3), pi2.getChildren());
        assertEquals(pi2, i3.getParent());
    }

    @Test
    public void testRemoveChild() {
        var pi1 = newItem("pi1", null);
        var i1 = newItem("i1", pi1);
        var i2 = newItem("i2", pi1);
        assertEquals(newArrayList(i1, i2), pi1.getChildren());
        assertEquals(pi1, i2.getParent());

        pi1.removeChild(i2);
        assertEquals(newArrayList(i1), pi1.getChildren());
        assertNull(i2.getParent());
    }

    MenuItemEntity newItem(String id, MenuItemEntity parent) {
        var i = new MenuItemEntity();
        i.setItemId(id);
        if (parent != null) {
            i.setParent(parent);
            parent.getChildren().add(i);
        }
        return i;
    }

}