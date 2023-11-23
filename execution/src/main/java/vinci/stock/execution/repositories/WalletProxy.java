package vinci.stock.execution.repositories;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import vinci.stock.execution.dto.Position;

import java.util.List;

@Repository
@FeignClient(name = "wallet")
public interface WalletProxy {

    @GetMapping("/wallet/{username}")
    void getOne(@PathVariable String username);

    @PostMapping("/wallet/{username}")
    void updateOne(@PathVariable String username, @RequestBody List<Position> position);
}
