## Model06 : 일대일(OneToOne) - 양방향(Bidirectional), 주테이블에 외래키

### 1. Domain

#### 1-1. 테이블 연관관계 VS 객체 연관관계

<img src="http://assets.kickscar.me:8080/markdown/jpa-practices/37001.png" width="500px" />
<br>

<img src="http://assets.kickscar.me:8080/markdown/jpa-practices/37002.png" width="500px" />
<br>
        
1. __보통은 서비스에서 방향성을 찾는 경우가 많다.__
    1) 블로그 한 개만을 개설할 수 있는 회원제 블로그 서비스
    2) 보통 블로그에 방문할 경우 blog.domain.com/userid 이런 식으로 접근하는 경우가 많다.
    3) userid로 블로그 정보(블로그 이름, 블로그 소개, 카테고리, 포스트등) 가져와야 한다. User -> Blog 연관관계로 User에서 Blog를 참조해야 한다.
    4) 반대방향을 보면, User <- Blog 참조도 블로그 검색 결과 리스트와 같은 비즈니스 요구사항에서는 필요해 보인다.
    5) 따라서 양방향(Bidirectioanl)으로 결정한다. 

2. __다중성은 방향성이 결정나면 쉽게 결정 할 수 있다.__
    1) User(1) <-> Blog(1)
    2) OneToOne 이다.
       
3. __OneToOne 에서 고려해야 하는 것은 와래키를 두는 엔티티(관계주인)가 주테이블 또는 대상테이블 일 수 있다.__
    1) 주테이블에 외래키를 두는 경우
        + 주테이블에 매핑된 엔티티(User)에서 대상테이블에 매핑된 엔티티(Blog)를 참조할 수 있으므로 편하다.
        + 객체지향개발에서 선호한다.
        + JPA에서도 주테이블에 외래키를 두면 여러모로 편리한 점이 많다.
    2) 대상 테이블에 외래키를 두는 경우
        + 일대일(OneToOne) 단방향(Unidirectional)에서는 불가능하지만 일대일(OneToOne) 양방향(Bidirectional)에서는 가능하다.
        + 주로 전통적인 RBMS 스키마 모델링에서 선호한다.
        + 장점은 스키마를 유지하면서 OneToMany로 바꿀 수 있다(블로그를 한 개이상 개설하는 것으로 비즈니스가 변경되는 경우 좋다.)
    3) Model07에서는 주테이블에 왜래키를 둔다.
    
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
        + OneToOne 에서는 Default Fetch Mode는 EAGER 이다. Global Fetch Mode LAZY로 수정했다. 
        + ManyToOne 단방향(Bidirectional)과 유사하다.
        + 일대일(OneToOne) 양방향(Bidirectional)에서는 양쪽 테이블에 외래키를 둘 수 있지만, Model07에서는 주테이블(User)에 외래키를 두었다.
      
    2) OneToOne(Blog 엔티티, 대상테이블)  
        
        ```
             .
             .
            @Id
            @Column(name = "no")
            @GeneratedValue(strategy=GenerationType.IDENTITY)
            private Long no;
             .
             .
            @OneToOne(mappedBy = "blog", fetch = FetchType.LAZY)
            private User user;       
             .
             .
        ```
        + 양방향이므로 연관관계의 주인을 정해야 한다.
        + User 테이블이 FK를 가지고 있으므로 User 엔티티에 있는 User.blog가 연관관계의 주인이다
        + 따라서 반대 매핑인 Blog의 Blog.user는 mappedBy를 선언해서 연관관계의 주인이 아니라고 설정해야 한다.
    
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
        + 스키마 생성 DDL를 보면 OneToOne 단방향(Unidirectional)과 다르지 않다.

#### 2-1. 요약: 다루는 기술적 내용
1. 양방향(Bidirectional)에서 관계 필드 변경과 Update 실행 여부
2. 

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

2. __JpaBlogRepository.java__
    1) 기본 Spring Data JPA 기본 레포지토리 인터페이스이다.
    2) 테스트를 위한 목적이기 때문에 별다른 메소드 추가가 없다.

3. __JpaBlogRepositoryTest.java__
    1) test01Save
        + 엔티티 객체 영속화
        + 테스트 데이터 저장
    2) test02UpdateUser
        + DB로 부터 Blog, User 엔티티 객체를 각각 영속화 시킨다.
        + 양방향(Bidirectional) 중 관계의 주인이 아닌 쪽에서 연관관계 필드를 변경한다.  
            ```
                blog.setUser(user);
                    
            ```
    3) test03VerifyUpdateUser01
        + 앞의 test02UpdateUser의 결과를 확인한다.
        + User를 통해 Blog를 탐색해보면 null이다.
        + blog_no(FK)가 update 안 되었다.
    4) test02UpdateUser02
        + DB로 부터 Blog, User 엔티티 객체를 각각 영속화 시킨다.
        + 양방향(Bidirectional) 중 관계의 주인 쪽에서 연관관계 필드를 변경한다.  
            ```
                user.setBlog(blog);
                    
            ```
    5) test03VerifyUpdateUser02
        + 앞의 test02UpdateUser02의 결과를 확인한다.
        + User를 통해 Blog를 탐색해서 블로그 이름을 확인해 볼 수 있다.
        + blog_no(FK)가 정상적으로 update 되었다.
