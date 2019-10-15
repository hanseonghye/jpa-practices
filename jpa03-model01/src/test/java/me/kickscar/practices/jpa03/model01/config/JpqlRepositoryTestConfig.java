package me.kickscar.practices.jpa03.model01.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(JpqlConfig.class)
@ComponentScan( basePackages = { "me.kickscar.practices.jpa03.model01.repository" })
public class JpqlRepositoryTestConfig {
}
