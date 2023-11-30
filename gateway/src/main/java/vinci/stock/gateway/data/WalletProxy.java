package vinci.stock.gateway.data;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import vinci.stock.gateway.models.Position;

@Repository
@FeignClient(name="wallet")
public interface WalletProxy {

  @GetMapping("/wallet/{username}")
  Iterable<Position> readWallet(@PathVariable String username);

  @PostMapping("/wallet/{username}")
  Iterable<Position> addPositions(@PathVariable String username, @RequestBody Iterable<Position> positions);

  @GetMapping("/wallet/{username}/net-worth")
  int readNetWorth(@PathVariable String username);
}
