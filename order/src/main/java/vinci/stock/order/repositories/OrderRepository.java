package vinci.stock.order.repositories;

import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vinci.stock.order.models.Order;

@Repository
public interface OrderRepository extends CrudRepository<Order, UUID> {

  Iterable<Order> findByOwner(String username);

  @Query("SELECT o FROM orders o WHERE o.ticker = ?1 AND o.side = ?2 AND o.filled < o.quantity")
  Iterable<Order> findByTickerAndSideWhereIsOpen(String ticker, String side);


}
