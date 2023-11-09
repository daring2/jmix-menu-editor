package ru.itsyn.jmix.menu_editor.screen.menu_config;

import io.jmix.flowui.menu.MenuConfig;

public class BaseMenuConfig extends MenuConfig {

    public BaseMenuConfig(MenuConfigDependencies dependencies) {
        super(
                dependencies.resources,
                dependencies.messages,
                dependencies.messageTools,
                dependencies.dom4JTools,
                dependencies.environment,
                dependencies.uiProperties,
                dependencies.modules,
                dependencies.metadata,
                dependencies.metadataTools
        );
    }

}
