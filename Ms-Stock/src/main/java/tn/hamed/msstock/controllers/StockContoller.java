package tn.hamed.msstock.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.hamed.msstock.services.IStockServices;
import tn.hamed.msstock.entities.Stock;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("stock")
public class StockContoller {
    private final IStockServices stockServices;
    @PatchMapping("{id}")
    Stock updateArticle(
            @RequestBody Map<Object, Object> fields,
            @PathVariable Long id
    ) {
        return stockServices.updateStock(fields, id);
    }

    @PostMapping("/add")
    public Stock addArticle(@RequestBody Stock c) {
        return stockServices.addArticle(c);
    }
    @GetMapping("/getStock/{id}")
    public Stock findStockById(@PathVariable Long id) {
        return stockServices.findStockById(id);
    }

}
