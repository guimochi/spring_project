package vinci.stock.price.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vinci.stock.price.entities.Price;

@Repository
public interface PriceRepository extends CrudRepository<Price, String> {

}
