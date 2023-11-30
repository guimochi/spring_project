package vinci.stock.execution.repositories;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Repository
@FeignClient(name = "price")
public interface PriceProxy {

    @GetMapping("/price/{ticker}")
    Integer getOne(@PathVariable String ticker);

    @PatchMapping("/price/{ticker}")
    void updateOne(@PathVariable String ticker, @RequestBody int price);
}
