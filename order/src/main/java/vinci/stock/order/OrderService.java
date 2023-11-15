package vinci.stock.order;

import java.util.UUID;
import org.springframework.stereotype.Service;
import vinci.stock.order.models.Order;
import vinci.stock.order.repositories.OrderRepository;

@Service
public class OrderService {

  private final OrderRepository repository;

  public OrderService(OrderRepository repository) {
    this.repository = repository;
  }

  public Order readOne(String guid) {
    UUID uuid = UUID.fromString(guid);
    return repository.findById(uuid).orElse(null);
  }

  public Order updateOne(String guid, int filled) {
    UUID uuid = UUID.fromString(guid);
    Order order = repository.findById(uuid).orElse(null);
    if (order == null) {
      return null;
    }
    order.setFilled(filled);
    repository.save(order);
    return order;
  }

  public Iterable<Order> readAllByUser(String username) {
    return repository.findByOwner(username);
  }

  public Iterable<Order> readAllOpenByTickerAndSide(String ticker, String side) {
    return repository.findByTickerAndSideWhereIsOpen(ticker, side);
  }
}
