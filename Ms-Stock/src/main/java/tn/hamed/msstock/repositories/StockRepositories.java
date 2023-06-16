package tn.hamed.msstock.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.hamed.msstock.entities.Stock;

public interface StockRepositories extends JpaRepository<Stock, Long> {

}
