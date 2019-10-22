## Model02 : 다대일(ManyToOne) - 단방향


### 01. Domain

#### 1) 테이블 연관관계 VS 객체 연관관계

   <img src="http://assets.kickscar.me:8080/markdown/jpa-practices/32001.png" width="400px" />
   <br>
   <img src="http://assets.kickscar.me:8080/markdown/jpa-practices/32002.png" width="400px" />
   <br>
   
   1. __엔티티의 연관관계를 매핑할 때는 다음의 세가지 조건을 기준으로 매핑한다.__
      + **다중성**(Multiplicity, 데이터베이스 Relation에 가까운 개념)  
      + **방향성**(Direction, 객체지향의 Association에 가까운 개념)  
      + **연관관계의 주인(Owner of Associations)**  
   
   2. __보통은 서비스(비즈니스 로직)에서 방향성을 찾는 경우가 많다.__
      + 게시판의 경우, 게시판 리스트 화면에서 작성자를 표시해야 경우가 많다. (Board -> User, Navigational Association)  
      + 반대로 특정 사용자의 글들을 가져와야 하는 경우는 경험상 흔치 않다.(필요하다면 User->Board 관계도 잡으면 된다.. 이 결정은 전적으로 개발하는 서비스에 달려있다.)  
      + 따라서 이 모델에서는 단방향(Unidirection) 으로 결정한다.(Board -> User)  
   
   3. __다중성은 방향성이 결정나면 쉽게 결정 할 수 있다.__
      + ManyToOne 똔느 OneToMany 또는 OneToOne를 결정하는 것은 쉽다. 참조를 해야하는 엔티티 기준으로 결정하면 된다.  (
        A -> B 인 경우 A기준이다. 따라서, Board -> User 단방향에서는 Board 기준이다.  
      + 데이터 모델링 Relation에서, Board가 Many, User가 One 이므로 **ManyToOne** 이다.  
   
   4. __단방향에서는 연관관계의 주인을 따지지 않아도 된다. (연관관계가 하나밖에 없기 때문에 관계의 주인이 없다. 2개가 되는 양방향에서 따져줘야 한다.)__


#### 2) Entity Class: User, Board
  1. __User.java 엔티티 매핑 참고__
  2. __Board.java 엔티티 매핑 참고__
  3. __연관관계 매핑__
     
     Board.java
     
     ```
       .
       .
       .
       @ManyToOne
       @JoinColumn( name = "user_no" )
       private User user;
       .
       .
       .    
     ```
     + 데이터베이스 테이블에서는 N:1 관계에서는 N쪽에 외래키를 둔다.
     + 엔티티 매핑에서도 마찬가지다. 다중성을 결정하는 어노테이션과 외래키(조인칼럼)을 @JoinColumn 어노테이션으로 지정해주면 끝이다.


### 02. SpringBoot Test Case

#### 1) 테스트 환경
  1. __Java SE 1.8__  
  2. __Spring Boot Starter Web 2.1.8.RELEASE (Spring Core, Context, Web ... etc 5.19.RELEASE)__   
  3. __Spring Boot Starter Data JPA 2.1.8.RELEASE (Spring Data JPA 2.1.10.RELEASE)__
  4. __Hibernate 5.4.4.Final__ 
  5. __QueryDsl JPA(com.querydsl) 4.2.1__
  6. __QueryDsl APT(com.mysema.querydsl) 3.7.4__ 
  7. __H2 Database 1.4.197__  
  8. __JUnit 4.12__
  9. __Spring Boot Starter Test 2.1.8.RELEASE (Spring Test 5.1.9.RELEASE)__   
 10. __Gradle 5.4__   


#### 2) Jpql UserRepository Test : JPQL 기반 Repository
  1. __JpqlConfig.java__
  
     + jpa03-model01 내용과 동일 
     
  2. __JpqlUserRepository.java__
    
     + JPQL 기반으로 작성
     + 저장을 위한 객체 영속화 
     + TypedQuery 객체 및 Update 구현시 TypedQuery 대신 Query 객체 사용하는 방법
     + DTO 객체를 활용한 Projection 방법
     + 집합함수: 통계 쿼리 스칼라 타입 조회

  3. __JpqlUserRepositoryTest.java__
    
     + test01Save
        - JpqlUserRepository.save(User)
        - 객체 영속화

     + test02FindAllById
       - JpqlUserRepository.findById(id)
       - 영속화 객체 조회 이기 때문에 영속화 컨텍스트에서 찾고 없으면 select 쿼리를 통해 DB에서 가져온다.(1차 캐시)
         
     + test03UpdatePersisted
       - JpqlUserRepository.update1(User)
       - 영속화 객체를 사용한 수정(업데이트)
       - 성능 이슈: update 이전에 select 쿼리가 실행되는 문제점이 있다.(쿼리 로그 확인해 볼 것) 
         
     + test04FindByEmailAndPassword
       - JpqlUserRepository.findByEmailAndPassword(Long, String)
       - 영속화 객체를 사용하지 않는다. 따라서 무조건 select 쿼리를 통해 DB에서 가져온다.
       - TypedQuery 객체 사용
       - 주로 사용자 인증(로그인)에 사용하게 될 메소드이다.
       - 따라서, 모든 정보를 담은 User 엔티티 객체륿 반환할 이유가 없다. UserDto를 사용한 Projection 구현  
       - 이름 기반 파라미터 바인딩

     + test05Update2
       - JpqlUserRepository.update2(User)
       - 영속화 객체를 사용하지 않고 JPQL 기반의 업데이트
       - JpqlUserRepository.update1(User)에 비하여 select 쿼리가 실행되지 않는 장점이 있다(쿼리 로그 확인해 볼 것)
       - 반환할 타입이 없는 경우에는 TypedQuery 대신 Query객체를 사용하여 JPQL를 실행시킨다.
       - 이름 기반의 파라미터 바인딩을 사용하는 것은 TypedQuery와 다르지 않다.
    
     + JpqlUserRepository.count() 메소드
       - JPQL에서 통계함수 사용
   

#### 3) Jpql BoardRepository Test : JPQL 기반 Repository

  1. __JpqlConfig.java__
  
     + jpa03-model01 내용과 동일 
     
  2. __JpqlBoardRepository.java__
    
     + 저장(insert)을 위한 객체 영속화
     + 삭제(delete) 지원
     + 수정(update) 지원
     + 조회(select) - fetch one 하는 두가지 방법
     + 조회(select) - fetch list, inner join list, **fetch join** list, 
     + 조회(select) - paging 및 like 검색
     + JPQL 파라미터 인덱스 바인딩 & 이름 바인딩
     + 집합함수: 통계 쿼리 스칼라 타입 조회
     + **@ManyToOne Fetch.EAGER 기본 모드 이해**
     + fetch one에서 join 문제 이해하기

  3. __JpqlBoardRepositoryTest.java__
    
     + test01Save
        - JpqlBoardRepository.save(Board)
        - 객체 영속화

     + test02FindById1
       - JpqlBoardRepository.findById1(id)
       - Eager Fetch(@ManyToOne 기본 Fetch Mode)는 Proxy 객체 타입을 리턴하지 않는다. Lazy Fetch는 Proxy 객체를 리턴한다.(실제 User 객체가 아니다)
       - 영속화 객체 조회 에서는 Left Outer Join 으로 User 정보를 가져온다. (로그 확인 할 것)
         
     + test03FindById2
       - JpqlBoardRepository.findById2(id)
       - Eager Fetch(@ManyToOne 기본 Fetch Mode)는 Proxy 객체 타입을 리턴하지 않는다. Lazy Fetch는 Proxy 객체를 리턴한다.(실제 User 객체가 아니다)
       - JPQL를 사용하면 User 정보를 가져오기 위해 Join 대신 Select 쿼리를 2번 실행한다. (로그 확인 할 것)  
         
     + test04FindAll1
       - JpqlBoardRepository.findAll1()
       - Board 엔티티만 지정하면 join으로 한 번에 User 정보까지 가져오지 않는다는 것이다. 
       - 기본이 EAGER이기 때문에 각각의 Board가 참조하고 있는 User의 정보를 얻어오기 위해 Select 쿼리가 개별적으로 실행된다.(User가 영속객체이기 때문에 1차 캐시됨)
       - **성능이슈**: 대용량 게시판에선 문제가 될 수 있다.
       - 해결 방법은 **Inner Join을 직접 사용하는 방법**과 **Fetch Join** 을 사용하는 것이다.
        
     + test05FindAll2
       - JpqlBoardRepository.findAll2()
       - Inner Join을 사용한다.
       - 쿼리상으로 Join이 걸리지만 select에 User를 올릴 수 없기 떄문에 Inner Join도 User의 정보를 얻어오기 위해 Select 쿼리가 개별적으로 실행되는 문제가 있다.
 
     + test06FindAll3
       - JpqlBoardRepository.findAll3()
       - Fecth Join을 사용한다.
       - Fecth Join은 Inner Join의 성능 문제를 해결 할 수 있다.
       - 실제 실행되는 쿼리를 보면 select절에 user table의 컬럼이 프로젝션 된다.

     + test07FindAll4
       - JpqlBoardRepository.findAll4(page)
       - Fetch Join 적용
       - Paging 적용(TypedQuery 의 setFirstResult(), setMaxResults() 메소드)

     + test08FindAll5
       - JpqlBoardRepository.findAll5(keyword, page)
       - Fetch Join 적용
       - Paging 적용(TypedQuery 의 setFirstResult(), setMaxResults() 메소드)
       - like 검색 적용

     + test09Update1
       - JpqlBoardRepository.update1(Board)
       - 영속객체를 사용한다.
       - 선별적 컬럼 업데이트이지만 영속객체를 사용하기 때문에 전체 속성이 업데이트 된다.
       - select와 update 쿼리가 2개 실행된다.
       
     + test10Update2
       - JpqlBoardRepository.update2(Board)
       - 반환할 타입이 없는 경우에는 TypedQuery 대신 Query객체를 사용하여 JPQL를 실행시킨다.
       - JPQL기반 update 쿼리만 실행된다.
       - 선별적 컬럼 업데이트가 가능하다.
     
     + test11Delete1
       - JpqlBoardRepository.delete1(no)
       - 영속객체를 사용한다.
       - select와 delete 쿼리가 2개 실행된다.
      
     + test12Delete2
       - JpqlBoardRepository.delete2(no)
       - 반환할 타입이 없는 경우에는 TypedQuery 대신 Query객체를 사용하여 JPQL를 실행시킨다.
       - JPQL 기반 delete 쿼리만 실행된다.

     + test13Delete3
       - JpqlBoardRepository.delete3(boardNo, userNo)
       - 반환할 타입이 없는 경우에는 TypedQuery 대신 Query객체를 사용하여 JPQL를 실행시킨다.
       - JPQL 기반 delete 쿼리만 실행된다.
       - 게시판 삭제 비즈니스 로직에 맞게 작성된 메소드이다.
    
     + JpqlUserRepository.count() 메소드
       - JPQL에서 통계함수 사용


#### 4) QueryDSL UserRepository Test : QueryDSL 기반 Repository

  1. __JpqlConfig.java__
  
     + jpa03-model01 내용과 동일 
     
  2. __QueryDslUserRepository.java__
    
     + QueryDSL 기반으로 작성
     + 저장을 위한 객체 영속화
     + 다양한 쿼리함수 사용법 
     + QueryDSL DTO 객체를 활용한 Projection 방법
     + QueryDSL 통계 쿼리

  3. __QueryDslUserRepository Test.java__
    
     + test01Save
        - QueryDslUserRepository.save(User)
        - 객체 영속화

     + test02FindAllById
       - QueryDslUserRepository.findById(id)
       - 영속화 객체 조회 이기 때문에 영속화 컨텍스트에서 찾고 없으면 select 쿼리를 통해 DB에서 가져온다.(1차 캐시)
         
     + test03UpdatePersisted
       - QueryDslUserRepository.update1(User)
       - 영속화 객체를 사용한 수정(업데이트)
       - 성능 이슈: update 이전에 select 쿼리가 실행되는 문제점이 있다.(쿼리 로그 확인해 볼 것) 
         
     + test04FindAllById2
       - QueryDslUserRepository.findById2(id)
       - 영속화 객체를 사용하지 않는다. 따라서 무조건 select 쿼리를 통해 DB에서 가져온다.
       - 하지만, 반환되는 Entity 객체는 영속객체다.
       - 쿼리함수 from(), where(), fetchOne()

     + test05Update2
       - QueryDslUserRepository.update2(User)
       - 영속화 객체를 사용하지 않고 JPQL 기반의 업데이트
       - QueryDslUserRepository.update1(User)에 비하여 select 쿼리가 실행되지 않는 장점이 있다(쿼리 로그 확인해 볼 것)
       - 쿼리함수 update(), where(), set(), execute() 
         
     + test05FindByEmailAndPassword
       - QueryDslUserRepository.findByEmailAndPassword(Long, String)
       - 영속화 객체를 사용하지 않는다. 따라서 무조건 select 쿼리를 통해 DB에서 가져온다.
       - 주로 사용자 인증(로그인)에 사용하게 될 메소드이다.
       - QueryDSL에서 UserDto를 사용한 Projection 구현 (Projections.constructor) 
       - 쿼리함수 select(), from(), where(), fetchOne()
    
     + QueryDslUserRepository.count() 메소드
       - QueryDSL의 fetchCount() 사용방법

#### 5) QueryDSL BoardRepository Test : QueryDSL 기반 Repository
  1. __JpqlConfig.java__
  
     + jpa03-model01 내용과 동일 
     
  2. __QueryDslBoardRepository.java__
    
     + QueryDSL 기반으로 작성
     + 저장을 위한 객체 영속화
     + 다양한 쿼리함수 사용법
     + JPQL Repository 메소드와 완전 동일(구현 내용만 다름) 
     + JPQL Repository 메소드와 구현 및 실행 시 쿼리 로그 1:1 비교해 볼 것
     + QueryDSL Fetch Join
     + QueryDSL Like 검색  
     + QueryDSL 통계 쿼리

  3. __QueryDslBoardRepositoryTest.java__
     + JpqlBoardRepositoryTest 와 완전 동일
     
     + JpqlBoardRepositoryTest 쿼리로그 꼭 비교 분석할 것 (완전 일치)
     
     + test01Save
        - QueryDslBoardRepository.save(Board)
        - 객체 영속화

     + test02FindById1
       - QueryDslBoardRepository.findById1(id)
       - Eager Fetch(@ManyToOne 기본 Fetch Mode)는 Proxy 객체 타입을 리턴하지 않는다. Lazy Fetch는 Proxy 객체를 리턴한다.(실제 User 객체가 아니다)
       - 영속화 객체 조회 에서는 Left Outer Join 으로 User 정보를 가져온다. (로그 확인 할 것)
         
     + test03FindById2
       - QueryDslBoardRepository.findById2(id)
       - Eager Fetch(@ManyToOne 기본 Fetch Mode)는 Proxy 객체 타입을 리턴하지 않는다. Lazy Fetch는 Proxy 객체를 리턴한다.(실제 User 객체가 아니다)
       - JPQL를 사용하면 User 정보를 가져오기 위해 Join 대신 Select 쿼리를 2번 실행한다. (로그 확인 할 것)
       - from(), where(), fetchOne() 함수 사용법  
         
     + test04FindAll1
       - QueryDslBoardRepository.findAll1()
       - Board 엔티티만 지정하면 join으로 한 번에 User 정보까지 가져오지 않는다는 것이다. 
       - 기본이 EAGER이기 때문에 각각의 Board가 참조하고 있는 User의 정보를 얻어오기 위해 Select 쿼리가 개별적으로 실행된다.(User가 영속객체이기 때문에 1차 캐시됨)
       - **성능이슈**: 대용량 게시판에선 문제가 될 수 있다.
       - 해결 방법은 **Inner Join을 직접 사용하는 방법**과 **Fetch Join** 을 사용하는 것이다.
       - from(), orderBy(), Q클래스 desc(), fetch() 함수 사용법
        
     + test05FindAll2
       - QueryDslBoardRepository.findAll2()
       - Inner Join을 사용한다.
       - 쿼리상으로 Join이 걸리지만 select에 User를 올릴 수 없기 떄문에 Inner Join도 User의 정보를 얻어오기 위해 Select 쿼리가 개별적으로 실행되는 문제가 있다.
       - from(), innerJoin(), orderBy(), fetch() 함수 사용법
       
     + test06FindAll3
       - QueryDslBoardRepository.findAll3()
       - Fecth Join을 사용한다.
       - Fecth Join은 Inner Join의 성능 문제를 해결 할 수 있다.
       - 실제 실행되는 쿼리를 보면 select절에 user table의 컬럼이 프로젝션 된다.
       - from(), innerJoin(), fetchJoin(), orderBy(), fetch() 함수 사용법

     + test07FindAll4
       - QueryDslBoardRepository.findAll4(page)
       - Fetch Join 적용
       - Paging 적용(offset(), limit() 함수)
       - from(), innerJoin(), fetchJoin(), orderBy(), offset(), limit(), fetch() 함수 사용법

     + test08FindAll5
       - QueryDslBoardRepository.findAll5(keyword, page)
       - Fetch Join 적용
       - Paging 적용(offset(), limit() 함수)
       - like 검색 적용(Q클래스 contains() 메소드 사용)
       - from(), innerJoin(), fetchJoin(), where(), orderBy(), offset(), limit(), fetch() 함수 사용법 

     + test09Update1
       - QueryDslBoardRepository.update1(Board)
       - 영속객체를 사용한다.
       - 선별적 컬럼 업데이트이지만 영속객체를 사용하기 때문에 전체 속성이 업데이트 된다.
       - select와 update 쿼리가 2개 실행된다.
       
     + test10Update2
       - QueryDslBoardRepository.update2(Board)
       - 반환할 타입이 없는 경우에는 TypedQuery 대신 Query객체를 사용하여 JPQL를 실행시킨다.
       - update 쿼리만 실행된다.
       - 선별적 컬럼 업데이트가 가능하다.
       - update(), set(), where(), execute() 함수 사용법 
     
     + test11Delete1
       - JpqlBoardRepository.delete1(no)
       - 영속객체를 사용한다.
       - select와 delete 쿼리가 2개 실행된다.
      
     + test12Delete2
       - JpqlBoardRepository.delete2(no)
       - 반환할 타입이 없는 경우에는 TypedQuery 대신 Query객체를 사용하여 JPQL를 실행시킨다.
       - delete 쿼리만 실행된다.
       - delete(), where(), execute() 함수 사용법

     + test13Delete3
       - JpqlBoardRepository.delete3(boardNo, userNo)
       - 반환할 타입이 없는 경우에는 TypedQuery 대신 Query객체를 사용하여 JPQL를 실행시킨다.
       - delete 쿼리만 실행된다.
       - 게시판 삭제 비즈니스 로직에 맞게 작성된 메소드이다.
       - delete(), where(), execute() 함수 사용법
    
     + JpqlUserRepository.count() 메소드
       - JPQL에서 통계함수 사용     

#### 6) Spring Data JPA UserRepository Test : Spring Data JPA 기반 Repository
  1. __JpaConfig.java__
  
     + jpa03-model01 내용과 동일
      
  2. __JpaUserRepository.java__
    
     + 기본 메소드
       - save(User)         
       - findById(id)
       
     + 쿼리 메소드
       - findByEmailAndPassword(email, password)
       
     + @Query 어노테이션을 사용한 메소드에 쿼리 정의
       - findById2(id)
       - update(User)
       
  3. __JpaUserRepositoryTest.java__

     + test01Save()
       - CrudRepository.save(S)
       
     + test02FindById   
       - CrudRepository.findById()
     
     + test03UpdatePersisted()  
       - CrudRepository.findById()
       - 영속객체를 사용한 업데이트
       - 성능이슈: update 쿼리 이전에 select 쿼리 실행

     + test04FindById2()  
       - JpaUserRepository.findById2(id)
       - @Query 어노테이션을 사용한 메소드 쿼리(JPQL) 정의
       - JPQL Projection
       
     + test05Update
       - JpaUserRepository.update(...)
       - @Query 어노테이션을 사용한 메소드 쿼리(JPQL) 정의
       - JPQL 이름 바인딩
       - 이름 바인딩은 객체 이름 경로를 사용할 수 없기 때문에 메소드의 파라미터가 많다.
       - 이를 해결하기 위해서는 QueryDSL과 통합해야 함(jpa03-model03의 JpaUserRepository 참고) 
       
     + test06FindByEmailAndPassword()  
       - JpaUserRepository.findByEmailAndPassword(email, password)
       - JpaUserRepository **쿼리메소드 예시**
       - 프로젝션 하지 않음
       - 프로젝션을 하기 위해서는 앞의 @Query 메소드 쿼리 정의 또는 QueryDSL 통합을 해야 한다.(jpa03-model03의 JpaUserRepository 참고)


#### 7) Spring Data JPA BoardRepository Test : Spring Data JPA 기반 Repository
