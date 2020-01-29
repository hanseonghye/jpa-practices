# 단방향 OneToMany

## 게시글(board)의 댓글(comment)

- 보통 게시글 보기 페이지에서 게시글에 달린 댓글을 본다. 따라서 게시글 -> 댓글 의 관계는 1:N이 된다.

## 엔티티

- `toMany` 필드의 타입으로 `Map`, `Set` , `List`, `Collection`등을 사용하게 된다.

```java
@OneToMany(fetch = FetchType.LAZY)
@JoinColumn(name = "board_no")
private List<Comment> comments;
```

### 특이점

- 외래키 관기를 Manny(comment)쪽에서 해야하는데 단방향이기 때문에, Comment에 Board를 참조하는 매핑 필드가 없다.
- 관계 주인 필드인 Board.comments가 외래키 관리를 해야하고 `@JoinColumn name`에 Comment 엔티티의 FK(board_no)를 지정해야 한다.

### 주의 할 점

- 단점 인지
  - 외래키 관리를 다른 테이블에서 한다. -> 이는 다른 테이블에 fk가 있으면 insert작업시 추가적으로 update를 해야 한다.
- 단방향 OneToMany를 써야 한다면 양방향 ManyToOne을 보통 추천한다고 한다?!!? (양방향 OneToMany == 양방향 ManyToOne)