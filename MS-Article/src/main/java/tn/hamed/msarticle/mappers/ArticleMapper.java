package tn.hamed.msarticle.mappers;


import dto.ArticleDto;
import dto.StockDto;
import tn.hamed.msarticle.entities.Article;

public class ArticleMapper {

    public static ArticleDto mapToDto(Article article, StockDto stockDto){


        ArticleDto articleDto =  ArticleDto.builder()
                .id(article.getId())
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
                .name(article.getName())
                .qte(article.getQte())
                .stockDto(stockDto)
                .build();

        return articleDto;
    }


    public static Article mapToEntity(ArticleDto articleDto){

        Article article =  Article.builder()
                .qte(articleDto.getQte())
                .name(articleDto.getName())
                .build();

        return article;

    }}