## Model06 : 일대일(OneToOne) - 양방향(Bidirectional), 주테이블에 외래키, 식별관계

### 1. Domain

#### 1-1. 테이블 연관관계 VS 객체 연관관계

<img src="http://assets.kickscar.me:8080/markdown/jpa-practices/38001.png" width="500px" />
<br>

<img src="http://assets.kickscar.me:8080/markdown/jpa-practices/38002.png" width="500px" />
<br>
        
1. __보통은 서비스에서 방향성을 찾는 경우가 많다.__
    1) 블로그 한 개만을 개설할 수 있는 회원제 블로그 서비스
    2) 보통 블로그에 방문할 경우 blog.domain.com/userid 이런 식으로 접근하는 경우가 많다.
    3) userid로 블로그 정보(블로그 이름, 블로그 소개, 카테고리, 포스트등) 가져와야 한다. User -> Blog 연관관계로 User에서 Blog를 참조해야 한다.
    4) 반대방향을 보면, User <- Blog 참조도 블로그 검색 결과 리스트와 같은 비즈니스 요구사항에서는 필요해 보인다.
    5) 따라서 양방향(Bidirectioanl)으로 결정한다. 

2. __다중성은 방향성이 결정나면 쉽게 결정 할 수 있다.__
    1) 이 블로그 서비스는 회원 한명에게 하나의 블로그만 개설할 수 있다.
    2) User(1) <-> Blog(1)
    3) OneToOne 이다.
       
