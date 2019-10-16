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
   
   2. __보통은 서비스에서 방향성을 찾는 경우가 많다.__
      + 게시판의 경우 게시글 리스트에서 글을 작성한 사용자를 표시해야 경우가 많다. (Board -> User, Navigational Association)  
      + 특정 사용자가 작성한 글들을 가져와야 하는 경우는 경험상 흔하지 않다.(필요하다면 양방향이 된다. 이 결정은 전적으로 개발하는 서비스에 달려있다.)  
      + 게시판의 경우, 여기서는 단방향(Unidirection) 이다.  
   
   3. __다중성은 방향성이 결정나면 쉽게 결정 할 수 있다.__
      + ManyToOne, OneToMany 를 결정하는 것은 쉽다. 참조를 해야 하는 엔티티 중심으로 결정하면 된다. ( A -> B 인 경우 A기준으로...)  
      + Board가 Many, User가 One 이므로...  
      + ManyToOne 이다.  
   
   4. __단방향에서는 연관관계의 주인을 따지지 않아도 된다. (연관관계가 하나밖에 없기 때문에 관계의 주인이 없다. 2개가 되는 양방향에서 따져줘야 한다.)__


#### 2) Entity Class: Guestbook
  1. __me.kickscar.practices.jpa03.model02.domain.User.java 엔티티 매핑 참고__
  2. __me.kickscar.practices.jpa03.model02.domain.Board.java 엔티티 매핑 참고__
  3. __연관관계 매핑__
  
     ```
       .
       .
     
       @ManyToOne
       @JoinColumn( name = "user_no" )
       private User user;
     
       .
       .    
     ```
     + 테이블에서는 1:N 관계에서는 N쪽에 외래키를 둔다.
     + 엔티티 매핑에서도 마찬가지다. 다중성을 결정하는 어노테이션과 외래키(조인칼럼)을 @JoinColumn 로 지정해주면 끝이다.


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

