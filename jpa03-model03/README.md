## Model03 : 다대일(ManyToOne) - 양방향


### 01. Domain

#### 1) 테이블 연관관계 VS 객체 연관관계

   <img src="http://assets.kickscar.me:8080/markdown/jpa-practices/33001.png" width="500px" />
   <br>
   <img src="http://assets.kickscar.me:8080/markdown/jpa-practices/33002.png" width="500px" />
   <br>
   
   1. __보통은 서비스에서 방향성을 찾는 경우가 많다.__
      + 쇼핑몰 관리자 페이지에서 주문조회에서 주문에 사용자 정보가 나와야 한다. (Order -> User, Navigational Association)  
      + 회원의 경우, 마이페이지에서 자신의 주문리스트를 확인해 보는 서비스의 비지니스 로직이 필요하다. (Order <- User, Navigational Association) 
      + 쇼핑몰의 주문<->회원 관계매핑은 양방향(Bidirection) 이다.  
   
   2. __양방향에서는 연관관계의 주인을 따져야 한다.__
      + 외래키가 있는 Order 엔티티의 user 필드가 이 관계의 주인이 된다.
      + 이 말은 외래키 세팅은 Order 엔티티의 user 필드를 세팅할때만 변한다는 말이다.(그러니 관계의 주인인 거지!)
      + 반대편의 List<Order> 에 아무리 값을 설정해도 무시된다. (여긴 참조만 하는 것이지 관계설정에 아무런 영향을 미치지 못한다. 그래서 주인이 아니다.)
      + 사실상, 외래키 설정을 하는 ManyToOne 에서 관계 설정은 끝난 것이다.
      + 양방향으로 OneToMany를 하나 더 설정하는 것은 서비스 비즈니스 로직의 필요에 따라 객체 탐색의 편의성 때문에 하는 것이다.

   3. __다중성은 방향성이 결정나면 쉽게 결정 할 수 있다.__
      + 양방향에서는 ManyToOne, OneToMany가 다 존재하지만 관계의 주인이 되는 필드를 설정하는 ManyToOne으로 다중성을 잡는 것이 자연스럽다. 



#### 2) Entity Class: User, Order

   1. __User 엔티티 매핑 참고__
   2. __Order 엔티티 매핑 참고__
   3. __연관관계 매핑__
   
      + ManyToOne(User 엔티티)
        ```
             .
             .
        @ManyToOne(fetch = FetchType.EAGER)   
        @JoinColumn( name = "user_no" )
        private User user;      
             .
             .
        ```
        - ManytoOne, OneToOne에서 Default Fetch Mode는 EAGER 이다.  
      
      + OneToMany(Order 엔티티)  
        ```
             .
             .
	    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	    private List<Order> orders = new ArrayList<Order>();
             .
             .
        ```
        - mappedBy = "user" 주인이 누구임을 설정하는 부분이다. 반대편 엔티티의 user 필드이다.
        - OneToMany 에서는 Default Fetch Mode는 LAZY 이다.
      
      + 객체 관계 설정에 주의 할점
        - 영속성과 상관 없이 순수 객체들과의 관계도 고려한 엔티티 클래스 작성을 해야 한다.
        - 양방향 관계이기 때문에 순수 객체에서도 양방향 관계를 맺어주는 것이 좋다.
        - 관계를 맺는 주인 필드가 세팅되는 Order.setUser() 코드에서 양방향 관계를 맺는 안전한 코드 예시이다.
        
          ```
            public void setUser(User user) {
                this.user = user;

                if(!user.getOrders().contains(this)) {
                    user.getOrders().add(this);
                }
            }
                   
          ``` 
          
        - toString() 오버라이딩 하는 것도 주의해야 한다.
        
          ```
          public String toString() {
             return "Order{" +
                "no=" + no +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", totalPrice=" + totalPrice +
                ", regDate=" + regDate +
                // 무한루프조심!!!
                // ", user=" + user +
                '}';
          }         
          ``` 


### 02. SpringBoot Test Case

#### 0) 요약: 다루는 기술적 내용

  1. QueryDSL 통합하는 Spring Data JPA 기반의 레포지토리 작성방법
  2. ManyToOne 양방향(Bidirection) 매핑에서 OneToMany 방향에서는 Collection(List)가 연관필드이기 때문에 Join에서 발생할 수 있는 문제점들...
     - OneToMany의 켈렉션 조인(inner join, outer join, fetch join)의 문제점 이해 및 해결방법 이해
     - OneToMany의 Default Fetch Mode인 Lazy Loading 때문에 발생하는 N+1 문제 이해 및 해결방법 이해  
     - Fetch Join + 페이징 API 에서 발생하는 메모리 페이징 이해 및 해결방법 이해 

