package by.brel.newsmanagement.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto implements Serializable {

    private Long idComment;

    private LocalDateTime dateCreatedComment;

    private String text;

    private String idUser;

    @JsonIgnore
    private Long idNews;
}
