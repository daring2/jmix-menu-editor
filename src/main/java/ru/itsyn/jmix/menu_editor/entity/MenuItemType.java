package ru.itsyn.jmix.menu_editor.entity;

import io.jmix.core.metamodel.datatype.impl.EnumClass;

import javax.annotation.Nullable;


public enum MenuItemType implements EnumClass<String> {

    MENU("MENU"),
    SCREEN("SCREEN"),
    SEPARATOR("SEPARATOR");

    private String id;

    MenuItemType(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static MenuItemType fromId(String id) {
        for (MenuItemType item : MenuItemType.values()) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        return null;
    }
}