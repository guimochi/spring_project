package vinci.stock.gateway.data;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import vinci.stock.gateway.models.Order;

@Repository
@FeignClient(name="order")
public interface OrderProxy {
  @PostMapping("/order")
  Order createOne(Order order);

  @GetMapping("/order/by-user/{username}")
  Iterable<Order> readByUser(@PathVariable String username);
}
