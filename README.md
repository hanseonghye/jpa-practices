## 01. 다양한 매핑 모델들의 JPQL, QueryDSL, JPARepository 기반 Repository 작성 예시


### JPA01. 엔티티 매핑(Entity Mapping)
#### &nbsp;&nbsp;&nbsp;&nbsp;01. Member 엔티티 다양한 설정으로 매핑하기


### JPA02. 영속성 관리 / 엔티티 생명주기
#### &nbsp;&nbsp;&nbsp;&nbsp;01. 영속성 켄텍스트(Persistence Context)
#### &nbsp;&nbsp;&nbsp;&nbsp;02 ~ 04. 엔티티 조회/등록/수정/삭제 
#### &nbsp;&nbsp;&nbsp;&nbsp;05. 준영속성


### JPA03. 다양한 모델 매핑 및 다양한 Repository(JPQL, QueryDSL, JPARepository 기반)들 예제 코드
#### &nbsp;&nbsp;&nbsp;&nbsp;Model01. 단일(One)                      매핑        - 방명록
#### &nbsp;&nbsp;&nbsp;&nbsp;Model02. 다대일(@ManyToOne)              매핑(단방향) - 게시판(Board->User)
#### &nbsp;&nbsp;&nbsp;&nbsp;Model03. 다대일(@ManyToOne)              매핑(양방향) - 쇼핑몰(Order<->User)
#### &nbsp;&nbsp;&nbsp;&nbsp;Model04. 일대다(@OneToMany)              매핑(단방향) - 게시판(Board->Comment)
#### &nbsp;&nbsp;&nbsp;&nbsp;Model05. 일대다(@OneToMany)              매핑(양방향) - 쇼핑몰(User<->Order)
#### &nbsp;&nbsp;&nbsp;&nbsp;Model06. 일대일(@OneToOne)               매핑(단방향) - JBlog(User->Blog)
#### &nbsp;&nbsp;&nbsp;&nbsp;Model07. 일대일(@OneToOne)               매핑(양방향) - JBlog(User<->Blog)
#### &nbsp;&nbsp;&nbsp;&nbsp;Model08. 다대다(@ManyToMany)             매핑(단방향) - 음반검색(Artist->Album)
#### &nbsp;&nbsp;&nbsp;&nbsp;Model09. 다대다(@ManyToMany)             매핑(양방향) - 음반검색(Artis<t->Album)
#### &nbsp;&nbsp;&nbsp;&nbsp;Model10. 다대다(@OneToMany + @ManyToOne) 매핑       - 쇼핑몰(User->Cart->Product)
#### &nbsp;&nbsp;&nbsp;&nbsp;Model11. 다대다(@OneToMany + @OneToMany) 매핑       - 음반검색(Artist->Song<-Album)
#### &nbsp;&nbsp;&nbsp;&nbsp;Model12. 복합키                          매핑       - ?
#### &nbsp;&nbsp;&nbsp;&nbsp;Model13. 식별관계                         매핑       - JBlog


## 02. 프로젝트 로컬 클론 및 모듈 임포트 그리고 실행 환경 설정 방법 (IntelliJ IDEA)
  1) Git Repository URL 선택  - **Clone** 
  <img src="http://assets.kickscar.me:8080/markdown/jpa-practices/00001.png" width="600px" />
  <br/>  
   
  2) Checkout from Version Control - **Yes**  
  <img src="http://assets.kickscar.me:8080/markdown/jpa-practices/00002.png" width="600px" />
  <br/>  
  
  3) Create Project from Existing Sources - **Next**  
  <img src="http://assets.kickscar.me:8080/markdown/jpa-practices/00003.png" width="600px" />
  <br/>  
  
  4) Project Basic Layouts - **Default & Next**  
  <img src="http://assets.kickscar.me:8080/markdown/jpa-practices/00004.png" width="600px" />
  <br/>  
   
  5) Not Empty Folder - **Yes**  
  <img src="http://assets.kickscar.me:8080/markdown/jpa-practices/00005.png" width="600px" />
  <br/>  
  
  6) Project Root Directory 선택 - **Nothing Selected & Finish**  
  <img src="http://assets.kickscar.me:8080/markdown/jpa-practices/00006.png" width="600px" />
  <br/>  
    
  7) Import Module #1  - **Project Structure Window(command + ;)**  
  <img src="http://assets.kickscar.me:8080/markdown/jpa-practices/00007.png" width="600px" />
  <br/>  
  
  8) Import Module #2  
  <img src="http://assets.kickscar.me:8080/markdown/jpa-practices/00008.png" width="600px" />
  <br/>  
  
  9) Project Root Directory 선택 - **OK**  
  <img src="http://assets.kickscar.me:8080/markdown/jpa-practices/00009.png" width="600px" />
  <br/>  

  10) Import Module from External Model - **Gradle & Finish**  
  <img src="http://assets.kickscar.me:8080/markdown/jpa-practices/00010.png" width="600px" />
  <br/>  

  11) Close Project Structure Window - **Apply & OK**  
  <img src="http://assets.kickscar.me:8080/markdown/jpa-practices/00011.png" width="600px" />
  <br/>  
  
  12) Reimport All Gradle Project  
  <img src="http://assets.kickscar.me:8080/markdown/jpa-practices/00018.png" width="600px" />
  <br/>  
  
  13) 잘가져온 모듈 모습!!!
  <img src="http://assets.kickscar.me:8080/markdown/jpa-practices/00013.png" width="600px" />
  <br/>  

  14) Settings(Command + ,) 에서 Intellij IDEA Native 실행 설정 (Default는 gradle 실행) 
  15) *.iml 파일 생성 Diabled  - **Apply & OK** 
  <img src="http://assets.kickscar.me:8080/markdown/jpa-practices/00014.png" width="600px" />
  <br/>
  
  16) 실행하기(Run)  
  <img src="http://assets.kickscar.me:8080/markdown/jpa-practices/00015.png" width="600px" />
  <br/>

  17) [Tip] .ignore 플러그인 설치 - **Restart IDE** 
  <img src="http://assets.kickscar.me:8080/markdown/jpa-practices/00016.png" width="600px" />
  <br/>
  
  18) Hide Ignored files 
  <img src="http://assets.kickscar.me:8080/markdown/jpa-practices/00017.png" width="600px" />
  <br/>

  19) 나머지 모듈도 같은 방식으로... (jpa03 모듈들의 QueryDSL Gradle 플러그인 설정은 각 모듈 README.md 참고)

## 03. 프로젝트 로컬 클론 및 모듈 임포트 그리고 실행 환경 설정 방법 (Eclipse)



 
  