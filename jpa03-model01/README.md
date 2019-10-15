## Model01 : 단일 엔티티


### 01. Domain

#### 1) Scheme 
   <img src="http://assets.kickscar.me:8080/markdown/jpa-practices/30001.png" width="600px" />


#### 2) Entity Class: Guestbook
   me.kickscar.practices.jpa03.model01.domain.Guestbook.java 엔티티 매핑 참고  



### 02. Test

#### 1) 개발환경
  1. __Java SE 1.8__  
  2. __Spring Boot 2.1.8.Release__   
  3. __Spring Data JPA 2.1.8.Release__   
  4. __Hibernate 5.4.4.Final__  
  5. __H2 Database 1.4.197__  
  6. __Gradle 5.4__   


#### 2) Jpql GuestbookRepository Test : Guestbook JPQL 기반 Repository
  
  1. __me.kickscar.practices.jpa03.model01.config.JpqlConfig__
     + Datasource Bean 설정  
     + **TransactionManager 설정**
     + PersistenceExceptionTranslationPostProcessor JPA 예외 전환 설정  
     + LocalContainerEntityManagerFactoryBean 엔티티매니저팩토리 설정 (Repository에서 엔티티매니저 빈을 주입받기 위해)  
     + EntityManager 빈 등록(Repository 빈에 주입)
     + JPA Properties (appication.yml의 JPA 섹션과 비교해 보자)  

  2. __JPA 트랜잭션 관리에 관해서...(중요개념)__
      + 트랜잭션과 영속성켄텍스트
        - 객체 저장, 수정, 삭제, 탐색(특히, Fetch.LAZY)는 영속성켄텍스트 안의 객체를 대상으로 한다.
        - JPA에서는 트랜잭션이 시작할 때 영속성컨텍스트를 생성하고 트랜잭션이 끝날 때 영속성컨텍스트를 종료한다.(트랜잭션 범위 == 영속성 컨텍스트의 생존범위)
        - 이는 트랜잭션이 시작과 끝에 많은 경우의 JPA 작업을 해야 한다는 뜻이다.

      + Spring Conatiner 에서의 트랜잭션과 영속성컨텍스트 
        - Spring Conatiner는 하나의 쓰레드에 하나의 트랜잭션을 할당한다.(중요)
        - SpringMVC에서는 하나의 요청에 한 개의 쓰레드가 할당 된다.
        - 이 쓰레드에서, 서비스와 레포지토리를 거치면서 다수의 엔티티매니저가 객체의 영속성에 관여할 수 있으나 하나의 쓰레드 즉, 트랜잭션이 같기 때문에 영속성컨텍스트는 같다.
        - 반대로, **여러 요청에 대한 여러 쓰레드에서 같은 엔티티매니저를 쓰는 경우는 SpringMVC에서는 흔한 일이다. 이런 경우는 영속성컨텍스트가 다르기 때문에 멀티쓰레드에서는 안전하다.**
        - 스프링 MVC에서는 비즈니스 로직을 시작하는 서비스 계층에서 AOP가 적용된 @Transactional으로 트랜잭션을 시작하는 것이 보통이다.
      
      + 결론은 Spring Container가 알아서 쓰레드와 연관된 트랜잭션과 영속성켄텍스트의 관리를 맡아주니 개발자는 @Transactional과 비즈니스 로직에만 집중하면 된다.
      + 하지만, 이 내용을 숙지하지 못하면 지연로딩이나 프록시초기화 등에 문제가 발생하면 해결에 많은 노력을 기울여야 하기 때문에 꼭 이해해야 할 개념이다. 
  
  3. __me.kickscar.practices.jpa03.model01.repository.JpqlGuestbookRepository__  
     + JPQL 기반으로 작성    
     + save(Guestbook) : 영속화 
     + findAll() : TypedQuery 기반 Projection 및 Order by 지원
     + remove() : Query 사용
     + count : JPQL 기반 집합함수 적용
      
  4. __me.kickscar.practices.jpa03.model01.repository.JpqlGuestbookRepositoryTest__  
     + @FixMethodOrder : 메소드 순서 정하기
     + @Transactional : 모든 메소드에 트랜잭션  AOP 적용
     + 방명록에 사용하는 메소드만 테스트


