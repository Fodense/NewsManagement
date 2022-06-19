package by.brel.newsmanagement.mapper;

import by.brel.newsmanagement.dto.NewsDto;
import by.brel.newsmanagement.entity.News;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NewsMapper {

    @Autowired
    private ModelMapper modelMapper;

    public NewsDto convertNewsToNewsDto(News news) {
        return modelMapper.map(news, NewsDto.class);
    }

    public News convertNewsDtoToNews(NewsDto newsDto) {
        return modelMapper.map(newsDto, News.class);
    }
}
