package by.brel.newsmanagement.repository;

import by.brel.newsmanagement.entity.Comment;
import by.brel.newsmanagement.entity.news.News;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllCommentsByNews(News news, Pageable pageable);
}
