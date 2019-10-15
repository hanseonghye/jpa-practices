package me.kickscar.practices.jpa03.model02.conf;

import me.kickscar.practices.jpa03.model02.config.JpaConfig;
import me.kickscar.practices.jpa03.model02.config.JpqlConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(JpqlConfig.class)
@ComponentScan({"me.kickscar.practices.jpa03.model02.repository"})
public class JpqlRepositoryTestConfig {
}
