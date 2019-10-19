package me.kickscar.practices.jpa03.model02.conf;

import me.kickscar.practices.jpa03.model02.config.JpaConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(JpaConfig.class)
public class JpaRepositoryTestConfig {
}
