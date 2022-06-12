package by.brel.newsmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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

    @Column(name = "date")
    private LocalDateTime dateCreatedComment;

    @Column(name = "text")
    private String text;

    @Column(name = "user_id")
    private String idUser;

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
