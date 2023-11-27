package vinci.stock.gateway.data;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;

@Repository
@FeignClient(name="wallet")
public interface WalletProxy {
}
