package vinci.stock.gateway.data;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import vinci.stock.gateway.models.InvestorData;

@Repository
@FeignClient(name="investor")
public interface InvestorProxy {
  @GetMapping("/investor/{username}")
  InvestorData readOne(@PathVariable String username);
}
