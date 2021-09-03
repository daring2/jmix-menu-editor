package ru.itsyn.jmix.menu_editor.screen.menu_item;

import org.dom4j.DocumentHelper;
import org.junit.jupiter.api.Test;
import ru.itsyn.jmix.menu_editor.entity.MenuItemType;

import static org.junit.jupiter.api.Assertions.*;

class MenuConfigBuilderTest {

    @Test
    void testGetElementName() {
        var builder = new MenuConfigBuilder();
        assertEquals("menu", builder.getElementName(MenuItemType.MENU));
        assertEquals("separator", builder.getElementName(MenuItemType.SEPARATOR));
        assertEquals("item", builder.getElementName(MenuItemType.SCREEN));
        assertEquals("item", builder.getElementName(null));
    }

    @Test
    void testAddAttributeValue() {
        var builder = new MenuConfigBuilder();
        var e = DocumentHelper.createElement("e1");
        builder.addAttributeValue(e, "a1", null);
        assertNull(e.attribute("a1"));
        assertEquals(0, e.attributeCount());
        builder.addAttributeValue(e, "a1", "v1");
        assertEquals("v1", e.attributeValue("a1"));
        assertEquals(1, e.attributeCount());
        builder.addAttributeValue(e, "a2", 2);
        assertEquals("2", e.attributeValue("a2"));
        builder.addAttributeValue(e, "a3", MenuItemType.MENU);
        assertEquals("MENU", e.attributeValue("a3"));
    }

    @Test
    void testAddContentXml() {
        var builder = new MenuConfigBuilder();
        var e = DocumentHelper.createElement("e1");
        builder.addContentXml(e, null);
        assertEquals(0, e.elements().size());
        builder.addContentXml(e, "<item screen=\"sc1\"/>");
        assertEquals(1, e.elements().size());
        var e1 = e.element("item");
        assertEquals("sc1", e1.attributeValue("screen"));
        var ex1 = assertThrows(RuntimeException.class, () ->
                builder.addContentXml(e, ">invalid<")
        );
        assertEquals("Cannot parse contentXml", ex1.getMessage());
    }

}