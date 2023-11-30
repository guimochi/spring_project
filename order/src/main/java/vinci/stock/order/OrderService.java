package vinci.stock.order;

import org.springframework.stereotype.Service;
import vinci.stock.order.models.Order;
import vinci.stock.order.models.Side;
import vinci.stock.order.repositories.OrderRepository;

/**
 * Service for orders.
 */
@Service
public class OrderService {

  private final OrderRepository repository;

  /**
   * Constructor for OrderService.
   *
   * @param repository the repository to use
   */
  public OrderService(OrderRepository repository) {
    this.repository = repository;
  }

  /**
   * Read one order by its guid.
   * @param guid the guid of the order to read
   * @return the order if found, null if not
   */
  public Order readOne(String guid) {
    return repository.findById(guid).orElse(null);
  }

  /**
   * Add a quantity to the filled quantity of an order.
   * @param guid the guid of the order to update
   * @param filled the quantity to add
   * @return true if the order was found and updated, false if not
   */
  public boolean addFilledQuantity(String guid, int filled) {
    Order order = repository.findById(guid).orElse(null);
    if (order == null) {
      return false;
    }
    order.setFilled(order.getFilled() + filled);
    repository.save(order);
    return true;
  }

  /**
   * Read all orders by their owner.
   * @param username the username of the owner
   * @return the orders if found, empty iterable if not
   */
  public Iterable<Order> readAllByUser(String username) {
    return repository.findByOwner(username);
  }

  /**
   * Read all open orders(where filled < quantity) by ticker and side.
   * @param ticker the ticker of the orders
   * @param side the side of the orders
   * @return the orders if found, empty iterable if not
   */
  public Iterable<Order> readAllOpenByTickerAndSide(String ticker, Side side) {
    return repository.findByTickerAndSideWhereIsOpen(ticker, side);
  }

  /**
   * Create an order.
   * @param order the order to create
   * @return the created order
   */
  public Order createOne(Order order) {
    return repository.save(order);
  }
}
