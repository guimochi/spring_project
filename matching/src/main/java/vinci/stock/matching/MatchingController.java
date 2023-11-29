package vinci.stock.matching;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MatchingController {

  private final MatchingService service;

  public MatchingController(MatchingService service) {
    this.service = service;
  }

  @PostMapping("/trigger/{ticker}")
  public ResponseEntity<Void> trigger(@PathVariable String ticker) {
    service.trigger(ticker);
    return ResponseEntity.ok().build();
  }
}
