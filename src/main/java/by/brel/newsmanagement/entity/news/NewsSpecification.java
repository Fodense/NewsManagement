package by.brel.newsmanagement.entity.news;

import lombok.Data;
import lombok.Getter;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Data
public class NewsSpecification implements Specification<News> {

    @Getter
    private final String keyWord;

    @Override
    public Predicate toPredicate(Root<News> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        String request = "%" + getKeyWord() + "%";

        return criteriaBuilder.or(
                criteriaBuilder.like(root.get("title"), request),
                criteriaBuilder.like(root.get("text"), request)
        );
    }
}
