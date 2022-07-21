package by.brel.newsmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "News")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class News implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_news")
    private Long idNews;

    @NotNull
    @Column(name = "date")
    private LocalDateTime dateCreatedNews;

    @NotNull
    @Size(max = 255)
    @Column(name = "title")
    private String title;

    @NotNull
    @Size(max = 2046)
    @Column(name = "text")
    private String text;

    @Valid
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
