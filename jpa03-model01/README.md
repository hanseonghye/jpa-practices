## Model01 : 단일 엔티티


### 01. Domain

#### 1) 엔티티 클래스: Guestbook
   me.kickscar.practices.jpa03.model01.domain.Guestbook.java 엔티티 매핑 참고  
   
#### 2) ERD 
   <img src="http://assets.kickscar.me:8080/markdown/jpa-practices/30001.png" width="600px" />

#### 3) QueryDSL를 위한 쿼리타입 QClass(쿼리용 클래스, Q로 시작) QGuestbook 생성하기

  1. __QueryDSLRespository.java 컴파일 오류__  
     
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
       
     쿼리타입 클래스 QGuestbook가 없기 때문에 발생!!!
  
  2. __querydsl plugin for gradle를 build.gradle 에 설정하고 Build Task의 build 와 clean 실행을 통해 생성과 삭제를 한다.__ 
  
  3. __querydsl plugin 설정 (build.gradle)__  
  
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
      
     설정은 gradle 버젼에 민감함(gradle 5.4 기준임. gradle wrapper를 제공하기 때문에 큰 문제는 없다)
     
  4. __Build Task의 build 또는 Other Task의 compileJava 함수 실행__
 
     <img src="http://assets.kickscar.me:8080/markdown/jpa-practices/30002.png" width="600px" />
     <br/>

     생성되었다!!!  
     <img src="http://assets.kickscar.me:8080/markdown/jpa-practices/30003.png" width="600px" />
  
  5. __Build Task clean 함수 실행으로 삭제할 수 있다.__
  
  

### 02. Repository

#### 1) Model01JpqlRepository

#### 2) Model01QueryDslRepository

#### 3) Model01JpaRepository extends JpaRepository
  1. __JpaRepository Interface__  
     + Spring Data JPA에서 제공하는 인테페이스로 상속받은 Repoitory Interface 에 기본적인 CRUD 메서드를 제공한다.       
     + 구현체는 애프리케이션 실행 시, Spring Data JPA가 생성해서 제공해 준다.
     + 즉, **데이터 접근 계층(DAO, Repository) 개발할 떄 구현 클래스 없이 인터페이스만 작성해도 개발을 완료할 수 있다.**  

  2. __설정(Spring Boot)__
  
     설정 클래스에 @EnableJpaRepositories 어노테이션으로 JPA Repositories 활성화해야 한다.(코드 주석 참고)
           
     ```java
        @Configuration
        @EnableTransactionManagement
        @EnableJpaRepositories(basePackages = { "me.kickscar.practices.jpa03.model01.repository" })
        public class JpaConfig {
             
        }      
     ```
  3. __작성__  
     
     기본적으로 JpaRepository를 상속하는 Repository 인터페이스를 생성한다.  
     
     ```java
        public interface Model01JpaRepository extends JpaRepository<Guestbook, Long> {
        }
     ```    
     + 이 코드로도 다음과 같은 메소드를 직접 불러 사용할 수 있다.  
       save(S), findOne(Id), exists(Id), count(), detete(T), deleteAll() - CRUD 기능  
       findAll(Sort), findAll(Pageable) - 정열 및 패이징  
     + 더 막강한 기능은 쿼리 메소드 기능이다. (메소드이름으로 내부에서 JPQL를 생성해서 호출, 예제코드 참고)  
     + JPA NamedQuery   
     + QueryDSL과 통합  
     + Specification   
  
       

### 03. Test SpringBoot Application

#### 1) app01.Jpa03SpringBootApp01(Command Line)
  
  1. __Model01JpqlRepository 빈 주입__
  
  2. __insert, select, delete 테스트__ 


#### 2) app02.Jpa03SpringBootApp02(Command Line)

  1. __Model01QueryDslRepository 빈 주입__

  2. __insert, select, delete 테스트__ 


#### 3) app03.Jpa03SpringBootApp03(Rest Web)

  1. __예제는 Spring Boot Rest Web Application으로 작성__
  2. __얘프리케이션이 실행되고 다음 코드에 의해서 BasicCRUDAndQueryMethodTest bean의 run() 메소드가 실행__
  
     ```
        public static void main(String[] args) {
           ConfigurableApplicationContext context = SpringApplication.run( Jpa03SpringBootRestApp03.class, args );
           context.getBean(BasicCRUDAndQueryMethodTest.class).run();
        }
     ```     
     API 서비스 DB 데이터 생성 및 기본 CRUD 메서드 와 몇 개 예제로 만들어 놓은 QueryMethod 테스트 목적    
  
  3. __내부에 작성된 Model01Controller 에 매핑된 URL로 Model01JpaRepository 메소드를 테스트 해 볼수 있다.__  
     
  4. __애플리케이션 싫행 후, URL 접근__  
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
