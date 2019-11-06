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
    2) Song(\*) -> Genre(\*)
    3) ManyToMany 이다.
       
3. __다대다 연관관계의 관계형 데이터베이스와 JPA에서의 차이점__
    1) 관계형 데이터베이스는 정규화된 테이블 2개로 다대다 관계를 표현할 수 없다.
    2) 그래서 보통 다대다 관계 를 일대다, 다대일 관계로 풀어내는 연결 테이블을 사용한다.
        
        <img src="http://assets.kickscar.me:8080/markdown/jpa-practices/39003.png" width="800px" />
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
        + ManyToMany  기본 페치 전략은 LAZY 이다.
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


#### 2-3. JpaSongRepository Test : Spring Data JPA 기반 Repository
1. __JpaGenreRepositry__
    1) 기본 Spring Data JPA 기본 레포지토리 인터페이스이다.
    2) 테스트를 위해 Genre 엔티티 영속화 목적이기 때문에 별다른 메소드 추가가 없다.

2. __JpaSongRepositry__
    1) 기본 Spring Data JPA 기본 레포지토리 인터페이스이다.
    2) 테스트를 위한 목적이기 때문에 별다른 메소드 추가가 없다.

3. __JpaSongQryDslRepositry__
    1) Lazy 로딩으로 Genre를 가져오지 않고 fetch join으로 Genre가 포함된 Song을 가져오는 메소드 2개를 정의
    2) Song findById2(no) - no(PK)로 Genre가 포함된 Song 엔티티 객체 1개를 가져온다.
    3) List<Song> c() - Genre가 포함된 Song 엔티티 객체 리스트를 가져온다. 

4. __JpaSongQryDslRepositryImpl__
    1) findById2, findById2의 구현
    2) QueryDSL 통합 구현

5. __JpaSongRepositoryTest__
    1) test01Save
        + 쟝르1, 쟝르2와 노래1의 연관관계를 설정했다.
        + 쟝르1, 쟝르4와 노래2의 연관관계를 설정했다.
        + 노래1, 노래2를 각각 저장할 때 연결 테이블에도 값이 저장된다.
        + 노래1이 저장될 때 실행된 SQL
            ```
                Hibernate: 
                    /* insert me.kickscar.practices.jpa03.model09.domain.Song
                        */ insert 
                        into
                            song
                            (title) 
                        values
                            (?)

                Hibernate: 
                    /* insert collection
                        row me.kickscar.practices.jpa03.model09.domain.Song.genres */ insert 
                        into
                            song_genre
                            (song_no, genre_no) 
                        values
                            (?, ?)
                Hibernate: 
                    /* insert collection
                        row me.kickscar.practices.jpa03.model09.domain.Song.genres */ insert 
                        into
                            song_genre
                            (song_no, genre_no) 
                        values
                            (?, ?)                              
            ```    
    2) test02FindById
        + 기본 메소드 findById(no) 테스트
        + Lazy 로딩이기 때문에 Genre 객체를 탐색하기 전까지는 지연 로딩을 한다.
            ```
                Hibernate: 
                    select
                        song0_.no as no1_1_0_,
                        song0_.title as title2_1_0_ 
                    from
                        song song0_ 
                    where
                        song0_.no=?          
            ```
        + genres의 size()를 호출하기 전까지는 프록시 객체 초기화가 되지 않았다.
        + genres의 size()를 호출하면 genre 리스트를 가져오는 쿼리가 실행된다.
            ```
                Hibernate: 
                    select
                        genres0_.song_no as song_no1_2_0_,
                        genres0_.genre_no as genre_no2_2_0_,
                        genre1_.no as no1_0_1_,
                        genre1_.abbr_name as abbr_nam2_0_1_,
                        genre1_.name as name3_0_1_ 
                    from
                        song_genre genres0_ 
                    inner join
                        genre genre1_ 
                            on genres0_.genre_no=genre1_.no 
                    where
                        genres0_.song_no=?          
            ```
         + 마지막에서 프록시 객체가 초기화되어 콜렉션이 채워져 있음을 알수 있다.
        
6. __JpaGenreRepositoryTest__
    1) Song -> Genre 단방향이기 때문에  Genre쪽에서는 객체 탐색등이 불가능하다.
    2) 저장, 삭제, 변경, 카운팅 정도의 기본 메소드 사용으로 충분하다.


         
