## Model06 : 일대일(OneToOne) - 단방향(Unidirectional), 주테이블에 외래키

### 1. Domain

#### 1-1. 테이블 연관관계 VS 객체 연관관계

<img src="http://assets.kickscar.me:8080/markdown/jpa-practices/36001.png" width="500px" />
<br>

<img src="http://assets.kickscar.me:8080/markdown/jpa-practices/36002.png" width="500px" />
<br>
        
1. __보통은 서비스에서 방향성을 찾는 경우가 많다.__
    1) 블로그 한 개만을 개설할 수 있는 회원제 블로그 서비스
    2) 보통 블로그에 방문할 경우 blog.domain.com/userid 이런 식으로 접근하는 경우가 많다.
    3) userid로 블로그 정보(블로그 이름, 블로그 소개, 카테고리, 포스트등) 가져와야 한다. User -> Blog 연관관계로 User에서 Blog를 참조해야 한다.
    4) 반대방향을 보면, User <- Blog 도 블로그 검색 결과리스트와 같은 기능이 필요하다면, 충분히 고려해 볼만 하지만, 일대일(OneToOne) 양방향(Bidirectional)에서 적용해 본다.   
    5) Model06에서는 단방향(Unidirectioanl)으로 결정한다. 

2. __다중성은 방향성이 결정나면 쉽게 결정 할 수 있다.__
    1) User(1) -> Blog(1)
    2) OneToOne 이다.
       
3. __OneToOne 에서 고려해야 하는 것은 와래키를 두는 엔티티(관계주인)가 주테이블 또는 대상테이블 일 수 있다.__
    1) 주테이블에 외래키를 두는 경우
        + 주테이블에 매핑된 엔티티(User)에서 대상테이블에 매핑된 엔티티(Blog)를 참조할 수 있으므로 편하다.
        + 객체지향개발에서 선호한다.
        + JPA에서도 주테이블에 외래키를 두면 여러모로 편리한 점이 많다.
    2) 대상 테이블에 외래키를 두는 경우
        + 일대일(OneToOne) 단방향(Unidirectional)에서는 대상 테이블에  외래키를 둘 수 없다.
        + 당연히 JPA에서도 지원하지 않는다.
        + 하지만 일대일(OneToOne) 양방향(Bidirectional)에서는 가능하다.
        + 주로 전통적인 RBMS 스키마 모델링에서 선호한다.
        + 장점은 스키마를 유지하면서 OneToMany로 바꿀 수 있다(블로그를 한 개이상 개설하는 것으로 비즈니스가 변경되는 경우 좋다.)

#### 1-2. Entity Class: User, Blog
1. __User 엔티티 매핑 참고__
2. __Blog 엔티티 매핑 참고__
3. __연관관계 매핑__
    1) OneToOne(User 엔티티, 주테이블)
        
        ```
            .
            .
            @Id
            @Column(name = "id", nullable = false, length = 24)
            private String id;       
            .
            .
	        @OneToOne(fetch = FetchType.LAZY)
	        @JoinColumn(name="blog_no")
	        private Blog blog; 
            .
            .
        ```
        + OneToOne 에서는 Default Fetch Mode는 LAZY로 유지한다.
        + ManyToOne 단방향(Unidirectional)과 유사하다.
        + 일대일(OneToOne) 단방향(Unidirectional)에서는 대상 테이블에 외래키를 주테이블에 두어야 한다.
      
    2) OneToOne(Blog 엔티티, 대상테이블)  
        
        ```
             .
             .
            @Id
            @Column(name = "no")
            @GeneratedValue( strategy = GenerationType.IDENTITY  )
            private Long no;
             .
             .
        ```
        + 일대일(OneToOne) 단방향(Unidirectional)에서는 대상 테이블에 외래키를 둘 수 없다.
    
    3) 생성 스키마
    
        ```
            Hibernate: 
                
                create table blog (
                   no bigint not null auto_increment,
                    name varchar(200) not null,
                    primary key (no)
                ) engine=InnoDB
            Hibernate: 
                
                create table user (
                   id varchar(24) not null,
                    join_date datetime not null,
                    name varchar(24) not null,
                    password varchar(64) not null,
                    blog_no bigint,
                    primary key (id)
                ) engine=InnoDB
            Hibernate: 
                
                alter table user 
                   add constraint FK1g7a1d0no76mnqmr01ys1a40k 
                   foreign key (blog_no) 
                   references blog (no)       
       
        ```

