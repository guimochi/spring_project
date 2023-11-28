package vinci.stock.repositories;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import vinci.stock.dto.Position;

@Repository
@FeignClient(name = "wallet")
public interface WalletProxy {

  @GetMapping("/wallets/{username}")
  Iterable<Position> readOne(@PathVariable String username);
}
