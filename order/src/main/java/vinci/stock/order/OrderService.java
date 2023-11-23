package vinci.stock.order;

import org.springframework.stereotype.Service;
import vinci.stock.order.models.Order;
import vinci.stock.order.models.Side;
import vinci.stock.order.repositories.OrderRepository;

@Service
public class OrderService {

  private final OrderRepository repository;

  public OrderService(OrderRepository repository) {
    this.repository = repository;
  }

  public Order readOne(String guid) {
    return repository.findById(guid).orElse(null);
  }

  public boolean addFilledQuantity(String guid, int filled) {
    Order order = repository.findById(guid).orElse(null);
    if (order == null) {
      return false;
    }
    order.setFilled(order.getFilled() + filled);
    repository.save(order);
    return true;
  }

  public Iterable<Order> readAllByUser(String username) {
    return repository.findByOwner(username);
  }

  public Iterable<Order> readAllOpenByTickerAndSide(String ticker, Side side) {
    return repository.findByTickerAndSideWhereIsOpen(ticker, side);
  }

  public Order createOne(Order order) {
    return repository.save(order);
  }
}
