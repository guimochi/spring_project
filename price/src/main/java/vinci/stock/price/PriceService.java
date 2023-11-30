package vinci.stock.price;

import java.util.Optional;
import org.springframework.stereotype.Service;
import vinci.stock.price.entities.Price;
import vinci.stock.price.repositories.PriceRepository;

@Service
public class PriceService {
  private final PriceRepository repository;

  public PriceService(PriceRepository repository) {
    this.repository = repository;
  }

  /**
   * Get the price of a position
   * @param ticker Ticker of the position
   * @return Price of the position
   */
  public int getOne(String ticker) {
    Optional<Price> p = repository.findById(ticker);
    return p.map(Price::getPrice).orElse(1);
  }

  /**
   * Update the price of a position
   * If the position does not exist, it is created
   * @param p Position with updated price
   */
  public void updateOne(Price p) {
    repository.save(p);
  }
}
