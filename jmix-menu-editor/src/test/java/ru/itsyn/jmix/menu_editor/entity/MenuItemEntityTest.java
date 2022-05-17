package ru.itsyn.jmix.menu_editor.entity;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MenuItemEntityTest {

    @Test
    public void testHasAncestor() {
        var rootItem = newItem("root", null);
        assertFalse(rootItem.hasAncestor(rootItem));
        var item1 = newItem("item1", rootItem);
        assertFalse(rootItem.hasAncestor(item1));
        assertTrue(item1.hasAncestor(rootItem));
        var item2 = newItem("item2", rootItem);
        assertTrue(item2.hasAncestor(rootItem));
        assertFalse(item2.hasAncestor(item1));
        var item21 = newItem("item21", item2);
        assertTrue(item21.hasAncestor(item2));
        assertTrue(item21.hasAncestor(rootItem));
    }

    @Test
    public void testVisitItems() {
        var rootItem = newItem("root", null);
        var item1 = newItem("item1", rootItem);
        var item2 = newItem("item2", rootItem);
        var item21 = newItem("item21", item2);
        var items = new ArrayList<MenuItemEntity>();
        rootItem.visitItems(items::add);
        assertThat(items, equalTo(List.of(rootItem, item1, item2, item21)));
    }

    @Test
    public void testAddChild() {
        var parentItem1 = newItem("parentItem1", null);
        var item1 = newItem("item1", parentItem1);
        var item2 = newItem("item2", null);
        assertThat(parentItem1.getChildren(), equalTo(List.of(item1)));
        assertThat(item2.getParent(), nullValue());

        parentItem1.addChild(item2, -1);
        assertThat(parentItem1.getChildren(), equalTo(List.of(item1, item2)));
        assertThat(item2.getParent(), equalTo(parentItem1));

        var item3 = newItem("item3", null);
        parentItem1.addChild(item3, 0);
        assertThat(parentItem1.getChildren(), equalTo(List.of(item3, item1, item2)));
        assertThat(item3.getParent(), equalTo(parentItem1));

        var parentItem2 = newItem("parentItem2", null);
        parentItem2.addChild(item3, 0);
        assertThat(parentItem1.getChildren(), equalTo(List.of(item1, item2)));
        assertThat(parentItem2.getChildren(), equalTo(List.of(item3)));
        assertThat(item3.getParent(), equalTo(parentItem2));
    }

    @Test
    public void testRemoveChild() {
        var parentItem1 = newItem("parentItem1", null);
        var item1 = newItem("item1", parentItem1);
        var item2 = newItem("item2", parentItem1);
        assertThat(parentItem1.getChildren(), equalTo(List.of(item1, item2)));
        assertThat(item2.getParent(), equalTo(parentItem1));

        parentItem1.removeChild(item2);
        assertThat(parentItem1.getChildren(), equalTo(List.of(item1)));
        assertThat(item2.getParent(), nullValue());
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