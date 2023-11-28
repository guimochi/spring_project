package vinci.stock.gateway.data;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import vinci.stock.gateway.models.InvestorData;
import vinci.stock.gateway.models.InvestorWithPassword;

@Repository
@FeignClient(name="investor")
public interface InvestorProxy {
  @GetMapping("/investor/{username}")
  InvestorData readOne(@PathVariable String username);

  @PostMapping("/investor/{username}")
  void createOne(@PathVariable String username, InvestorWithPassword investorWithPassword);

  @PutMapping("/investor/{username}")
  void updateOne(@PathVariable String username, InvestorData investorData);

  @DeleteMapping("/investor/{username}")
  void deleteOne(@PathVariable String username);
}
