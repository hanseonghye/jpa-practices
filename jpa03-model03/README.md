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

  1. Spring Data JPA 기반의 QueryDSL 통합 레포지토리만 작성했다.
  2. ManyToOne 양방향(Bidirection) 매핑에서 OneToMany 방향의 left join(컬렉션 조인), fetch join 등의 이슈등을 주로 다루고 해결방법을 제시한다.


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
     + 기본메소드나 쿼리메소드가 성능이슈가 발생하거나 자동 구현이 불가능한 경우 직접 메소드를 직접 만들어 JPQL를 실행해야 한다.
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
       


#### 3) QueryDSL Repository Test

#### 4) Spring Data JPA(JpaRepository) Repository Test

