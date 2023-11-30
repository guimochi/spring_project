package vinci.stock.matching;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for the matching service.
 */
@RestController
public class MatchingController {

  private final MatchingService service;

  /**
   * Constructor.
   *
   * @param service the matching service
   */
  public MatchingController(MatchingService service) {
    this.service = service;
  }

  /**
   * Triggers the matching service for the given ticker.
   *
   * @param ticker the ticker
   * @return a response entity
   */
  @PostMapping("/trigger/{ticker}")
  public ResponseEntity<Void> trigger(@PathVariable String ticker) {
    service.trigger(ticker);
    return ResponseEntity.ok().build();
  }
}
