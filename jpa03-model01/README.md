## Model01 : 단일 엔티티


### 01. Domain

#### 1) Scheme 
   <img src="http://assets.kickscar.me:8080/markdown/jpa-practices/30001.png" width="600px" />


#### 2) Entity Class: Guestbook
   me.kickscar.practices.jpa03.model01.domain.Guestbook.java 엔티티 매핑 참고  



### 02. Test SpringBoot Application

#### 1) 개발환경
  1. __Java SE 1.8__  
  2. __Spring Boot 2.1.8.Release__   
  3. __Spring Data JPA 2.1.10.Release__   
  4. __Hibernate 5.4.4.Final__  
  5. __H2 Database 1.4.197__  
  6. __Gradle 5.4__   

#### 2) app01
  
  1. __Model01JpqlRepository__  
     + JPQL 기반으로 작성    
     + 간단한 save(Guestbook), findAll(), remove(Guestbook) 등의 CRUD 메소드 구현  

  2. __JpaConfig(JPA Java Config)__
     + Datasource Bean 설정  
     + TransactionManager 설정  
     + PersistenceExceptionTranslationPostProcessor JPA 예외 전환 설정  
     + LocalContainerEntityManagerFactoryBean 엔티티매니저팩토리 설정 (Repository에서 엔티티매니저 빈을 주입받기 위해)  
     + JPA Properties (appication.yml의 JPA 섹션과 비교해 보자)  
  
  3. __Test Application__  
     + Jpa03SpringBootApp01.java  
     + Model01JpqlRepository 빈 주입(Auto Wiring)  
     + Command Line Mode  


#### 3) app02

  1. __Model01QueryDslRepository__  

     + 컴파일 오류  
     
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

  2. __QueryDSL를 위한 쿼리타입 QClass(쿼리용 클래스, Q로 시작) QGuestbook 생성하기__
   
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
  
       <img src="http://assets.kickscar.me:8080/markdown/jpa-practices/30002.png" width="600px" />
       <br/>

       생성되었다!!!  
       <img src="http://assets.kickscar.me:8080/markdown/jpa-practices/30003.png" width="600px" />
  
     + Build Task clean 함수 실행으로 삭제할 수 있다.

  3. __JpaConfig(JPA Java Config)__  

  4. __Test Application__  
     + Jpa03SpringBootApp02.java  
     + Model01QueryDslRepository 빈 주입(Auto Wiring)  
     + Command Line Mode  


#### 3) app03

  1. __Model01JpaRepository__
    
     + JpaRepository Interface 
       - Spring Data JPA에서 제공하는 인테페이스로 상속받은 Repoitory Interface 에 기본적인 CRUD 메서드를 제공한다.         
       - 구현체는 애프리케이션 실행 시, Spring Data JPA가 생성해서 제공해 준다.  
       - 즉, **데이터 접근 계층(DAO, Repository) 개발할 떄 구현 클래스 없이 인터페이스만 작성해도 개발을 완료할 수 있다.**  
     
     + 기본적으로 JpaRepository를 상속하는 Repository 인터페이스를 생성한다.  
     
       ```java
          public interface Model01JpaRepository extends JpaRepository<Guestbook, Long> {
          }
       ```    
       - 이 코드로도 다음과 같은 메소드를 직접 불러 사용할 수 있다.   
         save(S), findOne(Id), exists(Id), count(), detete(T), deleteAll() - CRUD 기능  
         findAll(Sort), findAll(Pageable) - 정열 및 패이징  
       - 더 막강한 기능은 쿼리 메소드 기능이다. (메소드이름으로 내부에서 JPQL를 생성해서 호출, 예제코드 참고)  
       - JPA NamedQuery 작성이 가능하다.   
       - QueryDSL과 통합이 가능하다  
       - Specification 를 통해 검색조건을 다양하게 조립하여 사용할 수 있다.  

  2. __JpaConfig(JPA Java Config)__  
     
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
  
  3. __Test Application__
     + Jpa03SpringBootApp03.java  
     + Rest Web Application
     + Application 실행 후, RepositoryMethodTestAfterBootAppLoaded Bean의 test() 실행(코드 참고)  
       ```
          public static void main(String[] args) {
             ConfigurableApplicationContext context = SpringApplication.run( Jpa03SpringBootRestApp03.class, args );
             context.getBean( RepositoryMethodTestAfterBootAppLoaded.class ).run();
          }
       ```     
       RepositoryMethodTestAfterBootAppLoaded:   
       데이터베이스에 테스트 데이터 생성과 Model01JpaRepository의 기본 CRUD 메서드와 예제 QueryMethod 테스트 목적으로 만든 클래스   
       실행 후, query 로그 꼭 확인해 볼 것. 
  
     + Model01Controller에 매핑된 URL로 Model01JpaRepository의 메소드들을 테스트 해 볼수 있다.
       ```ssh
         $ curl http://localhost:8088/model01/list
         [{"no":3,"name":"도우넛","contents":"안녕4","password":"1234","regDate":"2019-10-11T07:25:31.972+0000"},{"no":2,"name":"마이콜","contents":"안녕2","password":"1234","regDate":"2019-10-11T07:25:31.9"}, {"no":1,"name":"둘리","contents":"안녕1","password":"1234","regDate":"2019-10-11T07:25:31.912+0000"}] 
         $
         $ curl http://localhost:8088/model01/list/0
         [{"no":2,"name":"마이콜","contents":"안녕2","password":"1234","regDate":"2019-10-11T07:25:31.969+0000"},{"no":1,"name":"둘리","contents":"안녕1","password":"1234","regDate":"2019-10-11T07:25:31.9}]
         $
         $ curl http://localhost:8088/model01/list/1 
         [{"no":3,"name":"도우넛","contents":"안녕4","password":"1234","regDate":"2019-10-11T07:25:31.972+0000"}]
         $
       ```
       [!!!] 윈도우에서는 콘솔창이 기본 cp949이기 때문에 curl사용시 응답내용의 한글이 깨질 수 있다. 다음과 같이 테스트를 하면 UTF-8 인코딩 내용도 제대로 출력된다.  
       <img src="http://assets.kickscar.me:8080/markdown/jpa-practices/30007.png" width="600px" />
       <br/>
