package ru.itsyn.jmix.menu_editor.screen.menu_item;

import io.jmix.core.Messages;
import io.jmix.flowui.menu.MenuItem;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.itsyn.jmix.menu_editor.entity.MenuItemType;
import ru.itsyn.jmix.menu_editor.entity.MenuOpenMode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static ru.itsyn.jmix.menu_editor.screen.menu_item.MenuItemFactory.MESSAGE_PACK;
import static ru.itsyn.jmix.menu_editor.screen.menu_item.MenuItemFactory.ROOT_ITEM_ID;

class MenuItemFactoryTest {

    MenuItemFactory factory;

    @BeforeEach
    public void init() {
        factory = new MenuItemFactory();
        factory.messages = mock(Messages.class);
        factory.menuItemHelper = new TestMenuItemHelper();
    }

    @Test
    public void testCreateRootItem() {
        when(factory.messages.getMessage(MESSAGE_PACK, "rootItemCaption"))
                .thenReturn("root caption");
        var ri = factory.createRootItem();
        assertEquals(ROOT_ITEM_ID, ri.getId());
        assertEquals(MenuItemType.MENU, ri.getItemType());
        assertEquals("root caption", ri.getCaptionKey());
        assertEquals("rootItem.caption", ri.getCaption());
    }

    @Test
    public void testCreateItem() {
        when(factory.messages.getMessage(anyString()))
                .then(i -> i.getArgument(0) + ".message");
        when(factory.messages.getMessage(MenuItemType.SEPARATOR))
                .thenReturn("separator caption");

        var mi = new MenuItem("mi1");
        mi.setTitle("caption1");
        mi.setDescription("desc1");
        mi.setClassNames("style1");
        mi.setIcon("icon1");
        mi.setOpened(true);
        mi.setView("screen1");
//        mi.setRunnableClass("runnableClass");
        mi.setBean("bean1");
        mi.setBeanMethod("beanMethod1");

        mi.setMenu(true);
        mi.setSeparator(false);
        assertMenuItem(mi, MenuItemType.MENU);

        mi.setMenu(false);
        mi.setSeparator(false);
        assertMenuItem(mi, MenuItemType.SCREEN);

        var de = DocumentHelper.createElement("d");
        de.addAttribute("openType", "NEW_TAB");
        de.addAttribute("resizable", "true");
        de.addAttribute("shortcutCombination", "shortcut1");
        mi.setDescriptor(de);
        assertMenuItem(mi, MenuItemType.SCREEN);

        de.addElement("param").addAttribute("name", "p1");
        de.addElement("param").addAttribute("name", "p2");

        mi.setMenu(false);
        mi.setSeparator(true);
        assertMenuItem(mi, MenuItemType.SEPARATOR);

        var ci = new MenuItem(mi, "ci1");
        ci.setMenu(false);
        ci.setSeparator(true);
        assertMenuItem(ci, MenuItemType.SEPARATOR);
    }

    void assertMenuItem(MenuItem mi, MenuItemType itemType) {
        var item = factory.createItem(mi);
        assertEquals(itemType, item.getItemType());
        assertEquals(item.getId() + ".caption", item.getCaption());
        if (itemType != MenuItemType.SEPARATOR) {
            assertEquals("caption1", item.getCaptionKey());
            assertEquals("desc1", item.getDescription());
            assertEquals("style1", item.getStyleName());
            assertEquals("icon1", item.getIcon());
        }
        if (itemType == MenuItemType.MENU) {
            assertEquals(true, item.getExpanded());
            assertNull(item.getScreen());
        } else if (itemType == MenuItemType.SEPARATOR) {
            assertEquals("separator caption", item.getCaptionKey());
            assertNull(item.getScreen());
            if (item.getParent() != null) {
                assertEquals("separator-0", item.getId());
            }
        } if (itemType == MenuItemType.SCREEN) {
            assertEquals("screen1", item.getScreen());
//            assertEquals("runnableClass", item.getRunnableClass());
            assertEquals("bean1", item.getBean());
            assertEquals("beanMethod1", item.getBeanMethod());
            assertNull(item.getExpanded());
            var descriptor = mi.getDescriptor();
            if (descriptor != null) {
//                assertEquals(MenuOpenMode.NEW_TAB, item.getOpenMode());
                assertEquals("shortcut1", item.getShortcut());
            } else {
                assertNull(item.getOpenMode());
                assertNull(item.getShortcut());
            }
            if (descriptor != null && !descriptor.content().isEmpty()) {
                assertEquals("<param name=\"p1\"/><param name=\"p2\"/>", item.getContentXml());
            } else {
                assertNull(item.getContentXml());
            }
        }
    }

    @Test
    public void testBuildContentXml() throws Exception {
        checkContextXml("<root/>", null);
        checkContextXml("<root><p1/><p2/></root>", "<p1/><p2/>");
        checkContextXml("<root>\n  <p1/>  \n\n  <p2/>\n</root>", "<p1/>\n<p2/>");
    }

    void checkContextXml(String docXml, String result) throws DocumentException {
        var doc = DocumentHelper.parseText(docXml);
        var content = factory.buildContentXml(doc.getRootElement());
        assertEquals(result, content);
    }

}