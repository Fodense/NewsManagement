package by.brel.newsmanagement.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto implements Serializable {

    private Long idComment;

    @JsonFormat(pattern = "dd-MM-yyyy hh:mm:ss")
    private Date dateCreatedComment;

    private String text;

    private String idUser;

    @JsonIgnore
    private Long idNews;
}
