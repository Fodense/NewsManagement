package by.brel.newsmanagement.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsDto implements Serializable {

    private Long idNews;

    @JsonFormat(pattern = "dd-MM-yyyy hh:mm:ss")
    private Date dateCreatedNews;

    private String title;

    private String text;

    private List<CommentDto> commentList;
}
