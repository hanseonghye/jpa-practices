## Model05 : 일대다(OneToMany) - 양방향


### 1. Domain

#### 1-1. 테이블 연관관계 VS 객체 연관관계

<img src="http://assets.kickscar.me:8080/markdown/jpa-practices/35001.png" width="500px" />
<br>

<img src="http://assets.kickscar.me:8080/markdown/jpa-practices/35002.png" width="500px" />
<br>

1. __OneToMany 양방향(Bidirectioanl)은 존재하지 않는다__
    1) OneToMany 양방향(Bidirectinal)은 ManyToOne 양방향(Bidirectional)과 완전 동일하다. 
    2) ManyToOne 양방향(Bidirectional)을 사용해야 한다.
    3) 보통은 앞에를 연관관계의 주인으로 인지들 하기 때문에 OneToMany 양방향(Bidirectinal)에서는 One쪽이 주인이 되어야 하는데 이는 RDBMS 특성상 불가능하기 때문에 존재하지 않는다.
    4) 존재하지 않는 이유는 보통 외래키를 관리하는 곳이 연관관계의 주인인데 외래키는 Many쪽에 있기 때문이다.
        
2. __보통은 서비스에서 방향성을 찾는 경우가 많다.__
    1) 회원의 경우, 마이페이지에서 자신의 주문리스트를 확인해 보는 서비스의 비지니스 로직이 필요하다. (User -> Orders, Navigational Association) 
    2) 반대방향을 보면, User <- Orders 도 당연히 탐색(Navigational)이 가능해야 한다. 
    3) 따라서 양방향(Bidirectioanl)이다.
   
3. __양방향에서는 연관관계의 주인을 따져야 한다.__
    1) user 필드(FK)가 있는 Orders 엔티티가 이 관계의 주인이 된다.
    2) 하지만 OneToMany 양방향(Bidirectional)에서는 User 엔티티가 중인이 되어야 하지만 RDBMS와 특성과의 차이로 주인이 될 수 없다.
    3) 대신, 양쪽에 @JoinColumn의 이름을 같게 하고 Many쪽의 관계 필드(user)는 읽기 전용으로 한다.
    4) **OneToMany 단방향(Unidirectional)에 반대편 Many에 읽기전용 연관 필드를 하나더 두는 관계** 라 볼수 있다.
    5) OneToMany Unidirectional의 저장 문제점을 그대로 가지고 있어 추천하지 않는다.
    6) 사용하는 경우를 억지로 찾자면, OSIV에서 주문 조회 페이지 같은 View에서 주문자 변경 가능성을 차단할 수는 있겠다.
    7) 그런데, ManyToOne 양방향에서도 설정할 수 있기 때문에 그렇게 꼭 OneToMany 양방향만의 장점이라 볼 수 없다.
    8) **반드시 OneToMany 양방향(Bidirectioanal) 보다는 ManyToOne 양방향(Bidirectional) 사용을 권고한다.**
    
4. __생성 스키마 DDL__    
    <img src="http://assets.kickscar.me:8080/markdown/jpa-practices/35002.png" width="600px" />
    <br>
    1) OneToMany 단방향과 같다.
    2) Many쪽에 지정한 이름으로 FK가 설정되어 있음을 알 수 있다.

#### 1-2. Entity Class: User, Orders
1. __User 엔티티 매핑 참고__
2. __Orders 엔티티 매핑 참고__
3. __연관관계 매핑__
    1) OneToMany(User 엔티티)
        
        ```
            .
            .
            @OneToMany(fetch = FetchType.LAZY)   
            @JoinColumn( name = "user_no" )
            private List<Orders> orders = new ArrayList<Orders>(); 
            .
            .
        ```
        - OneToMany 에서는 Default Fetch Mode는 LAZY로 유지한다.
      
    2) ManyToOne(Orders 엔티티)  
        
        ```
             .
             .
            @ManyToOne(fetch = FetchType.LAZY)
            @JoinColumn(name = "user_no", insertable = false, updatable = false)
            private User user;
             .
             .
        ```
        - ManytoOne, OneToOne에서 Default Fetch Mode는 EAGER (Global Fetch 전략 LAZY) 로 수정  
      
    3) 객체 관계 설정에 주의 할점


### 2. SpringBoot Test Case

