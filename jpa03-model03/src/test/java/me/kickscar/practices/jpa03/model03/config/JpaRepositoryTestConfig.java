package me.kickscar.practices.jpa03.model03.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(JpaConfig.class)
public class JpaRepositoryTestConfig {
}