#### 2-1. 요약: 다루는 기술적 내용
1. OneToOne Bidirectional 단점 이해
2. Order -> User Read Only 확인
3. OneToMany 양방향(Bidirectioanal) 보다는 ManyToOne 양방향(Bidirectional) 사용 권고 


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


#### 2-3. Spring Data JPA OrderRepository Test : Spring Data JPA 기반 Repository
1. __JpaUserRepositry__
    1) 기본 Spring Data JPA 기본 레포지토리 인터페이스이다.
    2) 테스트를 위한 목적이기 때문에 별다른 메소드 추가가 없다.

2. __JpaOrdersRepository.java__
    1) 기본 Spring Data JPA 기본 레포지토리 인터페이스이다.
    2) 테스트를 위한 목적이기 때문에 별다른 메소드 추가가 없다.

3. __JpaOrdersQryDslRepository__
    1) QueryDSL 통합 인터페이스 이다.
    2) Orders(주문) 저장 편의 메소드 save(Long userNo, Orders ...orders)를 정의 하였다.

4. __JpaOrdersQryDslRepositoryImpl__
    1) QueryDSL 통합 인터페이스 구현 클래스이다.
    2) Orders(주문) 저장 편의 메소드 save(Long userNo, Orders ...orders)를 구현 하였다.

5. __JpaOrdersRepositoryTest.java__
    1) test01Save
        + 테스트를 위해 2개 User와 3개 주문를 1개의 User로 세팅하여 저장한다.
        + OneToMany 단방향(Unidirectional) 에서 다루었지만 저장 후, 업데이트 쿼리가 한 번 더 실행되는 단점을 OneToMany 양방향(Bidirectional)에서도 그대로 가지고 있다.
        + 쿼리 로그를 보면 Insert(Save)후, FK Update 쿼리가 실행된 것을 볼 수 있다.
              
            ```
                Hibernate: 
                    /* insert me.kickscar.practices.jpa03.model05.domain.Orders
                        */ insert 
                        into
                            orders
                            (address, name, reg_date, total_price) 
                        values
                            (?, ?, ?, ?)
                Hibernate: 
                    /* create one-to-many row me.kickscar.practices.jpa03.model05.domain.User.orders */ update
                        orders 
                    set
                        user_no=? 
                    where
                        no=?            
            ```
    2) test02UpdateUser
        + PK 1L인 Orders를 가져와 영속화 시킨다.
        + PK 2L("마이콜") 인 User를 가져와 영속화 시킨다.
        + Orders에 영속화된 User 엔티티 객체를 세팅하여 업데이트 시킨다.
    
    3) test03UpdateUserResultFails
        + OneToMany 양방향(Biidirectional)에서는 외래키 관리를 두 군데서 하기 때문에 Many(Orders)에서 User(FK)를 변경하는 것을 금지 시키고 One(관계주인, User)에서만 가능하도록 했다.
        + test02UpdateUser에서는 Many(Orders)에 User 엔티티 객체를 세팅하여 업데이트 시켰는데 그 결과를 확인하는 테스트 이다.
        + 테스트 통과 조건은 2L("마이콜")로 변경되지 않아야 한다.
        + 변경 되지 않았다. ReadOnly 설정이 정상적으로 작동하는 것을 알 수 있다.
    
    4) 결론
        + OneToMany 양방향(Biidirectional)는 ManyToOne 양방향(Biidirectional)과 완전 동일한 연관관계 이다.
        + One쪽을 주인으로 보았지만, RDBMS의 특성상 주인의 관계설정 필드를 가지지 못하고 Many쪽에 두는 특이한 점이 OneToMany 단방향과 마찬가지로 가지고 있다.
        + Many쪽에서는 JoinColumn 설정을 정상적으로 할 수 있지만, 와래키 관리 포인트가 두군데가 되어 One쪽에 그 설정을 맡기고 ReadOnly 설정을 하게된다.
        + 결론적으로 OnToMany 단방향에다가 반대편에 탐색을 위한 필드(User)를 하나 추가 한 형태로 그 연관관계가 존재하지 않느다 보는 것이 맞다.
        + 전반적으로 부자연스러운 매핑 설정을 계속 해야한다.
        + ManyToOne 양방향(Bidirectional) 매핑을 사용하도록 하자.