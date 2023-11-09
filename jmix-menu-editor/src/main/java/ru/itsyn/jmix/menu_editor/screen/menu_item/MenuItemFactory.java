package ru.itsyn.jmix.menu_editor.screen.menu_item;

import io.jmix.core.Messages;
import io.jmix.flowui.menu.MenuItem;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.itsyn.jmix.menu_editor.entity.MenuItemEntity;
import ru.itsyn.jmix.menu_editor.entity.MenuItemType;
import ru.itsyn.jmix.menu_editor.entity.MenuOpenMode;
import ru.itsyn.jmix.menu_editor.util.MenuItemHelper;

import java.time.Instant;
import java.util.Date;
import java.util.stream.Collectors;

import static io.jmix.core.UuidProvider.createUuid;
import static org.apache.commons.lang3.StringUtils.*;

@Component("menu_MenuItemFactory")
public class MenuItemFactory {

    public static final String ROOT_ITEM_ID = "rootItem";
    public static final String MESSAGE_PACK = MenuItemFactory.class.getPackageName();

    @Autowired
    Messages messages;
    @Autowired
    MenuItemHelper menuItemHelper;

    public MenuItemEntity createRootItem() {
        var entity = new MenuItemEntity();
        entity.setId(ROOT_ITEM_ID);
        entity.setItemType(MenuItemType.MENU);
        entity.setCaptionKey(messages.getMessage(MESSAGE_PACK, "rootItemCaption"));
        menuItemHelper.updateItemCaption(entity);
        return entity;
    }

    public MenuItemEntity createItem(MenuItem item) {
        var descriptor = item.getDescriptor();
        var entity = new MenuItemEntity();
        entity.setId(item.getId());
        entity.setCaptionKey(item.getTitle());
        entity.setDescription(item.getDescription());
        entity.setStyleName(item.getClassNames());
        entity.setIcon(item.getIcon());
        if (item.isMenu()) {
            entity.setItemType(MenuItemType.MENU);
            entity.setExpanded(item.isOpened());
        } else if (item.isSeparator()) {
            entity.setItemType(MenuItemType.SEPARATOR);
            entity.setId(buildSeparatorId(item));
            entity.setCaptionKey(messages.getMessage(entity.getItemType()));
        } else {
            entity.setItemType(MenuItemType.SCREEN);
            entity.setScreen(item.getView());
//            entity.setRunnableClass(item.getRunnableClass());
            entity.setBean(item.getBean());
            entity.setBeanMethod(item.getBeanMethod());
            if (descriptor != null) {
                entity.setOpenMode(MenuOpenMode.fromId(descriptor.attributeValue("openType")));
                entity.setShortcut(descriptor.attributeValue("shortcut"));
                entity.setContentXml(buildContentXml(descriptor));
            }
        }
        if (descriptor != null) {
            entity.setCreatedDate(parseDate(descriptor, "createdDate"));
            entity.setCreatedBy(descriptor.attributeValue("createdBy"));
            entity.setLastModifiedDate(parseDate(descriptor, "lastModifiedDate"));
            entity.setLastModifiedBy(descriptor.attributeValue("lastModifiedBy"));
            entity.setComment(descriptor.attributeValue("comment"));
        }
        menuItemHelper.updateItemCaption(entity);
        return entity;
    }

    String buildSeparatorId(MenuItem item) {
        var parent = item.getParent();
        if (parent == null)
            return createUuid().toString();
        return "separator-" + parent.getChildren().indexOf(item);
    }

    String buildContentXml(Element descriptor) {
        if (descriptor.content().isEmpty())
            return null;
        var xml = descriptor.asXML();
        xml = substringAfter(xml, ">");
        xml = substringBeforeLast(xml, "<");
        return xml.lines().map(String::trim)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.joining("\n"));
    }

    Date parseDate(Element descriptor, String attribute) {
        var value = descriptor.attributeValue(attribute);
        if (isBlank(value))
            return null;
        return Date.from(Instant.parse(value));
    }

}
