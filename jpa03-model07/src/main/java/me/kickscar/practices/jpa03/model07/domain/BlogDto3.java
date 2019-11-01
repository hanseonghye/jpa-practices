package me.kickscar.practices.jpa03.model07.domain;

import com.querydsl.core.annotations.QueryProjection;

public class BlogDto3 {
    private Long no;
    private String name;
    private String userId;

    public BlogDto3(){
    }

    @QueryProjection
    public BlogDto3(Long no, String name, String userId){
        this.no = no;
        this.name = name;
        this.userId = userId;
    }

    public Long getNo() {
        return no;
    }

    public void setNo(Long no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "BlogDto3{" +
                "no=" + no +
                ", name='" + name + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
