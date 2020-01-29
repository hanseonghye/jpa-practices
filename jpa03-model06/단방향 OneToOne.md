# 단방향 OneToOne

## 관계의 주인 정하기 (주테이블:user vs 대상 테이블:blog)

1. 주 테이블에 외래키를 두는 경우
   - 주테이블에 매핑된 엔티티(user)에서 대상 테이블에 매핑된 엔티티(blog)를 참조할 수 있으므로 편하다.
   - jpa상에서도 마찬가지로 편함
2. 대상 테이블에 외래키를 두는 경우
   - rdb상에서 대상 테이블에 외래키를 둘 수 없다.
   - jpa도 마찬가지로 지원 x
   - 일대일 양방향에서는 가능하다.
   - 장점은 스키마를 유지하면서 OneToMany로 바꿀 수 있다.  ( 블로그릏 한개 이상 개설하는 비지니스로 변경할 경우 쉽게 바꿀수 있게 된다.)



## 엔티티 구현

```java
// user.java
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

- 외래키를 주 테이블 (user)에 두어야 한다.
- OneToOne에서 Default Fetch Mode 는 `LAZY`이다.

```java
// blog.java
     .
     .
    @Id
    @Column(name = "no")
    @GeneratedValue( strategy = GenerationType.IDENTITY  )
    private Long no;
     .
     .
     
```

- OneToOne 단방향에서는 대상테이블에 외래키를 둘 수 없다.