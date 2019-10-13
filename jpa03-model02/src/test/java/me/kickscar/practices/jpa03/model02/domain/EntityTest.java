package me.kickscar.practices.jpa03.model02.domain;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.sql.DataSource;
import java.util.Properties;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {EntityTest.TestJpaConfig.class})
public class EntityTest {
    @PersistenceUnit
    EntityManagerFactory emf;

    @Test
    public void testEntityManagerFactoryNull(){
        /* 로그 DDL 생성 확인 할 것 */
        /* alter table drop foreign key ....  -> 오류 발생할 수 있음 */

        assertNotNull(emf);
    }

    @Configuration
    public static class TestJpaConfig {
        @Bean
        public DataSource dataSource() {
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName( "org.h2.Driver" );
            dataSource.setUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;MODE=MYSQL");
            dataSource.setUsername("sa");
            dataSource.setPassword("");
            return dataSource;
        }

        @Bean
        public PersistenceExceptionTranslationPostProcessor exceptionTranslation(){
            return new PersistenceExceptionTranslationPostProcessor();
        }

        @Bean
        public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
            LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
            em.setDataSource( dataSource() );
            em.setPackagesToScan(new String[] { "me.kickscar.practices.jpa03.model02.domain" });

            JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
            em.setJpaVendorAdapter(vendorAdapter);
            em.setJpaProperties( jpaProperties() );

            return em;
        }

        Properties jpaProperties() {
            Properties properties = new Properties();

            properties.setProperty( "jpa.generate-ddl", "true" );
            properties.setProperty( "hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect" );
            properties.setProperty( "hibernate.show_sql", "true" );
            properties.setProperty( "hibernate.format_sql", "true" );
            properties.setProperty( "hibernate.use_sql_comments", "true" );
            properties.setProperty( "hibernate.id.new_generator_mappings", "true" );
            properties.setProperty( "hibernate.hbm2ddl.auto", "create-drop" );

            return properties;
        }
    }
}
