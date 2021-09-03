package ru.itsyn.jmix.menu_editor.autoconfigure;

import ru.itsyn.jmix.menu_editor.JmixMenuEditorConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({JmixMenuEditorConfiguration.class})
public class JmixMenuEditorAutoConfiguration {
}

