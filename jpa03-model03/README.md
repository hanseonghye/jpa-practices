## Model03 : 다대일(ManyToOne) - 양방향


### 1. Domain

#### 1-1. 테이블 연관관계 VS 객체 연관관계

<img src="http://assets.kickscar.me:8080/markdown/jpa-practices/33001.png" width="500px" />
<br>

<img src="http://assets.kickscar.me:8080/markdown/jpa-practices/33002.png" width="500px" />
<br>
   
1. __보통은 서비스에서 방향성을 찾는 경우가 많다.__
    1) 쇼핑몰 관리자 페이지에서 주문조회에서 주문에 사용자 정보가 나와야 한다. (Orders -> User, Navigational Association)  
    2) 회원의 경우, 마이페이지에서 자신의 주문리스트를 확인해 보는 서비스의 비지니스 로직이 필요하다. (Orders <- User, Navigational Association) 
    3) 쇼핑몰의 주문<->회원 관계매핑은 양방향(Bidirection) 이다.  
   
2. __양방향에서는 연관관계의 주인을 따져야 한다.__
    1) user 필드(FK)가 있는 Orders 엔티티가 이 관계의 주인이 된다.
    2) 이 말은 외래키 세팅을 통한 관계의 변화는  Orders 엔티티의 user 필드를 세팅할때만 변한다는 말이다.(그러니 관계의 주인인 거지!)
    3) 반대편의 List<Orders> 에 아무리 값을 설정해도 무시된다. (여긴 참조만 하는 것이지 관계설정에 아무런 영향을 미치지 못한다. 그래서 주인이 아니다.)
    4) 사실상, 외래키 설정을 하는 ManyToOne 에서 연관관계 설정은 끝난 것이다.
    5) 양방향으로 OneToMany를 하나 더 설정하는 것은 서비스 비즈니스 로직의 필요에 따라 객체 탐색의 편의성 때문에 하는 것이다.

3. __다중성은 방향성이 결정나면 쉽게 결정 할 수 있다.__
    1) 양방향에서는 ManyToOne, OneToMany가 다 존재하지만 관계의 주인이 되는 필드를 설정하는 ManyToOne으로 다중성을 잡는 것이 자연스럽다. 

#### 1-2. Entity Class: User, Orders
1. __User 엔티티 매핑 참고__
2. __Orders 엔티티 매핑 참고__
3. __연관관계 매핑__
    1) ManyToOne(Order 엔티티)
        
        ```
            .
            .
            @ManyToOne(fetch = FetchType.EAGER)   
            @JoinColumn( name = "user_no" )
            private User user;      
            .
            .
        ```
        - ManytoOne, OneToOne에서 Default Fetch Mode는 EAGER 이다.  
      
    2) OneToMany(User 엔티티)  
        
        ```
             .
             .
	    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	    private List<Orders> orders = new ArrayList<Orders>();
             .
             .
        ```
        - mappedBy = "user" 주인이 누구임을 설정하는 부분이다. 반대편 엔티티의 user 필드이다.
        - OneToMany 에서는 Default Fetch Mode는 LAZY 이다.
      
    3) 객체 관계 설정에 주의 할점
        - 영속성과 상관 없이 순수 객체들과의 관계도 고려한 엔티티 클래스 작성을 해야 한다.
        - 양방향 관계이기 때문에 순수 객체에서도 양방향 관계를 맺어주는 것이 좋다.
        - 관계를 맺는 주인 필드가 세팅되는 Orders.setUser() 코드에서 양방향 관계를 맺는 안전한 코드 예시이다.
        
        ```
            public void setUser(User user) {
                this.user = user;

                if(!user.getOrders().contains(this)) {
                    user.getOrders().add(this);
                }
            }
                   
        ``` 
        - toString() 오버라이딩 하는 것도 주의해야 한다.
        
        ```
          public String toString() {
             return "Orders{" +
                "no=" + no +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", totalPrice=" + totalPrice +
                ", regDate=" + regDate +
                // 무한루프조심!!!
                // ", user=" + user +
                '}';
          }         
        ``` 


### 2. SpringBoot Test Case

#### 2-1. 요약: 다루는 기술적 내용
1. QueryDSL 통합하는 Spring Data JPA 기반의 레포지토리 작성방법

2. ManyToOne 양방향(Bidirection) 매핑에서 OneToMany 방향에서는 Collection(List)가 연관필드이기 때문에 Join에서 발생할 수 있는 문제점들...
    1) OneToMany의 켈렉션 조인(inner join, outer join, fetch join)의 문제점 이해 및 해결방법 이해
    2) OneToMany의 Default Fetch Mode인 Lazy Loading 때문에 발생하는 N+1 문제 이해 및 해결방법 이해  
    3) 페이징 자체가 불가능 하다.

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

