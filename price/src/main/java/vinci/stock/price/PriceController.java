package vinci.stock.price;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import vinci.stock.price.entities.Price;

@RestController
public class PriceController {
  private final PriceService priceService;

  public PriceController(PriceService priceService) {
    this.priceService = priceService;
  }

  @GetMapping("/price/{ticker}")
  public ResponseEntity<Integer> getPriceFromStock(@PathVariable String ticker) {
    int price = priceService.getOne(ticker);
    return new ResponseEntity<>(price, HttpStatus.OK);
  }

  @PatchMapping("/price/{ticker}")
  public ResponseEntity<Void> updatePrice(@PathVariable String ticker, @RequestBody int price) {
    if (price <= 0) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    priceService.updateOne(new Price(ticker, price));
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
