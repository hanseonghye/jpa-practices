# JpaBoardRepositoryTest

### test01Save()

> Many쪽인 comment 엔티티가 외래키 관리를 하지 않기 때문에, insert후, fk update를 해야하며, 반대편 board 엔티티를 통해 한다.

실제 `save()`메소드는 `JpaCommentQryDslRepositoryImpl` 클래스에 있다.

```java
    @Override
    public void save(Long boardNo, Comment ...comments) {
        EntityManager em = getEntityManager();

        Board board = em.find(Board.class, boardNo);

        for(Comment comment :  comments) {
            em.persist(comment);
            board.getComments().add(comment);
        }
    }
```

- `comment` 엔티티에는 `board`의 key값을 세팅할 필드가 없다.
- 따라서 위 `save` 메소드에서는  `comment`를 insert하고, `board`를 update한다.

- 이를 양방향 ManyToOne 으로 바꿧을때 예상되는 코드

  ```java
  Comment comment = new Comment();
  comment.setBoard(board);
  comment.setUSer(user);
  comment.setContents("댓글");
  
  commentRepository.save(comment);
  ```

  

## test02SaveEagerProblem01()

```java
// Comment.java

	...

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn( name = "user_no" )
    private User user;
```

`Comment` 엔티티에서 `user`을 조인할때 `EAGER Loading`을 사용한다.

따라서 comment를 select하면 user 또한 join해서 select한다.

user 정보가 필요없을 때조차, user 정보를 가져올 필요는 없다. 따라서 이를 Lazy loading으로 변경하자.

```java
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn( name = "user_no" )
    private User user;
```



## test03BoardListLazyProblem

위에서 `comment` 엔티티에서 `user`가져오는 부분을 LAZY로 바꿧다. 그런데 이제 user를 가져오기 위한 select가 추가적으로  발생하는 문제가 생긴다.

모든 `board`를 select하는 쿼리 `1번`과 각 게시글에 있는 `user`를 가져오기위한  select 쿼리 `N번`이  수행되는 `N+1`문제가 발생한다.



## test04BoardListLazyProblemSolved

> 위의 N+1 문제를 해결하자~