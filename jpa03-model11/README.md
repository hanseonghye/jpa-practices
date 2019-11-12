## Model11 : 혼합모델(ManyToMany 문제해결, 연결엔티티, 복합키(PK), 식별관계, @EmbeddedId)


### 1. Domain

#### 1-1. 다대다 매핑의 문제 해결, 조인테이블 대신 연결 엔티티 사용

<img src="http://assets.kickscar.me:8080/markdown/jpa-practices/31101.png" width="600px" />
<br>

<img src="http://assets.kickscar.me:8080/markdown/jpa-practices/31102.png" width="600px" />
<br>
        
1. __보통은 서비스에서 방향성을 찾는 경우가 많다.__
    1) 온라인북몰에서 회원과 책과의 관계이다.
    2) 회원은 온라인북몰에서 관심있는 책을 저장(장바구니)할 수 있는 데 이는 User와 Book 사이에 ManyToMany 로 생각할 수 있다. 
    3) 앞의 Model9/10의 관계매핑을 할 경우 문제점이 몇가지 있다. 담을 때 수량과 같은 새로운 칼럼의 추가 요구사항이 생긴다는 것이다.
    4) 그리고 앞의 Model9/10 ManyToMany는 조인테이블을 사용해서 편하게 매핑이 가능한 장점은 있지만 몇가지 문제점을 가지고 있기 때문에 QueryDSL과 통합해서 따로 JPQL을 구현했었다.
    5) 보통 실무에서는 ManyToMany 조인테이블을 사용하지 않고 연결 엔티티를 추가하여 세개의 엔티티로 매핑하는 복합모델을 사용하게 된다.
    6) User <-> CartItem (양방향) 과 CartItem -> Book (단방향) 복합모델로 ManyToMany 관계를 해결한다.

2. __다중성은 방향성이 결정나면 쉽게 결정 할 수 있다.__
    1) User는 다수의 책을 저장할 수 있기 때문에 User(1) <-> CartItem(\*) 관계다.
    2) OneToMany 이지만 ManyToOne을 많이 선호하기 때문에 CartItem <-> User ManyToOne 양방향을 결정한다.
    2) 책역시 다수의 User에 의해 카트에 잠길 수 있기 때문에 CartItem(\*) -> Book(1) 관계다. 
    3) CartItem -> Book ManyToOne 단방향으로 결정한다. 
       
3. __복합키 식별관계__
    1) CartItem은 2개의 관계에서 연관 필드를 두개 가지고 있다. 그리고 2개 관계 모두에서 N 다중성을 가지고 있기 때문에 외래키 두 개를 가지게 된다.
    2) 새로운 Id(PK)를 만들 수 있지만, Model11/12에서는 이 외래키 2개을 하나로 묶어 PK로 사용하는 복합키 식별관계로 매핑한다.
    3) JPA에서는 복합키를 @EmebeddedID, @IdClass 이렇게 두 가지 방식을 할 수 있다.
    4) Model11에서는 @EmebeddedID 방식으로 매핑하고 테스트 한다.  
    
