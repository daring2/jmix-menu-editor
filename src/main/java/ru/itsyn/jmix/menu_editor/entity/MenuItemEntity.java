package ru.itsyn.jmix.menu_editor.entity;

import io.jmix.core.entity.annotation.JmixId;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.core.metamodel.annotation.JmixProperty;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@JmixEntity(name = "menu_MenuItemEntity", annotatedPropertiesOnly = true)
public class MenuItemEntity {

    @NotNull
    @JmixId
    @JmixProperty(mandatory = true)
    private String id;

    @JmixProperty
    private String captionKey;

    @JmixProperty
    private String itemType;

    @JmixProperty
    private MenuItemEntity parent;

    @JmixProperty
    private String screen;

    @JmixProperty
    private String runnableClass;

    @JmixProperty
    private String bean;

    @JmixProperty
    private String beanMethod;

    @JmixProperty
    private String openMode;

    @JmixProperty
    private Boolean expanded;

    @JmixProperty
    private String shortcut;

    @JmixProperty
    private String description;

    @JmixProperty
    private String styleName;

    @JmixProperty
    private String icon;

    @JmixProperty
    private List<MenuItemEntity> children = new ArrayList<>();

    @JmixProperty
    private String contentXml;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCaptionKey() {
        return captionKey;
    }

    public void setCaptionKey(String captionKey) {
        this.captionKey = captionKey;
    }

    public MenuItemType getItemType() {
        return itemType == null ? null : MenuItemType.fromId(itemType);
    }

    public void setItemType(MenuItemType itemType) {
        this.itemType = itemType == null ? null : itemType.getId();
    }

    public MenuItemEntity getParent() {
        return parent;
    }

    public void setParent(MenuItemEntity parent) {
        this.parent = parent;
    }

    public String getScreen() {
        return screen;
    }

    public void setScreen(String screen) {
        this.screen = screen;
    }

    public String getRunnableClass() {
        return runnableClass;
    }

    public void setRunnableClass(String runnableClass) {
        this.runnableClass = runnableClass;
    }

    public String getBean() {
        return bean;
    }

    public void setBean(String bean) {
        this.bean = bean;
    }

    public String getBeanMethod() {
        return beanMethod;
    }

    public void setBeanMethod(String beanMethod) {
        this.beanMethod = beanMethod;
    }

    public MenuOpenMode getOpenMode() {
        return openMode == null ? null : MenuOpenMode.fromId(openMode);
    }

    public void setOpenMode(MenuOpenMode openMode) {
        this.openMode = openMode == null ? null : openMode.getId();
    }

    public Boolean getExpanded() {
        return expanded;
    }

    public void setExpanded(Boolean expanded) {
        this.expanded = expanded;
    }

    public String getShortcut() {
        return shortcut;
    }

    public void setShortcut(String shortcut) {
        this.shortcut = shortcut;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStyleName() {
        return styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<MenuItemEntity> getChildren() {
        return children;
    }

    public void setChildren(List<MenuItemEntity> children) {
        this.children = children;
    }

    public String getContentXml() {
        return contentXml;
    }

    public void setContentXml(String contentXml) {
        this.contentXml = contentXml;
    }

    @InstanceName
    @JmixProperty
    public String getCaption() {
        return captionKey;
    }

    public boolean isMenu() {
        return getItemType() == MenuItemType.MENU;
    }

    public void visitItems(Consumer<MenuItemEntity> consumer) {
        consumer.accept(this);
        children.forEach(i -> i.visitItems(consumer));
    }

    public boolean hasAncestor(MenuItemEntity item) {
        var pi = getParent();
        while (pi != null) {
            if (pi.equals(item))
                return true;
            pi = pi.getParent();
        }
        return false;
    }

    public int getChildIndex(MenuItemEntity item) {
        return children.indexOf(item);
    }

    public void addChild(MenuItemEntity item, int index) {
        var pi = item.getParent();
        if (pi != null)
            pi.getChildren().remove(item);
        item.setParent(this);
        if (index == -1)
            index = children.size();
        children.add(index, item);
    }

    public void removeChild(MenuItemEntity item) {
        item.setParent(null);
        children.remove(item);
    }

}