3. __식별관계 매핑__
    1) OneToOne 에서는 주테이블 또는 대상테이블에 외래키를 두는 것이 가능하다.
    2) Model08에서는 대상 테이블에 FK를 두고 이 키를 PK로도 사용하여 주테이블과 식별관계로 매핑한다.
    3) 식별관계 VS 비식별관계
        + 식별 관계(Identifying Relationship)
            1) 식별 관계는 부모 테이블의 기본 키를 내려 받아서 자식 테이블의 기본 키 + 외래 키로 사용하는 관계다.
            2) 식별 관계는 부모 테이블의 기본 키를 자식 테이블로 전파하면서 자식 테이블의 기본키 컬럼이 점점 늘어난다. 조인할 때 SQL이 복잡해지고 기본 키 인덱스가 불필요하게 커질 수 있다.
            3) 식별 관계는 2개 이상의 컬럼을 합해서 복합 기본 키를 만들어야 하는 경우가 많다.(JPA에서 지원은 하지만 복잡하다.)
            4) 식별 관계를 사용할 때 기본 키로 비즈니스 의미가 있는 자연 키 컬럼을 조합하는 경우가 많다. 비즈니스 요구사 항은 시간이 지남에 따라 언젠가는 변한다. 식별 관계의 자연 키 컬럼들이 자식에 손자까지 전파되 면 변경하기 힘들다.
        + 비식별 관계(Non-Identifying Relationship)
            1) 비식별 관계는 부모 테이블의 기본 키를 받아서 자식 테이블의 외래 키로만 사용하는 관계다.(보통, DB 스키마 모델링에서는 비식별 관계를 선호한다.)
            2) 일대일 관계를 제외하고 식별 관계는 2개 이상의 컬럼을 묶은 복합 기본 키를 사용한다. JPA에서 복합 키는 별도의 복합 키 클래스를 만들어서 사용해야 한다. 따라서 컬럼이 하나인 기본 키를 매핑 하는 것보다 많은 노력이 필요하다.
            3) 비식별 관계의 기본 키는 주로 대리 키를 사용하는데 JPA는 @GenerateValue 처럼 대리 키를 생 성하기 위한 편리한 방법을 제공한다.
        + 권고 사항
            1) ORM 신규 프로젝트 진행시 추천하는 방법은 될 수 있으면 비식별관계를 사용하고 기본키는 Long 타입의 대리 키를 사용하는 것이다.
            2) 대리키(후보키, Alternate Key)는 비즈니스와 아무 관련이 없다. 따라서 비즈니스가 변경되 어도 유연한 대처가 가능하다는 장점이 있다.
            3) JPA는 @GenerateValue 를 통해 간편하게 대리 키를 생성할 수 있다. 그리고 식별자 컬럼이 하나여서 쉽게 매핑할 수 있다.
            4) 식별자의 데이터 타입은 Long을 추천
            5) 필수적 비식별 관계를 사용하는 것이 좋다. 선택적인 비식별 관계는 NULL 을 허용하므로 조인할 때에 외부 조인을 사용해야 한다. 반면에 필수적 관계는 NOT NULL 로 항상 관계가 있다는 것을 보장하므로 내부 조인만 사용해도 된다. 
    
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
            @OneToOne(mappedBy="user", fetch=FetchType.LAZY)
            private Blog blog;
            .
            .
        ```
        + OneToOne 에서는 Default Fetch Mode는 EAGER 이다. Global Fetch Mode LAZY로 수정했다. 
        + 외래키 관리를 대상 테이블이 하기 때문에 관계 주인이 아님을 mappedBy를 통해 선언하고 반대편 엔티티(Blog)의 연관관계 필드를 지정했다.
      
    2) OneToOne(Blog 엔티티, 대상테이블)  
        
        ```
             .
             .
            @Id
            @Column(name = "id")
            private String id;
             .
             .
            @MapsId
            @OneToOne(fetch=FetchType.LAZY)
            @JoinColumn(name="id")
            private User user;       
             .
             .
        ```
        + @Id로 PK 설정을 했다. 식별관계로 부모테이블(User)의 PK를 사용할 것이기 때문에 String으로 타입을 바꿨다.
        + 외래키 관리를 해야 하기때문에 @JoinColumn(name="id")를 통해 외래키 설정을 하였다. 식별관계 설정을 위해 PK와 필드 이름이 같다.
        + 여기에 @MapsId를 통해 실제적인 PK 필드 id와 매핑을 해야 한다. 
    
    3) 생성 스키마
    
        ```
            Hibernate: 
                
                create table blog (
                    id varchar(24) not null,
                    name varchar(200) not null,
                    primary key (id)
                ) engine=InnoDB
            Hibernate: 
                
                create table user (
                    id varchar(24) not null,
                    join_date datetime not null,
                    name varchar(24) not null,
                    password varchar(64) not null,
                    primary key (id)
                ) engine=InnoDB
            Hibernate: 
                
                alter table blog 
                   add constraint FKqx432k8povl16musk8wt5cn7y 
                   foreign key (id) 
                   references user (id)       
       
        ```
        + 두 테이블의 PK필드의 타입이 같고 FK설정을 통해 식별관계가 설정되어 있음을 알 수 있다.

#### 2-1. 요약: 다루는 기술적 내용
1. 양방향(Bidirectional)에서 관계 필드 변경과 Update 반영 여부
2. Projection: @QueryProjection 사용법, Spring Data JPA 지원

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
    4) test04UpdateUser02
        + DB로 부터 Blog, User 엔티티 객체를 각각 영속화 시킨다.
        + 양방향(Bidirectional) 중 관계의 주인 쪽에서 연관관계 필드를 변경한다.  
            ```
                user.setBlog(blog);
                    
            ```
    5) test05VerifyUpdateUser02
        + 앞의 test02UpdateUser02의 결과를 확인한다.
        + User를 통해 Blog를 탐색해서 블로그 이름을 확인해 볼 수 있다.
        + blog_no(FK)가 정상적으로 update 되었다.
        
    6) test06findAllUser Vs test07findAll
        + test06findAllUser는 관계 주인 필드의 지연로딩을 테스트한다. 연관관계 주인 필드의 글로벌 페치 전략 LAZY는 잘 동작한다.
        + test06findAll는 지연로딩이 되지 않고 N+1이 발생하는 로그를 볼 수 있다.(@Transacational도 없다)
            1) OnetoOne Bidirectional 에서 mappedBy 선언된 연관 필드의 글로벌 페치 전략 LAZY는 무시된다.
            2) 이는 Proxy의 한계 때문에 발생하는데 반드시 해결해야 하면, Proxy 대신 bytecode instrumentation을 사용하면 된다.
            3) EAGER로 즉시 로딩을 하지만 Join도 되지 않는다. 따라서 N+1이 발생한다.
            4) BlogRepository의 findAll은 사용하지 말고 개선된 메소드를 만들어야 한다.
    
    7) test08findAll2
        + 앞의 test07findAll에서 문제가 되었던 기본메소드 findAll를 QueryDSL 통합방식으로 페치 조인한 findAll2()를 테스트한다.
        + 쿼리 로그를 확인해 보면, 두 테이블에 inner join이 정상적으로 실행된 것을 알 수 있다.
            ```
                select
                    blog0_.no as no1_0_0_,
                    user1_.id as id1_1_1_,
                    blog0_.name as name2_0_0_,
                    user1_.blog_no as blog_no5_1_1_,
                    user1_.join_date as join_dat2_1_1_,
                    user1_.name as name3_1_1_,
                    user1_.password as password4_1_1_ 
                from
                    blog blog0_ 
                inner join
                    user user1_ 
                        on blog0_.no=user1_.blog_no          
            ```
    8) test09findAll3
        + 페치 조인과 함께 BlogDto3 클래스 방식의 프로젝션을 하는 findAll3 메소드를 테스트 한다.
        + @QueryProjection 사용하는 데, 앞에서 했던 방식과 다른 것은 Projections.constructor, Projections.bean, Projections.field 이런 함수를 쓰지 않아도 된다.
        + BlogDto3에 @QueryProjection 적용
            ```
                public class BlogDto3 {
                    private Long no;
                    private String name;
                    private String userId;
                    
                          .
                          .
                          .
                
                    @QueryProjection
                    public BlogDto3(Long no, String name, String userId){
                        this.no = no;
                        this.name = name;
                        this.userId = userId;
                    }
                         .
                         .
                         .
                }              
            ```
            1) Q클래스가 자동으로 생성해야한다.
            2) JpaConfig.java 에 EntityManagerFactory Bean 설정에 정의한 DTO클래스가 스캐닝될 수 있도록 패키지를 추가해야 한다.
                ```
                    em.setPackagesToScan(new String[] { "me.kickscar.practices.jpa03.model07.domain", "me.kickscar.practices.jpa03.model07.dto" });
               
                ```
            3) QBlogDto3.java 가 자동으로 생성되며 import를 통해 Projection에 사용하면 된다.
                <img src="http://assets.kickscar.me:8080/markdown/jpa-practices/37003.png" width="500px" />
                <br>
        + findAll3() 구현 코드
            ```
                return queryFactory
                        .select(new QBlogDto3(blog.no, blog.name, blog.user.id.as("userId")))
                        .from(blog)
                        .innerJoin(blog.user)
                        .fetch();          
            ```
            1) projection 이기 때문에 fetchJoin()를 사용하지 말아야 한다.
            2) BlogDto3 클래스를 사용하면 안되고 자동으로 생성된 QBlogDto3 클래스로 projection을 해야 한다.
            3) 비교적 레포지토리 코드가 간결해 졌다.
        + 쿼리로그
            ```
                select
                    blog0_.no as col_0_0_,
                    blog0_.name as col_1_0_,
                    user1_.id as col_2_0_ 
                from
                    blog blog0_ 
                inner join
                    user user1_ 
                        on blog0_.no=user1_.blog_no          
            ```
            1) inner join과 projection이 적용된 SQL문 로그를 확인할 수 있다.
    9) test09findAllByOrderByNoDesc
        + Spring Data JPA에서 지원하는 방식은 쿼리 메소드 + DTO Interface 를 사용하는 방식이다.
        + 단점이기도 하고 당연한 것이기도 하지만, 쿼리 메소드(Query Method)에만 사용할 수 있다는 것이다.
        + DTO Interface 정의
            ```
                public interface BlogDto2 {
                    Long getNo();
                    String getName();
                    String getUserId();
                }
            ```
        + 쿼리 메소드 정의
            ```
                public interface JpaBlogRepository extends JpaRepository<Blog, Long>, JpaBlogQryDslRepository {
                    List<BlogDto2> findAllByOrderByNoDesc();
                }          
            ```
        + JPQL Projection에서는 기본적으로 반환하는 타입이 Tuple이다. DTO 인터페이스의 구현체 클래스는 org.springframework.data.jpa.repository.query.AbstractJpaQuery$TupleConverter$TupleBackedMap 으로 JPA가 런타임에 객체로 생성한다.
        + DTO 인터페이스는 @QueryProjection를 사용한 클래스기반 Projection에서 필요하던 Q클래스 스캐닝이 필요없기 때문에 별다른 설정을 할 필요가 없다.