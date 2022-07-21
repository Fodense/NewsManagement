package by.brel.newsmanagement.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Comment DTO class; With fields: idComment, dateCreatedComment, text, idUser, idNews
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto implements Serializable {

    @NotNull
    private Long idComment;

    @NotNull
    private LocalDateTime dateCreatedComment;

    @NotNull
    @Size(max = 2046)
    private String text;

    @NotNull
    @Size(max = 127)
    private String idUser;

    @NotNull
    @JsonIgnore
    private Long idNews;
}
