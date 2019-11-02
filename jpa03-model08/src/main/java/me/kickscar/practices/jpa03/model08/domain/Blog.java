package me.kickscar.practices.jpa03.model08.domain;


import javax.persistence.*;

@Entity
@Table(name = "blog")
public class Blog {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name="name", nullable=false, length=200)
    private String name;

    @MapsId
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="id")
    private User user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
