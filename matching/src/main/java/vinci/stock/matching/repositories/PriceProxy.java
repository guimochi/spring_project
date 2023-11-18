package vinci.stock.matching.repositories;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Repository
@FeignClient(name = "price")
public interface PriceProxy {

  @GetMapping("/price/{ticker}")
  ResponseEntity<Double> getPrice(@PathVariable String ticker);
}
