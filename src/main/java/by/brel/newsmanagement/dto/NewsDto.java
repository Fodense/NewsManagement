package by.brel.newsmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private Long idNews;

    private LocalDateTime dateCreatedNews;

    private String title;

    private String text;

    private List<CommentDto> commentList;
}
