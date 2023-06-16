package tn.hamed.msstock.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import tn.hamed.msstock.entities.Stock;
import tn.hamed.msstock.repositories.StockRepositories;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StockServiceImp implements IStockServices {
    private final StockRepositories stockRepositories;
    @Override
    public Stock addArticle(Stock article) {
        article.setCreatedAt(LocalDate.now());
        return stockRepositories.save(article);
    }

    @Override
    public Stock updateStock(Map<Object, Object> fields, Long id) {
        Optional<Stock> article = stockRepositories.findById(id);
        Assert.isTrue(article.isPresent(), "There's no stock for this ID!");
        fields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Stock.class, key.toString());
            field.setAccessible(true);
            ReflectionUtils.setField(field, article.get(), value);
        });
        return stockRepositories.save(article.get());
    }

    @Override
    public void removeStock(Long articleId) {
        stockRepositories.deleteById(articleId);
    }

    @Override
    public Stock findStockById(Long articleId) {
        Optional<Stock> article = stockRepositories.findById(articleId);
        Assert.isTrue(article.isPresent(), "There's no stock for this ID!");
        return article.get();
    }

    @Override
    public List<Stock> getAllStocks() {
        return stockRepositories.findAll();
    }
}
