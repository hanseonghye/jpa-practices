## Model04 : 일대다(OneToMany) - 단방향


### 01. Domain

#### 1) 테이블 연관관계 VS 객체 연관관계

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


#### 2) Entity Class: Board, Comment

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
          4) 하지만, 스키마 생성 DDL를 보면, Many쪽인 Comment 테이블에 Board에 대한 FK를 둔다(당연하지만;;)  
        
             <img src="http://assets.kickscar.me:8080/markdown/jpa-practices/34004.png" width="500px" />
             <br>
                
          5) @JoinColumn를 하지 않으면 JPA의 Join Table 전략이 적용되어 연결 테이블을 중간에 두고 연관관계를 관리한다.    
          6) OneToMany 에서는 Default Fetch Mode는 LAZY 이다.  
      
      + 객체 관계 설정에 주의 할점
        - 단점을 분명하게 인지할 것: 외래키 관리를 다른 테이블에서 한다. (Many 쪽에서 하지 않는다.) 이는 다른 테이블에 FK가 있으면 Insert 작업 시, 추가적으로 Update를 해야 한다.
        - **OneToMany Unidirectional을 써야 하다면 ManyToOne Bidirectional를 사용하는 것을 보통 추천한다**  


### 02. SpringBoot Test Case


#### 0) 요약: 다루는 기술적 내용

  1. OneToMany Unidirectional 단점 이해
  2. 성능 및 비지니스 요구 따른 레포지토리 메소드 다양화 하기


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


#### 2) Spring Data JPA BoardRepository Test : Spring Data JPA 기반 Repository

  1. __JpaBoardRepository.java__
     + 기본메소드와 쿼리메소드 

  2. __JpaBoardQryDslRepository.java__
     + 추가/성능 개선이 필요한 쿼리메소드 정의

  3. __JpaBoardQryDslRepositoryImp.java__
     + 추가/성능 개선이 필요한 쿼리메소드 구현
       
  4. __JpaBoardRepositoryTest.java__
     + test01Save
     
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
         
         1) Many쪽 Comment 엔티티가 외래키 관리를 하지 않기 때문에 Insert(Save)후, FK Update를 해야하며 반대편 Board 엔티티를 통해 한다.  
         2) 실제 코드는 기본 메소드 save(Entity)를 오버로딩한 JpaCommentQryDslRepositoryImp.save(boardNo, comments)를 구현하였다.  
         3) save(boardNo, comments) 메소드를 보면, Comment 엔티티에 board의 no를 세팅할 필드가 없고 당연히 setter 자체가 없기 때문에 두 개로 나눠 함께 전달한다.  
         4) comments 파라미터는 Variable Arguments(Varargs)를 사용하여 여러 Comment 엔티티 객체를 전달할 수 있도록 하였다.  
       
         ```
         commentRepository.save(1L, new Comment("댓글1"));
         commentRepository.save(2L, new Comment("댓글2"), new Comment("댓글3"));
         
         ```

         5) OneToMain Unidirectional의 단점은 다음 ManyToOne Bidirectional 으로 바꿨을 때 예상되는 코드와 비교해 보면 명확히 알 수 있다.

         ```
         Comment comment = new Comment();
         comment.setBoardNo(1L);
         comment.setContents("댓글");
       
         commentRepository.save(comment);
         
         ```
         6) OneToMain Unidirection의 단점은 다음 ManyToOne Bidirection 으로 바꿨을 때 예상되는 코드와 비교해 보면 명확히 알 수 있다.
       
       
  
#### 3) Spring Data JPA CommentRepository Test : Spring Data JPA 기반 Repository
        
  1. __JpaCommentRepository.java__
     + 기본메소드와 쿼리메소드 

  2. __JpaCommentQryDslRepository.java__
     + 추가/성능 개선이 필요한 쿼리메소드 정의
    
  3. __JpaCommentQryDslRepositoryImp.java__
     + 추가/성능 개선이 필요한 쿼리메소드 구현
       
  4. __JpaCommentRepositoryTest.java__
