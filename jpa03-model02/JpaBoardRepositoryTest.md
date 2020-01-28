# JpaBoardRepositoryTest

## test02FindById1

- `Eager Fetch` 사용. Proxy 객체 타입을 리턴하지 않는다.

- `Lazy Fetch`는 Proxy 객체를 리턴한다 --> 실제 객체가 아니다.

- `board.getUser()`는 `Left Outer Join`으로 실행된다.



## test02FindById2

- jpql을 사용하면 user 정보를 가져오기 위해 join 대신 select 쿼리를 2번 실행한다.