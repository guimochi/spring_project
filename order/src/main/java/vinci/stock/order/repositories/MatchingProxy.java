package vinci.stock.order.repositories;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Proxy for matching service.
 */
@Repository
@FeignClient(name = "matching")
public interface MatchingProxy {

  @PostMapping("/trigger/{ticker}")
  ResponseEntity<Void> trigger(@PathVariable String ticker);
}