#### 2-3. pring Data JPA UserRepository Test : Spring Data JPA 기반 Repository
1. __JpaUserRepository.java__
    1) User 엔티티(user 테이블)의 CRUD관련 메소드를 사용할 수 있는 인터페이스다.
    2) 기본메소드
        - 상속 Hierachy:   
            JpaUserRepository -> JpaRepository -> PagingAndSortingRepository -> CrudRepository -> Repository
        - 상속을 통해 상위 인터페이스 JpaRepository, PagingAndSortingRepository, CrudRepositor 들의 메소드들을 별다른 구현없이 사용 가능하다.
    3) 쿼리메소드
        - 메소드 이름기반으로 자동으로 JPQL을 생성하는 메소드가 구현되는데 이를 쿼리 메소드라 한다.
        - findByEmailAndPassword(String, String) 메소드가 그 예이다.

2. __JpaUserQryDslRepository.java__
    1) 기본메소드나 쿼리메소드가 성능이슈가 발생하거나 자동으로 제공받지 못하면 직접 메소드를 직접 만들어 JPQL를 실행해야 한다.
    2) @Query에 JPQL를 직접 넣는 방법이 있으나 문자열 기반이고 기능상 제약이 많다. QueryDSL을 통합하는 방식을 많이 선호한다.
    3) 이를 위해, 구현해야하는 QueryDSL기반 메소드를 정의해 놓은 인터페이스다.
    4) 그리고 애플리케이션에서 직접 사용해야 하는 인터페이스 JpaUserRepository가 이를 상속한다.
    5) 몇가지 메소드의 예를 보면,
        - findById2(no)는 기본메소드 findById(no)의 성능문제와 비즈니스 요구사항 때문에 QueryDSL로 직접 구현해야 하는 메소드이다.
        - update(user)는 영속객체를 사용한 update의 성능문제 때문에 jpa03-model02의 JpaUserRepository에 @Query 어노테이션을 사용해서 JPQL를 직접 사용했는데 메소드 파라미터에 문제가 있어 QueryDSL로 직접 구현해야 하는 메소드이다. 

3. __JpaUserQryDslRepositoryImp.java__
    1) JpaUserQryDslRepository 인터페이스의 메소드를 QueryDSL로 구현한다.
    2) 정리하면,
        - 기본 메소드, 쿼리 메소드의 구현은 Spring Data JPA가 해주는 것이고
        - JpaUserQryDslRepository 인터페이스 구현은 직접 해야 하는 것이다.
        - Spring Data JPA는 JpaUserQryDslRepository 인터페이스의 구현체를 스캔해야하기 때문에 네이밍 규칙이 있다. 인터페이스이름에 **"Impl"** 을 붙힌다.
            
            <img src="http://assets.kickscar.me:8080/markdown/jpa-practices/33003.png" width="500px" />
            <br>
       
