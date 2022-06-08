package by.brel.newsmanagement.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "comment")
@Data
public class Comment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comment")
    private Long idComment;

    @Column(name = "date")
    private Date dateCreatedComment;

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
