package ru.itsyn.jmix.menu_editor.screen.menu_config;

import io.jmix.core.*;
import io.jmix.core.common.xmlparsing.Dom4jTools;
import io.jmix.flowui.UiProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component("menu_MenuConfigDependencies")
public class MenuConfigDependencies {
    @Autowired
    public Resources resources;
    @Autowired
    public Messages messages;
    @Autowired
    public MessageTools messageTools;
    @Autowired
    public Dom4jTools dom4JTools;
    @Autowired
    public Environment environment;
    @Autowired
    public UiProperties uiProperties;
    @Autowired
    public JmixModules modules;
    @Autowired
    public Metadata metadata;
    @Autowired
    public MetadataTools metadataTools;
}
