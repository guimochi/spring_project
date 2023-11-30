package vinci.stock.matching.repositories;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import vinci.stock.matching.models.Transaction;

/**
 * Proxy for the execution service.
 */
@Repository
@FeignClient(name = "execution")
public interface ExecutionProxy {

  @PostMapping("/execute/{ticker}/{seller}/{buyer}")
  ResponseEntity<Void> execute(@PathVariable String ticker, @PathVariable String seller,
      @PathVariable String buyer, @RequestBody Transaction transactionDTO);
}
