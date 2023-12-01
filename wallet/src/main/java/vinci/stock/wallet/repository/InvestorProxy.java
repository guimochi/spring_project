package vinci.stock.wallet.repository;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import vinci.stock.wallet.dto.Investor;

@Repository
@FeignClient(name = "investor")
public interface InvestorProxy {
    @GetMapping("/investor/{id}")
    Investor getInvestor(@PathVariable String id);
}
