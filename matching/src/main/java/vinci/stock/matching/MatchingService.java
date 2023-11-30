package vinci.stock.matching;

import java.util.PriorityQueue;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vinci.stock.matching.models.Order;
import vinci.stock.matching.models.Side;
import vinci.stock.matching.models.Transaction;
import vinci.stock.matching.models.Type;
import vinci.stock.matching.repositories.ExecutionProxy;
import vinci.stock.matching.repositories.OrderProxy;
import vinci.stock.matching.repositories.PriceProxy;

/**
 * Service for the matching service.
 */
@Service
public class MatchingService {

  private final OrderProxy orderProxy;
  private final ExecutionProxy executionProxy;
  private final PriceProxy priceProxy;
  private final Logger logger = Logger.getLogger("matchingLogger");

  /**
   * Constructor.
   *
   * @param orderProxy     the order proxy
   * @param executionProxy the execution proxy
   * @param priceProxy     the price proxy
   */
  public MatchingService(OrderProxy orderProxy, ExecutionProxy executionProxy,
      PriceProxy priceProxy) {
    this.orderProxy = orderProxy;
    this.executionProxy = executionProxy;
    this.priceProxy = priceProxy;
  }

  /**
   * Triggers the matching service for the given ticker.
   *
   * <p>Note: the matching may match the buy and sell from the same owner. This is not a mistake.
   * There is no rule in yaml concerning this case so it is ignored.</p>
   *
   * @param ticker the ticker
   */
  public void trigger(String ticker) {
    Iterable<Order> ordersSideBuy = orderProxy.readAllOpenByTickerAndSide(ticker,
        Side.BUY).getBody();
    assert ordersSideBuy != null;
//  Create queue sorted by match first, then get the higher price and finally timestamp
    PriorityQueue<Order> ordersBuy = new PriorityQueue<Order>();
    for (Order order : ordersSideBuy) {
      ordersBuy.add(order);
    }
    Iterable<Order> ordersSideSell = orderProxy.readAllOpenByTickerAndSide(ticker,
        Side.SELL).getBody();
    assert ordersSideSell != null;
//  Create queue sorted by match first, then get the lower price and finally timestamp
    PriorityQueue<Order> ordersSell = new PriorityQueue<Order>();
    for (Order order : ordersSideSell) {
      ordersSell.add(order);
    }

    Transaction transaction = new Transaction();
    while (true) {
//      Get first of each
      Order buy = ordersBuy.poll();
      Order sell = ordersSell.poll();
//      Check if either is null, we can stop
      if (sell == null || buy == null) {
        break;
      }
//      get owner
      String buyer = buy.getOwner();
      String seller = sell.getOwner();

//      Check lower quantity available
      int buyLeft = buy.getQuantity() - buy.getFilled();
      int sellLeft = sell.getQuantity() - sell.getFilled();
      int quantity = Integer.min(buyLeft, sellLeft);

//      Set up transaction
      transaction.setTicker(ticker);
      transaction.setBuyer(buyer);
      transaction.setSeller(seller);
      transaction.setBuy_order_guid(buy.getGuid());
      transaction.setSell_order_guid(sell.getGuid());
      transaction.setQuantity(quantity);

//      Set up price
      double price;
//      Check both limit
      if (buy.getType() == Type.LIMIT && sell.getType() == Type.LIMIT) {
        if (sell.getLimit() > buy.getLimit()) {
          break;
        }
        price = (sell.getLimit() + buy.getLimit()) / 2;
//        buy limit and sell market
      } else if (buy.getType() == Type.LIMIT) {
        price = buy.getLimit();
//        sell limit and buy market
      } else if (sell.getType() == Type.LIMIT) {
        price = sell.getLimit();
//        both market - we need to get price from price service
      } else {
        ResponseEntity<Double> responsePrice = priceProxy.getPrice(ticker);
        if (responsePrice.getBody() == null) {
          logger.error("Error in priceProxy getPrice, body is null");
          break;
        }
        price = responsePrice.getBody();
      }
      transaction.setPrice(price);

//      Execute transaction
      ResponseEntity<Void> response = executionProxy.execute(ticker, seller, buyer, transaction);
      if (response.getStatusCode() != HttpStatus.OK) {
        Logger errorLogger = Logger.getLogger("errorLogger");
        errorLogger.error("Error in executionProxy execute");
        break;
      }

//      Update order
      buy.setFilled(buy.getFilled() + quantity);
      sell.setFilled(sell.getFilled() + quantity);
//      Put them back in queue if they are still open
      if (buy.checkOpen()) {
        ordersBuy.add(buy);
      }
      if (sell.checkOpen()) {
        ordersSell.add(sell);
      }
    }
  }
}