#### 1) 테스트 환경

  1. __Java SE 1.8__  
  2. __Spring Boot Starter Web 2.1.8.RELEASE (Spring Core, Context, Web ... etc 5.19.RELEASE)__   
  3. __Spring Boot Starter Data JPA 2.1.8.RELEASE (Spring Data JPA 2.1.10.RELEASE)__
  4. __Hibernate 5.4.4.Final__ 
  5. __QueryDsl JPA 4.2.1__
  6. __QueryDsl APT 3.7.4__ 
  7. __H2 Database 1.4.197__  
  8. __JUnit 4.12__
  9. __Spring Boot Starter Test 2.1.8.RELEASE (Spring Test 5.1.9.RELEASE)__   
 10. __Gradle 5.4__    


#### 2) Spring Data JPA UserRepository Test : Spring Data JPA 기반 Repository

  1. __JpaUserRepository.java__
     + User 엔티티(user 테이블)의 CRUD관련 메소드를 사용할 수 있는 인터 페이스다.
     + 기본메소드
       - 상속 Hierachy:   
         JpaUserRepository -> JpaRepository -> PagingAndSortingRepository -> CrudRepository -> Repository
       - 상속을 통해 상위 인터페이스 JpaRepository, PagingAndSortingRepository, CrudRepositor 들의 메소드들을 별다른 구현없이 사용 가능하다.
     + 쿼리메소드
       - 메소드 이름기반으로 자동으로 JPQL을 생성하는 메소드가 구현되는데 이를 쿼리 메소드라 한다.
       - findByEmailAndPassword(String, String) 메소드가 그 예이다.

  2. __JpaUserQryDslRepository.java__
     + 기본메소드나 쿼리메소드가 성능이슈가 발생하거나 자동으로 제공받지 못하면 직접 메소드를 직접 만들어 JPQL를 실행해야 한다.
     + @Query에 JPQL를 직접 넣는 방법이 있으나 문자열 기반이고 기능상 제약이 많다. QueryDSL을 통합하는 방식을 많이 선호한다.
     + 이를 위해, 구현해야하는 QueryDSL기반 메소드를 정의해 놓은 인터페이스다.
     + 그리고 애플리케이션에서 직접 사용해야 하는 인터페이스 JpaUserRepository가 이를 상속한다.
     + 몇가지 메소드의 예를 보면,
       - findById2(no)는 기본메소드 findById(no)의 성능문제와 비즈니스 요구사항 때문에 QueryDSL로 직접 구현해야 하는 메소드이다.
       - update(user)는 영속객체를 사용한 update의 성능문제 때문에 jpa03-model02의 JpaUserRepository에 @Query 어노테이션을 사용해서 JPQL를 직접 사용했는데 메소드 파라미터에 문제가 있어 QueryDSL로 직접 구현해야 하는 메소드이다. 

  3. __JpaUserQryDslRepositoryImp.java__
     + JpaUserQryDslRepository 인터페이스의 메소드를 QueryDSL로 구현한다.
     + 정리하면,
       - 기본 메소드, 쿼리 메소드의 구현은 Spring Data JPA가 해주는 것이고
       - JpaUserQryDslRepository 인터페이스 구현은 직접 해야 하는 것이다.
       - Spring Data JPA는 JpaUserQryDslRepository 인터페이스의 구현체를 스캔해야하기 때문에 네이밍 규칙이 있다. 인터페이스이름에 **"Impl"** 을 붙힌다.
    
       <img src="http://assets.kickscar.me:8080/markdown/jpa-practices/33003.png" width="500px" />
       <br>
       
  4. __JpaUserRepositoryTest.java__
      
      + test01Save()
        - 기본 메소드 CrudRepository.save(S)
        
      + test02Update
        - QueryDSL 기반 메소드 직접 구현하고 JpaUserRepository 인터페이스와 통합
        - JpaQryDslUserRepositoryImp.update(user)
    
      + test03OneToManyCollectionJoinProblem
        - OneToMany Collection Join(inner, outer, fetch)에서 발생하는 문제에 대한 테스트 이다.
        - 테스트 대상 메소드는 JpaUserQryDslRepositoryImpl.findAllCollectionJoinProblem() 메소드다.
        - 기본메소드 findAll()의 Collection Join를 추가한 QueryDSL로 작성된 user를 전부다 찾아주는 메소드다.
        - 테스트 코드 assert 에도 있지만, user 카운트가 order 카운트와 같은 문제가 있다.
 
          <img src="http://assets.kickscar.me:8080/markdown/jpa-practices/33004.png" width="500px" />
          <br>       
        
        - User와 Order가 조인되었기 때문에 연결된 Order의 개수만큼 User도 나오는 것이 당연하다.
        - 이는 따지고 보면 문제가 아니다. 관계형데이터베이스와 객체지향프로그래밍 차이에서 발생하는 문제점이라 볼 수 있다.
    
      + test04OCollectionJoinProblemSolved
        - Collection Join 문제 해결방법은 의외로 간단하다. distinct를 사용해 관계형데이터베이스에서 문제를 해결한다.
        - JpaUserQryDslRepositoryImpl.findAllCollectionJoinProblemSolved() 메소드를 보면 QueryDSL의 selectDistinct() 함수로 해결한다.
        - OneToMany Collection 조인 outer, inner, fetch join 모두 해당되는 내용으로 반드시 selectDistinct() 함수를 사용해야 한다.
        
      + test05NplusOneProblem
        - N+1 문제를 테스트 한다.
        - 테스트 코드는 총 Order 카운트를 먼저 가져온 다음, 전체 User List에서 개별 User 객체의 Order List의 사이즈를 모두 더해 같은 지 보는 것이다.
        - 당연히 같을 것이다. 테스트 통과 조건은 실행된 쿼리수와 전체 User를 가져오기 위해 실행된 쿼리(1)과 Lazy 때문에 각 User 별로 Order List를 가져오기 위해 실행된 쿼리 수(N)과 합이 같은 것이다.
        - 각 User 별로 Order List를 가져오기 위해 쿼리가 실행됐을 거라 추측할 수 있는 근거는 Lazy 때문에 User 엔티티 객체의 List<Order> Orders는 Proxy 객체로 실제로 DB에서 가져온 Order가 담긴 List가 아니다.
        - Proxy 객체이면 List의 사이즈를 가져올 때 쿼리가 들어 갈 것이기 때문에 쿼리 카운팅을 할 수 있다.
        
          ```
            if(!em.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(orders)){
                qryCount++;
            }
                    
          ```     
        - PersistenceUnitUtil.isLoaded(Entity) 반환이 false이면 초기화 되지 않은 Proxy객체로 지연로딩 중인 것이다.
        - 테스트 결과는 N+1번으로 쿼리가 실행된 것을 확인할 수 있다. (실제 쿼리 로그를 세어 보아도 확인된다.)
      
      + test06NplusOneProblemNotSolvedYet
        - Collection Join 문제를 해결한 findAllCollectionJoinProblemSolved() 메소드로 전체 User List를 가져와서 N+1 문제를 검증하는 테스트 코드를 돌려본다.
        - User List의 User의 Order List의 Order가 Proxy라면 test05NplusOneProblem() 태스트와 마찬가지로 N + 1번 쿼리가 수행됐을 것이다.
        - N + 1번 나오기 때문에 아직 문제가 해결되지 못했다.
        - findAllCollectionJoinProblemSolved() 는 left join을 사용한다.
        
      + test07NplusOneProblemSolved
        - N + 1 문제를 해결하기 위해  JpaUserQryDslRepositoryImpl.findAllCollectionJoinAndNplusOneProblemSolved() 메소드를 구현했다.
        - 이름은 길지만 innerJoin() + fetchJoin() 으로 작성된 QueryDSL 컬렉션 페치 조인한다.
        - 테스트 통과 조건인 1번 쿼리수가 나왔다.
        - OneToMany에서 객체그래프를 통해 컬렉션 접근 시, 발생하는 조인문제와 N+1 Lazy 로딩 문제를 해결하기 위해서는 selectDistinct(), fecthJoin()을 사용하면 된다.
        - Lazy로 객체 그래프를 통해 컬렉션에 접근하는 것이 반드시 좋지 못한 것은 아니다. 상황에 따라 선택해야 한다. 
