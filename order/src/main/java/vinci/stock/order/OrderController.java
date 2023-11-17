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


@RestController
public class OrderController {

  private final OrderService service;

  public OrderController(OrderService service) {
    this.service = service;
  }

  @PostMapping("/order")
  public ResponseEntity<Order> createOne(@RequestBody Order order) {
    if (order == null || !order.valid()) {
      return ResponseEntity.badRequest().build();
    }
    Order createdOrder = service.createOne(order);
    return ResponseEntity.ok(createdOrder);
  }

  @GetMapping("/order/{guid}")
  public ResponseEntity<Order> readOne(@PathVariable String guid) {
    Order order = service.readOne(guid);
    if (order == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(order);
  }

  //permet au système de mettre à jour la quantité d'action qui a déjà été échangée suite à cet
  // ordre.
  @PatchMapping("/order/{guid}")
  public ResponseEntity<Void> patchOne(@PathVariable String guid,
      @RequestBody FilledUpdateRequest request) {
    boolean updated = service.patchOne(guid, request.getFilled());
    if (!updated) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok().build();
  }

  //liste tous les ordres (ouverts et complétés) qui ont été passés par un investisseur donné
  @GetMapping("/order/by-user/{username}")
  public ResponseEntity<Iterable<Order>> readAllByUser(@PathVariable String username) {
    Iterable<Order> orders = service.readAllByUser(username);
    return ResponseEntity.ok(orders);
  }

  @GetMapping("/order/open/by-ticker/{ticker}/{side}")
  public ResponseEntity<Iterable<Order>> readAllOpenByTickerAndSide(@PathVariable String ticker,
      @PathVariable String side) {
    Iterable<Order> orders = service.readAllOpenByTickerAndSide(ticker, side);
    return ResponseEntity.ok(orders);
  }
}
