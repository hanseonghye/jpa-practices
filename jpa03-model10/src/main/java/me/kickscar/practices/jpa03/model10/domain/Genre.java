package me.kickscar.practices.jpa03.model10.domain;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "genre")
public class Genre {
    @Id
    @Column(name = "no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "abbr_name", nullable = false, length = 5)
    private String abbrName;

    @ManyToMany(mappedBy = "genres", cascade = CascadeType.REMOVE)
    private List<Song> songs = new ArrayList<>();

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

    public String getAbbrName() {
        return abbrName;
    }

    public void setAbbrName(String abbrName) {
        this.abbrName = abbrName;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }


    @Override
    public String toString() {
        return "Genre{" +
                "no=" + no +
                ", name='" + name + '\'' +
                ", abbrName='" + abbrName + '\'' +
                // Lazy Loading Test
                // ", songs=" + songs +
                '}';
    }
}
