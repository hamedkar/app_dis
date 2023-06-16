package tn.hamed.msstock.services;

import tn.hamed.msstock.entities.Stock;

import java.util.List;
import java.util.Map;

public interface IStockServices {
    Stock addArticle(Stock article);

    Stock updateStock(Map<Object, Object> fields, Long id);

    void removeStock(Long articleId);
    Stock findStockById( Long stockId);

    List<Stock> getAllStocks();
}
