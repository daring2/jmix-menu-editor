package ru.itsyn.jmix.menu_editor;

import io.jmix.core.annotation.JmixModule;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

@SpringBootConfiguration
@EnableAutoConfiguration
@Import(JmixMenuEditorConfiguration.class)
@PropertySource("classpath:/ru/itsyn/jmix/menu_editor/test-app.properties")
@JmixModule(id = "ru.itsyn.jmix.menu_editor.test", dependsOn = JmixMenuEditorConfiguration.class)
public class JmixMenuEditorTestConfiguration {

    @Bean
    @Primary
    DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .generateUniqueName(true)
                .setType(EmbeddedDatabaseType.HSQL)
                .build();
    }

}
