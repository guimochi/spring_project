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

  @PostMapping("/match/{ticker}")
  public ResponseEntity<Void> match(@PathVariable String ticker) {
    service.match(ticker);
    return ResponseEntity.ok().build();
  }
}
