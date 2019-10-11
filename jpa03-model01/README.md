## Model01 : 단일 엔티티


### 01. Doamin

#### 1) 엔티티 클래스: Guestbook
   me.kickscar.practices.jpa03.model01.domain.Guestbook.java 엔티티 매핑 참고  
   
#### 2) ERD 
   <img src="http://assets.kickscar.me:8080/markdown/jpa-practices/30001.png" width="600px" />
   <br/>

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


### 03. Test SpringBoot Application

#### 1) Jpa03SpringBootApp01(콘솔)
  
  1. __Model01JpqlRepository 빈 주입__
  
  2. __insert, select, delete 테스트__ 


#### 2) Jpa03SpringBootApp02(콘솔)

  1. __Model01QueryDslRepository 빈 주입__

  2. __insert, select, delete 테스트__ 


#### 3) JPARepository