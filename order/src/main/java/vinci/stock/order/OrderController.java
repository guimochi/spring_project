package vinci.stock.order;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import vinci.stock.order.models.FilledUpdateRequest;
import vinci.stock.order.models.Order;
import vinci.stock.order.models.Side;
import vinci.stock.order.repositories.MatchingProxy;

/**
 * Controller for orders.
 */
@RestController
public class OrderController {

  private final OrderService service;
  private final MatchingProxy matchingProxy;

  /**
   * Constructor for OrderController.
   *
   * @param service       the service to use
   * @param matchingProxy the proxy for the matching service
   */
  public OrderController(OrderService service, MatchingProxy matchingProxy) {
    this.service = service;
    this.matchingProxy = matchingProxy;
  }

  /**
   * Create a new order.
   *
   * @param order the order to create
   * @return response entity with the created order if ok, bad request if not
   */
  @PostMapping("/order")
  public ResponseEntity<Order> createOne(@RequestBody Order order) {
    if (order == null || !order.checkValid()) {
      return ResponseEntity.badRequest().build();
    }
    Order createdOrder = service.createOne(order);
    matchingProxy.trigger(createdOrder.getTicker());
//  The order created might have been modified by the matching engine, so we retrieve it again
    Order retrievedOrder = service.readOne(createdOrder.getGuid());
    return ResponseEntity.ok(retrievedOrder);
  }

  /**
   * Read one order by its guid.
   * @param guid
   * @return response entity with the order if found, not found if not
   */
  @GetMapping("/order/{guid}")
  public ResponseEntity<Order> readOne(@PathVariable String guid) {
    Order order = service.readOne(guid);
    if (order == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(order);
  }

  /**
   * Update the filled quantity of an order. This method does not check if the quantity is valid
   * not if it exceeds quantity.
   * @param guid the guid of the order to update
   * @param request the request containing a FilledUpdateRequest object with the quantity to add
   * @return response entity with ok if updated, not found if not
   */
  @PatchMapping("/order/{guid}")
  public ResponseEntity<Void> addFilledQuantity(@PathVariable String guid,
      @RequestBody FilledUpdateRequest request) {
    boolean updated = service.addFilledQuantity(guid, request.getFilled());
    if (!updated) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok().build();
  }

  /**
   * Read all orders by a user.
   *
   * <p>Note: if the user does not exist, an empty list is returned. This is different from the
   * yaml but it is a deliberate choice after talking about it with the professor as to not call
   * UserProxy </p>
   * @param username the username of the user
   * @return response entity with the list of orders if found, empty list if not
   */
  @GetMapping("/order/by-user/{username}")
  public ResponseEntity<Iterable<Order>> readAllByUser(@PathVariable String username) {
    Iterable<Order> orders = service.readAllByUser(username);
    return ResponseEntity.ok(orders);
  }

  /**
   * Read all open orders(where filled < quantity) by ticker and side
   * @param ticker the ticker of the orders
   * @param side the side of the orders
   * @return response entity with the list of orders if found, empty list if not
   */
  @GetMapping("/order/open/by-ticker/{ticker}/{side}")
  public ResponseEntity<Iterable<Order>> readAllOpenByTickerAndSide(@PathVariable String ticker,
      @PathVariable Side side) {
    Iterable<Order> orders = service.readAllOpenByTickerAndSide(ticker, side);
    return ResponseEntity.ok(orders);
  }
}
