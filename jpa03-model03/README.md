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
        - 양방향 관계이기 때문에 순수 객체에서도 양방향 관계를 맺어 준느 것이 좋다.
        - 관계를 맺는 주인 필드가 세팅되는 Order.setUser() 코드에서 양방향 관계를 맺는 안전한 코드 예시이다.
        
          ```
          public void setUser(User user) {
             if(this.user != null){
                this.user.getOrders().remove(this);
             }

             this.user = user;
             user.getOrders().add(this);
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

#### 2) JPQL Repository Test

#### 3) QueryDSL Repository Test

#### 4) Spring Data JPA(JpaRepository) Repository Test

