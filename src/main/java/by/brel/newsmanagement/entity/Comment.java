package by.brel.newsmanagement.entity;

import by.brel.newsmanagement.entity.news.News;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comment")
    private Long idComment;

    @NotNull
    @Column(name = "date")
    private LocalDateTime dateCreatedComment;

    @NotNull
    @Size(max = 2046)
    @Column(name = "text")
    private String text;

    @NotNull
    @Size(max = 127)
    @Column(name = "user_id")
    private String idUser;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "news_id")
    private News news;

    @Override
    public String toString() {
        return "Comment{" +
                "idComment=" + idComment +
                ", dateCreatedComment=" + dateCreatedComment +
                ", text='" + text + '\'' +
                ", idUser='" + idUser + '\'' +
                '}';
    }
}
