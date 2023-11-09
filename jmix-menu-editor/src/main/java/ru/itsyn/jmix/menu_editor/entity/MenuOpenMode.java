package ru.itsyn.jmix.menu_editor.entity;

import io.jmix.core.metamodel.datatype.EnumClass;

import javax.annotation.Nullable;


public enum MenuOpenMode implements EnumClass<String> {

    THIS_TAB("THIS_TAB"),
    NEW_TAB("NEW_TAB"),
    DIALOG("DIALOG");

    private String id;

    MenuOpenMode(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static MenuOpenMode fromId(String id) {
        for (MenuOpenMode item : MenuOpenMode.values()) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        return null;
    }

}