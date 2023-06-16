package tn.hamed.msarticle.services;

import dto.ArticleDto;
import tn.hamed.msarticle.entities.Article;

import java.util.List;
import java.util.Map;

public interface IArticleService {

    Article addArticle(Article article);

    Article updateArticle(Map<Object, Object> fields, Long id);

    void removeArticle(Long articleId);

    ArticleDto findArticleById(Long articleId);
    List<Article> getAllArticles();
}
