package by.brel.newsmanagement.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "News")
@Data
public class News implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_news")
    private Long idNews;

    @Column(name = "date")
    private LocalDateTime dateCreatedNews;

    @Column(name = "title")
    private String title;

    @Column(name = "text")
    private String text;

    @OneToMany(mappedBy = "news", cascade = CascadeType.ALL)
    private List<Comment> commentList;

    @Override
    public String toString() {
        return "News{" +
                "idNews=" + idNews +
                ", dateCreatedNews=" + dateCreatedNews +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", commentList=" + commentList +
                '}';
    }
}
