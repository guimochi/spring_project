package vinci.stock.matching.repositories;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import vinci.stock.matching.models.Order;
import vinci.stock.matching.models.Side;

/**
 * Proxy for the order service.
 */
@Repository
@FeignClient(name = "order")
public interface OrderProxy {

  @GetMapping("/order/open/by-ticker/{ticker}/{side}")
  ResponseEntity<Iterable<Order>> readAllOpenByTickerAndSide(@PathVariable String ticker,
      @PathVariable Side side);
}

