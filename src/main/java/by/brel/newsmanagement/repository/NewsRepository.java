package by.brel.newsmanagement.repository;

import by.brel.newsmanagement.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    List<News> findAllByTitleContainingOrTextContaining(String title, String text);
}
