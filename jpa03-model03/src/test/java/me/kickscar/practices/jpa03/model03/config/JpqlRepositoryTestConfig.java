package me.kickscar.practices.jpa03.model03.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(JpqlConfig.class)
@ComponentScan({"me.kickscar.practices.jpa03.model03.repository"})
public class JpqlRepositoryTestConfig {
}
