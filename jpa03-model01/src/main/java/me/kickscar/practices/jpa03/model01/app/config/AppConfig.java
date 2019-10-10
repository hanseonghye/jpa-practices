package me.kickscar.practices.jpa03.model01.app.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@ComponentScan( basePackages = { "me.kickscar.practices.jpa03.model01.repository" } )
public class AppConfig {

    // EntityManagerFactory가 인식할 수 있게끔!
    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    // Connection Pool DataSource(MySQL)
    public DataSource dataSourceMySQL() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName( "com.mysql.jdbc.Driver" );
        dataSource.setUrl("jdbc:mysql://localhost:3306/jpadb?serverTimezone=UTC&useSSL=false&characterEncoding=utf8");
        dataSource.setUsername("jpadb");
        dataSource.setPassword("jpadb");

        return dataSource;
    }

    @Bean
    public DataSource dataSourceH2() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName( "org.h2.Driver" );

        // H2 Memory Database URL 옵션 주의 할 것! (툭히, B_CLOSE_DELAY=-1)
        dataSource.setUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;MODE=MYSQL");

        dataSource.setUsername("sa");
        dataSource.setPassword("");

        return dataSource;
    }

    @Bean
    // 트랜잭션 관리자 등록
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);

        return transactionManager;
    }

    @Bean
    // JPA 예외를 스프링 예외로 변환
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation(){
        return new PersistenceExceptionTranslationPostProcessor();
    }

    @Bean
    // JPA 설정 ( 엔티티 매니저 팩토리 등록 )
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();

        // 데이터베이스(Connection Pool DataSource)
        // 예제는 H2 Memory DB 사용
        em.setDataSource( dataSourceH2() );

        // 엔티티(@Entity) 탐색 시작 위치
        em.setPackagesToScan(new String[] { "me.kickscar.practices.jpa03.model01.domain" });

        // 하이버네이트 구현체 사용
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        // 하이버네이트 상세 설정
        em.setJpaProperties( jpaProperties() );

        return em;
    }

    @Bean
    // JPAQueryFatory: EntityManager 를 주입받아 생성
    // QueryDSL 지원 레포지토리에서는 편하게 사용할 수 있다.
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory( entityManager );
    }


    Properties jpaProperties() {
        Properties properties = new Properties();

        /* 하이버네이트 상세 설정 */
        properties.setProperty( "jpa.generate-ddl", "true" );

        // H2 Memory DB를 쓰고 있지만 지원 Mode가 MySQL이기 때문에 큰 문제 없음.
        // properties.setProperty( "hibernate.dialect", "org.hibernate.dialect.H2Dialect" );
        properties.setProperty( "hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect" );

        properties.setProperty( "hibernate.show_sql", "true" );
        properties.setProperty( "hibernate.format_sql", "true" );
        properties.setProperty( "hibernate.use_sql_comments", "true" );
        properties.setProperty( "hibernate.id.new_generator_mappings", "true" );
        properties.setProperty( "hibernate.hbm2ddl.auto", "create-drop" );

        return properties;
    }
}
