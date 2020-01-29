# 양방향 OneToOne

## 엔티티 구현

```java
// user
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

- 단방향 onetoone과 동일

```java
// blog
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

- 대상 테이블에도 주인 테이블을 가리키는 필드가 추가됐다.
- 이때 `mappedBy`를 선언해서 연관관계의 주인이 아님을 말한다.
  - mappedBy를 사용하면 `fetch = FetchType.LAZY`를 무시하고 `Eager`로 로딩한다. 따라서 개선된 메소드를 만들어서 이를 사용해야 한다.

- db 스키마는 단방향 OneToOne과 동일하다.



## test04UpdateUser02

관계의 주인인 `user` 를 통해서는 `blog`를 update 시킬 수 있지만, user에 읽기권한만 있는 `blog`를 통해서는 `user`를 update시킬 수 없다.