#### 3) QueryDSL GuestbookRepository Test : Guestbook QueryDSL 기반 Repository

  1. __me.kickscar.practices.jpa03.model01.config.JpqlConfig__
     + JPQL과 설정파일 동일(실제로 QueryDSL은 JPQL의 쓰기쉽게, 특히 Criteria 대용의 래퍼 라이브러리이다)
     + QueryDSL Repository에 JPAQueryFactory를 주입하기 위한 빈설정이 추가적으로 필요하다.
     
       ```
          @PersistenceContext
          private EntityManager entityManager;
       
       ```
 
       ```
         @Bean
         public JPAQueryFactory jpaQueryFactory() {
            return new JPAQueryFactory( entityManager );
         }
       ```

  2. __me.kickscar.practices.jpa03.model01.repository.QueryDslGuestbookRepository__  
     + QueryDSL를 편하게 쓰기 위해 JPAQueryFactory Bean을 주입 받는다.
     + 영속화 관리를 위해 EntityManager 주입 받는다.
     + **컴파일 오류**
       
       ```
         import static me.kickscar.practices.jpa03.model01.domain.QGuestbook.guestbook;  
            .  
            .  
            .  
 
            @Transactional
            public Boolean remove( Guestbook parameter ) {
               return queryFactory
                  .delete( guestbook )
                  .where( guestbook.no.eq( parameter.getNo() ).and( guestbook.password.eq( parameter.getPassword() ) ) )
                  .execute() == 1;
            }
       ```
       **쿼리타입 클래스 QGuestbook가 없기 때문에 발생!!!**

  3. __QueryDSL를 위한 쿼리타입 QClass(쿼리용 클래스, Q로 시작) QGuestbook 생성하기__
   
     + querydsl plugin for gradle를 build.gradle 에 설정하고 Build Task의 build 와 clean 실행을 통해 생성과 삭제를 한다. 
     + querydsl plugin 설정 (build.gradle)
     
       - dependency 추가
         
       ```
         dependencies {

           compile('org.springframework.boot:spring-boot-starter-web:2.1.8.RELEASE')
           compile('org.springframework.boot:spring-boot-starter-data-jpa:2.1.8.RELEASE')
           compile('org.hibernate:hibernate-entitymanager:5.4.4.Final')
           compile('mysql:mysql-connector-java:8.0.16')
           runtimeOnly('com.h2database:h2:1.4.197')
           testCompile('junit:junit:4.12')

           compile('com.querydsl:querydsl-jpa:4.1.4') // querydsl-jpa 추가
           compile('com.mysema.querydsl:querydsl-apt:3.7.4') // querydsl-apt 추가 

           // annotationProcessor 추가
           annotationProcessor(
              'com.querydsl:querydsl-apt:4.1.4:jpa',
              'org.hibernate.javax.persistence:hibernate-jpa-2.1-api:1.0.2.Final',
              'javax.annotation:javax.annotation-api:1.3.2'
           )
         }     
       ```
         
       - querydsl gradle plugin 설정
       
       ```
          /* querydsl PlugIn Configuration */

          def querydslGenDirectory = 'src/main/generated'

          sourceSets {
            main.java.srcDirs += [ querydsldslGenDirectory ]
          }

          tasks.withType(JavaCompile) {
             options.encoding = 'UTF-8'
             options.annotationProcessorGeneratedSourcesDirectory = file(querydslGenDirectory)
          }

          clean.doLast {
             file(querydslGenDirectory).deleteDir()
          }  
       ```
       설정은 gradle 버젼에 매우 민감하다(gradle 5.4 기준임. 이 프로젝트의 gradle wrapper를 빌드에 사용하면 큰 문제가 없다)  
     
     + Build Task의 build 또는 Other Task의 compileJava 함수 실행  
  
       <img src="http://assets.kickscar.me:8080/markdown/jpa-practices/30002.png" width="400px" />
       <br/>

       생성되었다!!!  
       <img src="http://assets.kickscar.me:8080/markdown/jpa-practices/30003.png" width="400px" />
  
     + Build Task clean 함수 실행으로 삭제할 수 있다.
     
  4. __me.kickscar.practices.jpa03.model01.repository.QueryDSLGuestbookRepositoryTest__  
       + @FixMethodOrder : 메소드 순서 정하기
       + @Transactional : 모든 메소드에 트랜잭션  AOP 적용
       + 방명록에 사용하는 메소드만 테스트
       + JPQL 테스트 케이스와 동일!!!



#### 4) JpaRepository 상속받은 GuestbookRepository Test : Guestbook Spring Data JPA 기반 Repository

  1. __me.kickscar.practices.jpa03.model01.config.JpaConfig__
     + JPQL(QueryDSL 포함) 설정과 다르다.
     + 설정 클래스에 @EnableJpaRepositories 어노테이션으로 JPA Repositories 활성화해야 한다.(코드 주석 참고)
       ```
          @Configuration
          @EnableTransactionManagement
          @EnableJpaRepositories(basePackages = { "me.kickscar.practices.jpa03.model01.repository" })
          public class JpaConfig {
                 .  
                 .
                 .    
          }      
       ```

  2. __me.kickscar.practices.jpa03.model01.repository.JpaGuestbookRepository__
    
     + JpaRepository Interface 
       - Spring Data JPA에서 제공하는 인테페이스로 상속받은 Repoitory Interface 에 기본적인 CRUD 메서드를 제공한다.         
       - 구현체는 애프리케이션 실행 시, Spring Data JPA가 생성해서 제공해 준다.  
       - 즉, **데이터 접근 계층(DAO, Repository) 개발할 떄 구현 클래스 없이 인터페이스만 작성해도 개발을 완료할 수 있다.**  
     
     + 기본적으로 JpaRepository를 상속하는 Repository 인터페이스를 생성한다.  
     
       ```java
          public interface JpaGuestbookRepository extends JpaRepository<Guestbook, Long> {
          }
       ```    
       - 이 코드로도 다음과 같은 메소드를 직접 불러 사용할 수 있다.   
         save(S), findOne(Id), exists(Id), count(), detete(T), deleteAll() - CRUD 기능  
         findAll(Sort), findAll(Pageable) - 정열 및 패이징  
       - 더 막강한 기능은 쿼리 메소드 기능이다. (메소드이름으로 내부에서 JPQL를 생성해서 호출, 예제코드 참고)  
       - JPA NamedQuery 작성이 가능하다.   
       - QueryDSL과 통합이 가능하다  
       - Specification 를 통해 검색조건을 다양하게 조립하여 사용할 수 있다.  
  
  4. __me.kickscar.practices.jpa03.model01.repository.JpaGuestbookRepositoryTest__  
       + @FixMethodOrder : 메소드 순서 정하기
       + @Transactional : 모든 메소드에 트랜잭션  AOP 적용
       + 방명록에 사용하는 메소드만 테스트
       + JPQL 테스트 케이스와 동일!!!
