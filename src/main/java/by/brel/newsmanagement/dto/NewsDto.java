package by.brel.newsmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * News DTO class; With fields: idNews, dateCreatedNews, title, text, commentList
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsDto implements Serializable {

    @NotNull
    private Long idNews;

    @NotNull
    private LocalDateTime dateCreatedNews;

    @NotNull
    @Size(max = 255)
    private String title;

    @NotNull
    @Size(max = 2046)
    private String text;

    @Valid
    private List<CommentDto> commentList;
}
