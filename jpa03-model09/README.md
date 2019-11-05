## Model09 : 다대다(ManyToMany) 단방향(Unidirectional)


### 1. Domain

#### 1-1. 테이블 연관관계 VS 객체 연관관계

<img src="http://assets.kickscar.me:8080/markdown/jpa-practices/39001.png" width="500px" />
<br>

<img src="http://assets.kickscar.me:8080/markdown/jpa-practices/39002.png" width="500px" />
<br>
        
1. __보통은 서비스에서 방향성을 찾는 경우가 많다.__
    1) 음반검색에서 노래와 쟝르의 관계이다.
    2) 노래 정보를 보여줄 때 그 노래의 쟝르가 필요한 경우다.
    3) 쟝르 검색을 통해 해당 노래를 찾는 것도 필요하지만 이는 다대다 양방향(Bidirectional)에서 다룬다.
    4) Model09에서는 Song -> Jenre로 참조가 이루어 지는 단방향(Unidirection)을 매핑한다.

2. __다중성은 방향성이 결정나면 쉽게 결정 할 수 있다.__
    1) Song은 다수의 쟝르에 포함될 수 있다. 쟝르도 해당 쟝르의 노래들이 많다.
    2) Song(*) -> Genre(*)
    3) ManyToMany 이다.
       
3. __다대다 연관관계의 관계형 데이터베이스와 JPA에서의 차이점__
    1) 관계형 데이터베이스는 정규화된 테이블 2개로 다대다 관계를 표현할 수 없다.
    2) 그래서 보통 다대다 관계 를 일대다, 다대일 관계로 풀어내는 연결 테이블을 사용한다.
        <img src="http://assets.kickscar.me:8080/markdown/jpa-practices/39003.png" width="500px" />
        <br>    
    3) 객체는 테이블과 다르게 객체 2개로 다대다 관계를 만들 수 있다.
    4) Song 객체는 컬렉션을 사용해서 쟝르들을 참조하면 되고 반대로 쟝르들도 컬렉션을 사용해서 회원들을 참조하면 된다.
    
#### 1-2. Entity Class: User, Blog
1. __Song 엔티티 매핑 참고__
2. __Jenre 엔티티 매핑 참고__
3. __연관관계 매핑__
    1) ManyToMany(Song 엔티티)
        
        ```
            .
            .
            @Id
            @Column(name = "no")
            @GeneratedValue( strategy = GenerationType.IDENTITY  )
            private Long no;       
            .
            .
            @ManyToMany
            @JoinTable(name = "song_genre", joinColumns = @JoinColumn(name = "song_no"), inverseJoinColumns = @JoinColumn(name = "genre_no"))
            private List<Genre> genres = new ArrayList<Genre>();
            .
            .
        ```
        + @ManyToMany 와 @JoinTable 을 사용해서 연결 테이블을 바로 매핑한다.
        + 노래와 쟝르를 연결하는 노래_쟝르(Song_Genre)엔티티 없이 매핑을 완료할 수 있다.
        + @JoinTable.name : 연결 테이블을 지정한다. 
        + @JoinTable.joinColumns : 현재 방향인 노래와 매핑할 조인 컬럼 정보를 지정한다. song_no로 지정
        + @JoinTable.inverseJoinColumns : 반대 방향인 쟝릐와 매핑할 조인 컬럼 정보를 지정한다. genre_no로 지정했다.
        + @ManyToMany로 매핑한 덕분에 다대다 관계를 사용할 때는 이 연결 테이블을 신경쓰지 않아 도 된다.
      
    2) ManyToMany(Genre 엔티티)
        
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
        + ManyToMany 단방향에서는 관계주인이 아닌 엔티티는 별다른 설정을 하지 않아도 된다.
    
    3) 생성 스키마
    
        ```
            Hibernate: 
                
                create table genre (
                   no bigint not null auto_increment,
                    abbr_name varchar(5) not null,
                    name varchar(50) not null,
                    primary key (no)
                ) engine=InnoDB
            Hibernate: 
                
                create table song (
                   no bigint not null auto_increment,
                    title varchar(100) not null,
                    primary key (no)
                ) engine=InnoDB
            Hibernate: 
                
                create table song_genre (
                   song_no bigint not null,
                    genre_no bigint not null
                ) engine=InnoDB
            Hibernate: 
                
                alter table song_genre 
                   add constraint FKmdutew4w1ll7a9nd8uhvnajs2 
                   foreign key (genre_no) 
                   references genre (no)
            Hibernate: 
                
                alter table song_genre 
                   add constraint FK5njrlut9t666xo6kp4f339eua 
                   foreign key (song_no) 
                   references song (no)     
       
        ```
        + 세 개의 테이블을 생성한다.
        + 엔티티 테이블외에 song_genre 연결테이블이 생성 되었다.
        + song_genre 테이블은 다대다 관계를 일대다, 다대일 관계로 풀어내기 위해 필요한 연결 테이블이다.

### 2. Repository 작성 & Testing

#### 2-1. 요약: 다루는 기술적 내용
1. 
2. 
3. 


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


#### 2-3. JpaUserRepository Test : Spring Data JPA 기반 Repository
1. __JpaSongRepositry__
    1) 기본 Spring Data JPA 기본 레포지토리 인터페이스이다.
    2) 테스트를 위한 목적이기 때문에 별다른 메소드 추가가 없다.

2. __JpaSongRepositoryTest__
    1) test01Save
        + 순수객체(영속화되지 않은 객체, 엔티티매니저 관리 대상이 아닌 객체)를 save() 전달하여 영속화 시키는 테스트이다.
        + CrudRepository.save(entity)호출 시, 외부에서 전달하는 entity 객체는 대부분 영속화되서 다음 코드에서 영속화 객체로 사용하면 된다.


         
