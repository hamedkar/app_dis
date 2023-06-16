package tn.hamed.msarticle.controllers;

import dto.ArticleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.hamed.msarticle.services.IArticleService;
import tn.hamed.msarticle.entities.Article;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("article")
public class ArticleController {
    private final IArticleService articleService;

    @PatchMapping("{id}")
    Article updateArticle(
            @RequestBody Map<Object, Object> fields,
            @PathVariable Long id
    ) {
        return articleService.updateArticle(fields, id);
    }

    @PostMapping("/add")
    public Article addArticle(@RequestBody Article c) {
        return articleService.addArticle(c);
    }

    @GetMapping("/find/{id}")
    public ArticleDto findArticleById(@PathVariable Long id) {
        return articleService.findArticleById(id);
    }

}
