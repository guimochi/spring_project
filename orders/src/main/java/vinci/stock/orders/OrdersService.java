package vinci.stock.orders;

import org.springframework.stereotype.Service;
import vinci.stock.orders.models.Order;
import vinci.stock.orders.repositories.OrdersRepository;

@Service
public class OrdersService {

  private final OrdersRepository repository;

  public OrdersService(OrdersRepository repository) {
    this.repository = repository;
  }

  public Order readOne(String guid) {
    return repository.findById(guid).orElse(null);
  }

  public boolean patchOne(String guid, int filled) {
    Order order = repository.findById(guid).orElse(null);
    if (order == null) {
      return false;
    }
    order.setFilled(filled);
    repository.save(order);
    return true;
  }

  public Iterable<Order> readAllByUser(String username) {
    return repository.findByOwner(username);
  }

  public Iterable<Order> readAllOpenByTickerAndSide(String ticker, String side) {
    return repository.findByTickerAndSideWhereIsOpen(ticker, side);
  }

  public Order createOne(Order order) {
    return repository.save(order);
  }
}