4. __JpaUserRepositoryTest.java__
    1) test01Save()
        - 기본 메소드 CrudRepository.save(S)
    2) test02Update
        - QueryDSL 기반 메소드 직접 구현하고 JpaUserRepository 인터페이스와 통합
        - JpaQryDslUserRepositoryImp.update(user)
    3) test03OneToManyCollectionJoinProblem
        - OneToMany Collection Join(inner, outer, fetch)에서 발생하는 문제에 대한 테스트 이다.
        - 테스트 대상 메소드는 JpaUserQryDslRepositoryImpl.findAllCollectionJoinProblem() 메소드다.
        - 기본메소드 findAll()의 Collection Join(inner join)를 추가한 QueryDSL로 작성된 user를 전부 다 찾아주는 메소드다.
        - 테스트 코드 assert 에도 있지만, user 카운트가 orders 카운트와 같은 문제가 있다.
 
            <img src="http://assets.kickscar.me:8080/markdown/jpa-practices/33004.png" width="800px" />
            <br>       
        
            1) User와 Orders가 조인되었기 때문에 연결된 Orders의 개수만큼 User도 나오는 것이 당연하다.
            2) 이는 따지고 보면 문제가 아니다. 관계형데이터베이스와 객체지향프로그래밍 차이에서 발생하는 문제점이라 볼 수 있다.
    4) test04OCollectionJoinProblemSolved
        - Collection Join 문제 해결방법은 의외로 간단하다. distinct를 사용해 관계형데이터베이스에서 문제를 해결한다.
        - findAllCollectionJoinProblemSolved()는 left join을 사용한다.
        - JpaUserQryDslRepositoryImpl.findAllCollectionJoinProblemSolved() 메소드를 보면 QueryDSL의 selectDistinct() 함수로 해결한다.
        - OneToMany Collection 조인 outer, inner, fetch join 모두 해당되는 내용으로 반드시 selectDistinct() 함수를 사용해야 한다.
    5) test05NplusOneProblem
        - N+1 문제를 테스트 한다.
        - 테스트 코드는 총 Orders 카운트를 먼저 가져온 다음, 전체 User List에서 개별 User 객체의 Orders List의 사이즈를 모두 더해 같은 지 보는 것이다.
        - 당연히 같을 것이다.
        - 테스트 통과 조건은 실행된 쿼리수와 전체 User를 가져오기 위한 쿼리수(1)와 Lazy 때문에 각 User 별로 Orders List를 가져오기 위해 실행된 쿼리수(N)과 합이 같은 것이다.
        - 각 User 별로 Orders List를 가져오기 위해 쿼리가 실행됐을 거라 추측할 수 있는 근거를 이해하는 것이 중요하다.
          1) Lazy 때문에 User 엔티티 객체의 List<Orders>는 Proxy 객체로 실제로 DB에서 가져온 Orders가 담긴 List가 아니다.  
          2) Proxy 객체이면 result.size() 또는 result.get(0) 등, Orders 엔티티 객체에 접근하려고 할 때 쿼리가 실행될 것이기 때문에 쿼리수를 카운팅을 할 수 있다.  
          3) 다음은 이 상태임을 체크하는 코드다.  
        
            ```
               if(!em.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(orders)){
                  qryCount++;
               }
                    
            ```     
          4) PersistenceUnitUtil.isLoaded(Entity) 반환이 false이면 초기화 되지 않은 Proxy객체로 지연로딩 중인 것이다.  
        - 테스트 결과는 N+1번으로 쿼리가 실행된 것을 확인할 수 있다. (실제 쿼리로그를 세어 보아도 확인된다.)
    6) test06NplusOneProblemNotSolvedYet
        - Collection Join 문제를 해결한 findAllCollectionJoinProblemSolved() 메소드로 전체 User List를 가져와서 N+1 문제를 검증하는 테스트 코드를 돌려본다.
        - User List의 User의 Orders List의 Orders가 Proxy라면 test05NplusOneProblem() 태스트와 마찬가지로 N+1번 쿼리가 수행됐을 것이다.
        - N+1번 나오기 때문에 아직 문제가 해결되지 못했다.
    7) test07NplusOneProblemSolved
        - N+1 문제를 해결하기 위해  JpaUserQryDslRepositoryImpl.findAllCollectionJoinAndNplusOneProblemSolved() 메소드를 작성했다.
        - 이름은 길지만 innerJoin() + fetchJoin() 으로 작성된 QueryDSL 컬렉션페치조인을 한다.
        - 테스트 통과 조건인 1번 쿼리수가 나왔다!
        - @Transactional 를 사용하지 않은 것도 주목하자. Proxy 객체를 사용하지 않을 때는 영속성이 필요없기 때문에 사용하지 않아도 된다.(영속성과 트랜잭션과의 관계 이해 필요)
        - OneToMany에서 객체그래프를 통해 컬렉션 접근 시, 발생하는 조인문제와 N+1 Lazy 로딩 문제를 해결하기 위해서는 selectDistinct(), fecthJoin()을 사용하면 된다.
        - Lazy로 객체그래프를 통해 컬렉션에 접근하는 것이 좋지 못한 것은 아니다. 상황에 따라서는 성능에 도움이 되는 것도 기억해야 하고 Global Fetch 전략도 실무에서는 보통 LAZY임도 알아야 한다.
        - **N+1 문제는 LAZY, EAGER의 공통문제다. N번 쿼리가 언제 일어나는가? 문제이지 N번 쿼리는 일어난다. 단지, LAZY 보다는 EAGER가 지연로딩을 해도 상관없는 비즈니스나 뷰에 N번을 무조건 하기 때문에 문제가 될 가능성이 많다.**     
    8) test08findOrdersByNo
        - 최적화된 findAllCollectionJoinAndNplusOneProblemSolved() 기반으로 특정 사용자의 주문내역을 조회하는 메소드 findOrdersByNo(no)를 테스트 한다.

            <img src="http://assets.kickscar.me:8080/markdown/jpa-practices/33005.png" width="800px" />
            <br>       
           
        - 쿼리를 보면 만족스럽다.
        - 결과를 유도하는 과정을 이해했으면 페이징 자체가 컬레션조인에서는 의미가 없고 가능하지 않을 것 같다는 느낌이 와야한다.(사실, 페이징 API를 사용하면 무시된다.) 
        - 페이징이 필요하면 반대편 ManyToOne Orders Repository에서 하는 것이 자연스럽고 구현도 가능하다.(Orders Repository에 구현해 놓았다.)

