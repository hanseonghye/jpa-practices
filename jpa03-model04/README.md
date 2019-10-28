## Model04 : 일대다(OneToMany) - 단방향


### 1. Domain

#### 1-1. 테이블 연관관계 VS 객체 연관관계

   <img src="http://assets.kickscar.me:8080/markdown/jpa-practices/34001.png" width="500px" />
   <br>

   <img src="http://assets.kickscar.me:8080/markdown/jpa-practices/34002.png" width="500px" />
   <br>
   
   1. __게시판 게시물(Board)의 댓글(Comment)__
      + 게시판 게시물 보기 페이지에서 게시물 내용 아래에 달린 댓글들이다. Board View 페이지에서 다음과 같은 관계가 있을 것이다.   
        Board -> Comment Navigational Association  
      + 고려해야 할 반대 방향의 비즈니스 로직은 없어 보인다. 
      + Board -> Comment 관계매핑은 단방향(Unidirectional)이다.  

   2. __다중성은 방향성이 결정나면 쉽게 결정 할 수 있다.__
      + Board(1) -> Comment(N)
      + OneToMany 이다.
  
   3. OneToMany
      + 참조 필드의 타입으로 Map, Set, List, Collection등을 사용하게 된다.
      + JPA 2.0 부터 지원


#### 1-2. Entity Class: Board, Comment
1. __Board 엔티티 매핑 참고__
2. __Comment 엔티티 매핑 참고__
3. __연관관계 매핑__
    + OneToMany(Board 엔티티)
        
        ```
            .
            .
            @OneToMany(fetch = FetchType.LAZY)
            @JoinColumn(name = "board_no")
            private List<Comment> comments;
            .
            .
        ```
        - OneToMany 단방향의 특이점  
            1) 외래키 관리를 Many쪽(Comment)에서 해야하는데, 단방향이기 때문에 Comment에 매핑 필드가 없다는 것이다.  
            2) 그래도 관계 주인필드인 Board.comments가 외래키 관리를 해야 한다.     
            3) 그래서 @JoinColumn name에 Comment 엔티티의 FK(board_no)를 꼭 지정해야 한다.  
            4) 하지만, 스키마 생성 DDL를 보면, Many쪽인 Comment 테이블에 Board에 대한 FK를 둔다. 
        
                <img src="http://assets.kickscar.me:8080/markdown/jpa-practices/34004.png" width="500px" />
                <br>
                
            5) @JoinColumn를 하지 않으면 JPA의 Join Table 전략이 적용되어 연결 테이블을 중간에 두고 연관관계를 관리한다.    
            6) OneToMany 에서는 Default Fetch Mode는 LAZY 이다.  
      
    + 객체 관계 설정에 주의 할점
        - 단점을 분명하게 인지할 것: 외래키 관리를 다른 테이블에서 한다. (Many 쪽에서 하지 않는다.) 이는 다른 테이블에 FK가 있으면 Insert 작업 시, 추가적으로 Update를 해야 한다.
        - **OneToMany Unidirectional을 써야 하다면 ManyToOne Bidirectional를 사용하는 것을 보통 추천한다**  


###2. SpringBoot Test Case

####2-1. 요약: 다루는 기술적 내용
1. OneToMany Unidirectional 단점 이해
2. Global Fetch 전략 LAZY에 대한 이해
3. 성능 및 비지니스 요구 따른 레포지토리 메소드 최적화 하기

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

#### 2-3. Spring Data JPA BoardRepository Test : Spring Data JPA 기반 Repository
1. __JpaBoardRepository.java__
    1) 기본메소드와 쿼리메소드 

2. __JpaBoardQryDslRepository.java__
    1) 추가/성능 개선이 필요한 쿼리메소드 정의

3. __JpaBoardQryDslRepositoryImp.java__
    1) 추가/성능 개선이 필요한 쿼리메소드 구현

