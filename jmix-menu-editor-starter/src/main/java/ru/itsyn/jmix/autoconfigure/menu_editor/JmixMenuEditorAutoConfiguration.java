package ru.itsyn.jmix.autoconfigure.menu_editor;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import ru.itsyn.jmix.menu_editor.JmixMenuEditorConfiguration;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@Import({JmixMenuEditorConfiguration.class})
public class JmixMenuEditorAutoConfiguration {
}

