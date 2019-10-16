## Model03 : 다대일(ManyToOne) - 양방향


### 01. Domain

#### 1) 테이블 연관관계 VS 객체 연관관계

   <img src="http://assets.kickscar.me:8080/markdown/jpa-practices/33001.png" width="500px" />
   <br>
   <img src="http://assets.kickscar.me:8080/markdown/jpa-practices/33002.png" width="500px" />
   <br>
   
   1. __보통은 서비스에서 방향성을 찾는 경우가 많다.__
      + 쇼핑몰 관리자 페이지에서 주문조회에서 주문에 사용자 정보가 나와야 한다. (Order -> User, Navigational Association)  
      + 회원의 경우, 마이페이지에서 자신의 주문리스트를 확인해 보는 서비스 페이지가 있다. (Order <- User, Navigational Association) 
      + 쇼핑몰의 주문<->회원 관계매핑은 양방향(Bidirection) 이다.  
   
   3. __다중성은 방향성이 결정나면 쉽게 결정 할 수 있다.__
      + ManyToOne, OneToMany 를 결정하는 것은 쉽다. 참조를 해야 하는 엔티티 중심으로 결정하면 된다. ( A -> B 인 경우 A기준으로...)  
      + 양방향에서는 ManyToOne, OneToMany가 다 존재하기 때문에 애매하지만 ManyToOne을 많이 선호한다. 
   
   4. __양방향에서는 연관관계의 주인을 따져야 한다. (외래키가 있는 Orders 테이블이 이 관계의 주인이 된다.)__


#### 2) Entity Class: Guestbook
  1. __me.kickscar.practices.jpa03.model03.domain.User.java 엔티티 매핑 참고__
  2. __me.kickscar.practices.jpa03.model03.domain.Order.java 엔티티 매핑 참고__
  3. __연관관계 매핑__


### 02. Test SpringBoot Application

#### 1) 개발환경
  1. __Java SE 1.8__  
  2. __Spring Boot 2.1.8.Release__   
  3. __Spring Data JPA 2.1.8.Release__   
  4. __Hibernate 5.4.4.Final__  
  5. __H2 Database 1.4.197__  
  6. __Gradle 5.4__   

#### 2) JPQL Repository Test

#### 3) QueryDSL Repository Test

#### 4) Spring Data JPA(JpaRepository) Repository Test

