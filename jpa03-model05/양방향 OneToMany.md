# 양방향 OneToMany

> 양방향 OneToMany는 rdb상에서 불가능한 모델이다. one 에 해당하는 테이블이 many에 해당하는 테이블의 key를 가질 수 없기 때문이다. 하지만 jpa상에서 이는 구현이 가능하다. 테이블은 단방향 OneToMany와 같다. (ManyTOne 테이블도 동일)

`Order`엔티티와 `User` 엔티티는 모두 서로를 참조 한다. 이때 `Order` 엔티티의 연관필드(user)는 `읽기`권한만 가지게 해야한다. 

이는 `단방향 OneToMany`에 반대편 `Many` 엔티티에 읽기 전용 연관 필드를 두는것이라고 보면 된다.

따라서 `단방향 OneToMany`가 가지고 있는 단점을 그대로 가지고 간다. ➡ `양방향 ManyToOne`을 쓰자!



## test01Save()

단방향 OneToMany 처럼 order 을 insert한 뒤, user_no 컬럼만 따로 update 한다.

```
Hibernate: 
    /* insert me.kickscar.practices.jpa03.model05.domain.Orders
        */ insert 
        into
            orders
            (address, name, reg_date, total_price) 
        values
            (?, ?, ?, ?)
Hibernate: 
    /* create one-to-many row me.kickscar.practices.jpa03.model05.domain.User.orders */ update
        orders 
    set
        user_no=? 
    where
        no=?
```





## test02UpdateUser()

`orders.setUser()` 시켰지만 `Orders`엔티티는 읽기권한만 가진다. 

