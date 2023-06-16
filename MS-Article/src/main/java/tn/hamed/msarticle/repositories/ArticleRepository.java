package tn.hamed.msarticle.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.hamed.msarticle.entities.Article;

public interface ArticleRepository extends JpaRepository<Article, Long> {

}