#### 2-4. Spring Data JPA OrdersRepository Test : Spring Data JPA 기반 Repository
1. __JpaOrdersRepository.java__
    1) Orders 엔티티(orders 테이블)의 CRUD관련 메소드를 사용할 수 있는 인터페이스다.
    2) 기본메소드
        - 상속을 통해 상위 인터페이스 JpaRepository, PagingAndSortingRepository, CrudRepositor 들의 메소드들을 별다른 구현없이 사용 가능한 메소드다.
        - 부모 인터페이스의 메소드들을 보면 꽤 많다. 신뢰있는 영속성 엔티티 객체 핸들링을 보장해 준다.
    3) 쿼리메소드 - 당연하지만, 쿼리메소드들도 오버로딩을 사용해서 만들어 낼 수 있다. 
        - findAllByUserNo(userNo)
        - findAllByUserNo(userNo, sort)
        - countAllByUserNo(userNo)

2. __JpaOrdersQryDslRepository.java__
    1) 쿼리메소드 findAllByUserNo()가 2회 쿼리가 실행되는 것에 불만이 있다면 다음 메소드들로 대체할 수 있다.
    2) QueryDSL로 작성했으며 Sort, Pageablefmf QueryDSL로 바꾸는 코드는 볼 만 하다.
        - findAllByUserNo2(userNo);
        - findAllByUserNo2(userNo, sort);
        - findAllByUserNo2(userNo, pageable);
    3) countAllGroupByUser()는 QueryDSL에서 GroupBy를 사용하는 예제 메소드이다. 
    
3. __JpaOrdersQryDslRepositoryImp.java__
    1) JpaUserQryDslRepository 인터페이스의 메소드를 QueryDSL로 구현한다.
       
4. __JpaOrdersRepositoryTest.java__
    1) test01Save()
        - 기본 메소드 CrudRepository.save(S) 테스트 및 테스트 데이터 생성
    2) test02FindAllByUserNo
        - 쿼리메소드 findAllByUserNo(userNo)를 테스트 한다.
        - ManyToOne Fetch는 EAGER가 default이기 때문에 Orders들을 가져온 후, Orders의 user를 세팅하기 위해 select쿼리가 바로 실행된다.
        - Orders를 가져오는 데, outer join을 사용하지만 select에는 user가 없다.
        - PersistenceUnitUtil().isLoaded(Entity)로 영속 Entity 객체임이 바로 확인된다.
    3) test03FindAllByUserNo
        - 쿼리메소드 findAllByUserNo(userNo, sort)를 테스트 한다.
        - test02와 같고 sorting(Order By) 여부만 테스트한다.
        - 테스트 통과 조건에 sorting을 넣지 않았다(쿼리 로그로 Order By절이 추가되었는 지 확인)
    4) test04FindAllByUserNo2
        - test03, test03의 2번 쿼리 실행을 해결한 QueryDSL로 작성한 findAllByUserNo2() 이다.
        - innerJoin()은 조인은 되지만 문제가 select에 User 필드를 포함하지 않는 것이다.
        - fetchJoin()을 함께 해야 한다.
        - 쿼리 확인 할 것
    5) test05FindAllByUserNo2
        - findAllByUserNo2()를 오버로딩해서 Sort를 받아 Order By를 하는 메소드 findAllByUserNo2(userNo, sort)를 테스트 한다.
        - QueryDSL에 Sort를 적용하는 방법 예시다.
        - Order By 필드를 여러 개 적용하는 방법도 테스트 코드에 있다.
        - 외부에서 sorting 필드를 세팅할 수 있는 장점이 있다.
    6) test06FindAllByUserNo2    
        - findAllByUserNo2()를 오버로딩해서 Pageable를 받아 Order By와 limit을 함께 구현한 메소드 findAllByUserNo2(userNo, pageable)를 테스트 한다.
        - QueryDSL에 Pageable를 적용하는 방법 예시다.
        - 외부에서 Paging Size, Index, Sorting Field들을 세팅할 수 있는 장점이 있는 매우 유용한 방법이라 할 수 있다.
    7) test07CountAllGroupByUser
        - QueryDSL의 groupBy() 사용 예시 메소드 countAllGroupByUser()를 테스트 한다.
        - Group By뿐만 아니라 집계함수등이 사용되면(counting만 하는 것은 제외) 일단 Entity를 select에 올릴 수 없기 때문에 Projection과 DTO 사용을 먼저 생각해야 한다.
        - 테스트 통과 조건은 전체 Orders 수를 counting하고 사용자별 Orders 수를 저장한 DTO 들을 순회하면서 더한 값과 같은지 따져보는 것이다.
        - 참고로 Having도 QueryDSL에서 사용 가능하다.