4. __JpaBoardRepositoryTest.java__
    1) test01Save
        - **코멘트 저장하는 방법 이해(How to Persist Many Side's Entity for OneToMany Unidirectional Model)**
            ```
                User user = new User();
                user.setName("둘리");
                user.setPassword("1234");
                user.setEmail("dooly@kickscar.me");
                user.setGender(GenderType.MALE);
                user.setRole(RoleType.USER);
                userRepository.save(user1);
       
                Board board = new Board();
                board.setTitle("제목");
                board.setContents("내용");
                board.setUser(user);
                boardRepository.save(board);
       
                Comment comment = new Comment();
                comment.setContents("댓글");
       
                commentRepository.save(comment);
                board.getComments().add(comment);
            ```
            + Many쪽 Comment 엔티티가 외래키 관리를 하지 않기 때문에 Insert(Save)후, FK Update를 해야하며 반대편 Board 엔티티를 통해 한다.  
      
        - 실제 코드는 기본 메소드 save(entity)를 오버로딩한 JpaCommentQryDslRepositoryImp.save(boardNo, comments) 이다.  
          
            ```
                commentRepository.save(1L, new Comment("댓글1"));
                commentRepository.save(2L, new Comment("댓글2"), new Comment("댓글3"));
            ```
            + save(boardNo, comments) 메소드를 보면, Comment 엔티티에 board의 no를 세팅할 필드가 없고 당연히 setter 자체가 없기 때문에 두 개로 나눠 함께 전달한다.  
            + comments 파라미터는 Variable Arguments(Varargs)를 사용하여 여러 Comment 엔티티 객체를 전달할 수 있도록 하였다.  
            + 실행된 Update 쿼리 로그    
         
                ```
                    Hibernate: 
                    /* create one-to-many row me.kickscar.practices.jpa03.model04.domain.Board.comments */ update
                        comment 
                    set
                        board_no=? 
                    where
                        no=?         
                ```

        - OneToMain Unidirectional의 단점은 다음 ManyToOne Bidirectional 으로 바꿨을 때 예상되는 코드와 비교해 보면 명확히 알 수 있다.

            ```
                Comment comment = new Comment();
                comment.setBoardNo(1L);
                comment.setContents("댓글");
       
                commentRepository.save(comment);
            ```
    2) test02SaveEagerProblem
        - Comment 엔티티 저장에서 ManyToOne 기본 페치전략 EAGER의 문제점 테스트
        - **글로벌 Fecth 전략 - LAZY(지연로딩)**
            + EAGER : ManyToOne, OnetoOne
            + LAZY : OneToMany
            + 보통 위의 기본 페치전략을 유지하지 않는다. 특히, EAGER 페치전략을 LAZY로 해서 애플리케이션 전체 페치전략을 LAZY로 유지하는 것이 기본이다.
        - 테스트 코드
            ```
                Board board = em.find(Board.class, boardNo);    
            ```
            
            + Board 엔티티를 DB로부터 Fetch하여 영속화 시키기 위해 실행된 쿼리는,   
            
            ```
                Hibernate: 
                    select
                        board0_.no as no1_0_0_,
                        board0_.contents as contents2_0_0_,
                        board0_.hit as hit3_0_0_,
                        board0_.reg_date as reg_date4_0_0_,
                        board0_.title as title5_0_0_,
                        board0_.user_no as user_no6_0_0_,
                        user1_.no as no1_2_1_,
                        user1_.email as email2_2_1_,
                        user1_.gender as gender3_2_1_,
                        user1_.name as name4_2_1_,
                        user1_.password as password5_2_1_,
                        user1_.role as role6_2_1_ 
                    from
                        board board0_ 
                    left outer join
                        user user1_ 
                            on board0_.user_no=user1_.no 
                    where
                        board0_.no=?            
            ```
            + Board -> User(ManyToOne Unidirectional) 기본 Fetch 모드가 EAGER이기 때문에 User Entity와 Outer Join이 자동으로 실행된 것을 알 수 있다.
            + 이는 Comment에 FK세팅을 위해서 DB로 부터 Board 엔티티 객체를 가져오는데 실행하는 쿼리로는 조금은 부담스럽다.  
            
        - LAZY로 바꾸고 실행된 쿼리 로그를 보자.
            + Entity 매핑 수정
             
                ```
                    @ManyToOne(fetch = FetchType.LAZY)
                    @JoinColumn( name = "user_no" )
                    private User user;
                ```
            
            + 테스트 실행 후, 쿼리로그 확인     
             
                ```
                Hibernate: 
                    select
                        board0_.no as no1_0_0_,
                        board0_.contents as contents2_0_0_,
                        board0_.hit as hit3_0_0_,
                        board0_.reg_date as reg_date4_0_0_,
                        board0_.title as title5_0_0_,
                        board0_.user_no as user_no6_0_0_ 
                    from
                        board board0_ 
                    where
                        board0_.no=?
                ```
            
            + Join이 실행되지 않았으며 Comment 엔티티 객체 저장에 아무런 문제가 없다.
            + 성능 향상을 기대할 수 있다. 
    3) test03BoardListLazyProblem
        - 쿼리메소드 JpaBoardRepository.findAllByOrderByRegDateDesc()를 사용해서 게시물 리스트를 가져오는 테스트이다. 
        - 테스트 코드는 LAZY로딩 시, 발생하는 N+1 문제를 확인해 본다.
        - 게시판 리스트를 가져오기 위한 쿼리(1) 그리고 게시글이 있는 User 이름을 가져오기 위한 쿼리(2 == N)이 실행됨을 알 수 있다.(쿼리 로그참고) 즉, LAZY로딩의 N+1 문제가 발생한다.  
            + EAGER -> LAZY로 페치전략 수정 후, 코멘트 저장시 발생한 성능 이슈 1개가 해결되었다고 성능이 개선되었을 것이라 기대하면 안된다. 
            + 페치전략 수정으로 발생할 수 있는 성능 이슈는 N+1 문제로 일어날 수 있는 게시판 리스트, 글보기 등 다른 비즈니스 로직에서 성능 문제가 발생할 수 있기 때문이다.
    4) test04BoardListLazyProblemSolved()
     
        - N+1 문제를 해결하기 위한 **성능 및 비지니스 요구 따른 레포지토리 메소드 최적화 하기** 를 다루는 테스트이다.
        - JpaBoardQryDslRepositoryImpl.findAll3(pageable) 메소드를 테스트한다.
        - 해결을 위한 방법은 JPQL(QueryDSL)로 해결할 수 있다. 
            1) QueryDSL 경우에는 innerJoin(), fetchJoin()를 사용해 개선할 수 있다(jpa03-model02 참고)
            2) 게시판 리스트에서는 User 정보가 다 필요없다. 이름과 번호 정도일 것이다. Projection과 DTO를 함께 고려하면 성능 향상에 더 좋을 수 있다.
            3) 문제는 Projection을 하면 Join Fetch를 할 수 없다는 점이다.
            4) 이유는 Fetch Join은 기본적으로 select에 1개 이상의 Entity가 올 수 없다.(Fetch Join 뒤의 Entity에 별칭을 줄 수 없는 이유이기도 하다)
            5) 하지만, QueryDSL에서는 innerJoin() 만으로도 Fetch Join으로 생성된 쿼리와 같은 쿼리 실행이 가능하다.(jpa03-model02 참고)
        - 테스트 실행과 쿼리 로그 
            
            ```
                select
                    board0_.no as col_0_0_,
                    board0_.hit as col_1_0_,
                    board0_.title as col_2_0_,
                    board0_.contents as col_3_0_,
                    board0_.reg_date as col_4_0_,
                    user1_.name as col_5_0_ 
                from
                    board board0_ 
                inner join
                    user user1_ 
                        on board0_.user_no=user1_.no 
                order by
                    board0_.reg_date desc limit ?                  
            ```
          
    5) test05BoardViewLazyProblem(01~04)
        - 이 테스트도 **성능 및 비지니스 요구 따른 레포지토리 메소드 최적화 하기** 를 다루는 테스트이다.
        - 이 테스트에서는 가능한 쿼리 한방으로 게시글보기 화면에 맞는 데이터를 다 갖추도록 노력하겠지만, 성능과 최적화를 다루면서 유의 할 점이 있다.
            + 복잡한 조인 쿼리가 여러번 나눠서 가져오는 select(Lazy)보다 성능을 보장할 수 없다는 것
            + 너무 화면에 맞춘 레포지토리 메소드 작성은 Repository 계층이 Presentation(View) 계층과 과한 결합을 할 수 있다는 점이다.
            + Service 계층에 적당한 Lazy를 사용해서 Presentation(View)에 맞는 DTO를 제공하는 것이 맞는 경우도 있다.
        - 앞의 test03BoardListLazyProblem는 게시판 리스트에서 Board->User ManyToOne 매핑시 기본페치 전략을 EAGER -> LAZY로 수정 시 발생하는 문제를 다루었다면
        - 이 테스트는 기본페치 전략 EAGER -> LAZY로 변경 문제에다가 Board->Comment OneToMany 기본페치 전략 LAZY에 대한 문제을 복합적으로 다루어야 한다.
        - Comment 엔티티가 추가되면서 게시판 글보기 비즈니스 로직 관련 엔티티가 Board, User, Comment 3개가 되었기 때문이다.
        - 문제는 이미 해결되어 있다.
            + ManyToOne Bidirectional 에서 One쪽 Collection Fetch Join을 다루고 해결방법을 설명하였다.(jpa03-model03 참고) 
            + jpa03-model03의 JpaUserQryDslRepositoryImpl.findOrdersByNo() 에서는 selectDistinct(), fetchJoin()을 사용해서 이 문제를 해결했다.
            + User는 Board로 Order는 Comment로 바꿔보면 쉽게 이해할 수 있을 것이다.
            + Board->User 기본페치 전략 EAGER -> LAZY 변경으로 발생한 문제는 test03BoardListLazyProblem에서 해결한 방법을 적용하면 된다.
            + 당연히, QueryDSL 통합하는 방식으로 문제 해결을 해야 한다.(기본메소드, 쿼리메소드로는 성능문제를 해결할 수 없다는 점을 빨리 깨달으면 깨달을 수록 좋다. 그렇다고 무용지물은 아니다. 둘을 어디에 써야 하는가 아는 것도 JPA에 경험이 조금 필요하다.)
        - 문제를 하나 하나씩 해결해 보자.
            + test05BoardViewLazyProblem01 - 기본메소드 findByID(no)
        
            ```
            
            ```
          
        - jpa03-mode02에서는 Board, User 2개의 엔티티만 있을 때 findBy2(no)로 Lazy로딩 문제를 해결할 수 있었다.
        - jpa03-mode04에서는 새로운 Comment Entity가 Board와의 OneToMany Unidirectional 관계로 등장하여 findBy2(no)의 새로운 성능 이슈 문제를 일으켰다.
        - 성능 및 비지니스 요구 따른 레포지토리 메소드 최적화를 수행해야 하는 것은 개발중에 발생할 수도 있지만 서비스 중에도 발생할 수 있다.
        - 문제는 서비스 중에 비지니스의 요구 사항의 변경이 발생하면 이런 이슈를 발견 못할 가능성이 많다. 따라서 JPA는 사전에 충분한 경험이 있는 팀이 수행해서 사전에 성능 이슈를 발견하고 최적화 작업을 하는 것이 맞다.
        - 그리고 쿼리 로그를 계속 모니터링 하는 것도 언급할 필요없이 중요하다.  

JPA는 개별적 모델에 대한 전체적인 모델 매핑 지식, 다양한 객체지향쿼리 작성법 숙지, 영속성컨텍스트와 트랜잭션 개념과 SQL에 대한 이해와 RDBMS 스키마 모델링 경험등 이런 지식과 경험이 축적되어야 한다.
  
#### 2-4. Spring Data JPA CommentRepository Test : Spring Data JPA 기반 Repository
1. __JpaCommentRepository.java__
    1) 기본메소드와 쿼리메소드 

2. __JpaCommentQryDslRepository.java__
    1) 추가/성능 개선이 필요한 쿼리메소드 정의

3. __JpaCommentQryDslRepositoryImp.java__
    1) 추가/성능 개선이 필요한 쿼리메소드 구현

4. __JpaCommentRepositoryTest.java__
