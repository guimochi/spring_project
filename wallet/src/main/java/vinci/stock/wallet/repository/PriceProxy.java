package vinci.stock.wallet.repository;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Repository
@FeignClient(name = "price")
public interface PriceProxy {
    @GetMapping("/price/{ticker}")
    int getPriceFromStock(@PathVariable String ticker);
}
