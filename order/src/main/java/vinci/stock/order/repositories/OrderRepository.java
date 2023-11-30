package vinci.stock.order.repositories;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vinci.stock.order.models.Order;
import vinci.stock.order.models.Side;

/**
 * Repository for orders.
 */
@Repository
public interface OrderRepository extends CrudRepository<Order, String> {

  Iterable<Order> findByOwner(String username);

  /**
   * Find all open orders by ticker and side.
   *
   * @param ticker the ticker of the orders to find
   * @param side   the side of the orders to find
   * @return the orders if found, empty iterable if not
   */
  @Query("SELECT o FROM orders o WHERE o.ticker = ?1 AND o.side = ?2 AND o.filled < o.quantity")
  Iterable<Order> findByTickerAndSideWhereIsOpen(String ticker, Side side);


}
