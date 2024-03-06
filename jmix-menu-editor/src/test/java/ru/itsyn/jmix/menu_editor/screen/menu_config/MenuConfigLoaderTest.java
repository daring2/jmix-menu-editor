package ru.itsyn.jmix.menu_editor.screen.menu_config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MenuConfigLoaderTest {

    @Autowired
    MenuConfigLoader configLoader;

    @Test
    public void testLoadDefaultConfig() {
        var items = configLoader.loadDefaultConfig();
        assertEquals(1, items.size());
        var rootItem = items.get(0);
        assertEquals("administration", rootItem.getId());
        assertEquals(1, rootItem.getChildren().size());
        var item1 = rootItem.getChildren().get(0);
        assertEquals("menu_MenuEntity.list", item1.getId());
        assertEquals(item1.getId(), item1.getView());
    }

}