#### 1-2. Entity Class: User, Book, CartItem
1. __User 엔티티 매핑 참고__
2. __Book 엔티티 매핑 참고__
3. __CartItem 엔티티 매핑 참고__
4. __연관관계 매핑__
    1) 복합키를 지원하기 위해 JPA에서는 복합키를 Id(PK)로 잡기 때문에 복합키가 되는 1개 이상의 필드를 가지고 있는 식별자(Id)클래스를 따로 작성해주어야 한다.
    2) CartItemId가 식별자 클래스이고 CartItem 엔티티에서는 CartItemId가 식별자 클래스타입의 Id필드를 두어야 한다. 
        ```
            @Embeddable
            public class CartItemId implements Serializable {
                private Long userNo;
                private Long bookNo;
                .
                .
                getter/setter
                .
                .
                @Override
                public boolean equals(Object o) {
                    .
                    .
                }
            
                @Override
                public int hashCode() {
                    .
                    .
                }                
       
        ```
        + @EmbeddedId 어노테이션 적용방식의 복합키 구현을 위해 식별자 클래스는 우선 @Embeddable를 붙혀주어야 한다.
        + 식별자 클래스는 인터페이스 Serializable를 구현해야 한다.
        + 실제 컬럼 매핑은 식별자 클래스를 사용하는 연결엔티티에서 하지만 연결엔티티의 필드와 매핑할 수 있는 필드가 있어야 한다.(userNo, bookNo)
        + 기본 생성자로 생성이 가능해야 한다.
        + equals와 hashCode를 반드시 오버라이딩 해야 한다.
    
    3) 식별자 클래스 CartItemId를 Id(PK)로 사용하여 User와 Book과 연관 매핑을 하는 CartIetm은 다음과 같다.
        ```
            @Entity
            @Table(name = "cartitem")
            public class CartItem {
       
                @EmbeddedId
                public CartItemId catrtItemId = new CartItemId();
            
                @MapsId("bookNo")
                @ManyToOne
                @JoinColumn(name="book_no")
                private Book book;
            
                @MapsId("userNo")
                @ManyToOne
                @JoinColumn(name="user_no")
                private User user;
            
                @Column(name = "amount", nullable = false)
                private Integer amount;
                .
                .
                .
            }       
        ```
        + @EmbeddedId를 사용하여 Id(PK) 필드를 지정한다. 필드명은 제약은 없지만 반드시 객체 생성을 해주어야 한다.
        + CartItem 엔티티는 User 엔티티, Book 엔티티와 ManyToOne 관계를 맺기 때문에 관계주인 필드를 가진다.
        + book, user가 관계주인 필드이다. 따라서 @JoinColumn를 지정하였으며 @ManyToOne으로 다중성도 지정했다.
        + @MapsId를 지정하여 실제 컬럼값이 저장되는 객체의 필드와 매핑해야 한다. 코드상으로는  @EmbeddedId 달려 있는 catrtItemId의 userNo, bookNo가 된다.
      
    2) OneToMany(User 엔티티)
        
        ```
             .
             .
            @Id
            @Column(name = "no")
            @GeneratedValue( strategy = GenerationType.IDENTITY  )
            private Long no;
             .
             .
            @OneToMany(mappedBy = "user", fetch = FetchType.LAZY )
            private List<CartItem> cart = new ArrayList<CartItem>();
             .
             .       
        ```
        + OneToMany ManyToOne 양방향 반대편(OntToMany)에서는 관계주인이 아닌 엔티티는 mappedBy를 통해 관계의 주인이 아님을 선언한다.
        + 여기서는 CartItem.user 필드가 관계의 주인이다.
        + toMany 참조를 위해 컬렉션 매핑을 했다.
        
    3) Book 엔티티는 ManyToOne 단방향 반대편 엔티티기 때문에 연관관계 매핑은 별도로 없다.
    
    4) 생성 스키마
    
        ```
            Hibernate: 
                
                create table book (
                    no bigint not null auto_increment,
                    price integer not null,
                    title varchar(100) not null,
                    primary key (no)
                ) engine=InnoDB
            Hibernate: 
                
                create table cartitem (
                    book_no bigint not null,
                    user_no bigint not null,
                    amount integer not null,
                    primary key (book_no, user_no)
                ) engine=InnoDB
            Hibernate: 
                
                create table user (
                    no bigint not null auto_increment,
                    email varchar(200) not null,
                    name varchar(20) not null,
                    password varchar(128) not null,
                    primary key (no)
                ) engine=InnoDB
            Hibernate: 
                
                alter table cartitem 
                   add constraint FK8wjiwpl7f4veth9llay1tfiov 
                   foreign key (book_no) 
                   references book (no)
            Hibernate: 
                
                alter table cartitem 
                   add constraint FK2bocrqf9dlvblordkxan5ay1l 
                   foreign key (user_no) 
                   references user (no)     
       
        ```
        + ManyToMany 조인테이블과 큰 차이는 없다. 세 개의 엔티티에 대한 세 개의 테이블을 생성한다.
        + 연결 엔티티에 매핑된 cartitem 테이블을 보면 user, book 테이블을 참조하는 두개의 FK가 복합키로 PK로 세팅된 것을 볼 수 있다.
        + 그리고 추가 컬럼 amount가 있는 것을 볼 수 있다.

### 2. Repository 작성 & Testing

#### 2-1. 요약: 다루는 기술적 내용
1. 


#### 2-2. 테스트 환경
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


#### 2-3. JpaCartItemRepository Test : Spring Data JPA 기반 Repository
