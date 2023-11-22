package vinci.stock.matching;

import java.util.PriorityQueue;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vinci.stock.matching.models.Order;
import vinci.stock.matching.models.Side;
import vinci.stock.matching.models.Transaction;
import vinci.stock.matching.models.Type;
import vinci.stock.matching.repositories.ExecutionProxy;
import vinci.stock.matching.repositories.OrderProxy;
import vinci.stock.matching.repositories.PriceProxy;

@Service
public class MatchingService {

  private final OrderProxy orderProxy;
  private final ExecutionProxy executionProxy;
  private final PriceProxy priceProxy;

  public MatchingService(OrderProxy orderProxy, ExecutionProxy executionProxy,
      PriceProxy priceProxy) {
    this.orderProxy = orderProxy;
    this.executionProxy = executionProxy;
    this.priceProxy = priceProxy;
  }

  public void match(String ticker) {
    Iterable<Order> ordersSideBuy = orderProxy.readAllOpenByTickerAndSide(ticker,
        Side.BUY).getBody();
//  Create queue sorted by match first, then get the higher price and finally timestamp
    PriorityQueue<Order> ordersBuy = new PriorityQueue<Order>();
    assert ordersSideBuy != null;
    for (Order order : ordersSideBuy) {
      ordersBuy.add(order);
    }
    Iterable<Order> ordersSideSell = orderProxy.readAllOpenByTickerAndSide(ticker,
        Side.SELL).getBody();
//  Create queue sorted by match first, then get the lower price and finally timestamp
    PriorityQueue<Order> ordersSell = new PriorityQueue<Order>();
    assert ordersSideSell != null;
    for (Order order : ordersSideSell) {
      ordersSell.add(order);
    }

    Transaction transaction = new Transaction();
    while (true) {
//      Get first of each
      Order buy = ordersBuy.poll();
      Order sell = ordersSell.poll();
      if (sell == null || buy == null) {
        break;
      }
      String buyer = buy.getOwner();
      String seller = sell.getOwner();

//      Check lower quantity available
      int buyLeft = buy.getQuantity() - buy.getFilled();
      int sellLeft = sell.getQuantity() - sell.getFilled();

      int quantity = Integer.min(buyLeft, sellLeft);
//      setup the common part of transaction
      transaction.setTicker(ticker);
      transaction.setBuyer(buyer);
      transaction.setSeller(seller);
      transaction.setBuy_order_guid(buy.getGuid());
      transaction.setSell_order_guid(sell.getGuid());
      transaction.setQuantity(quantity);

//      Check both market
      if (buy.getType() == Type.MARKET && sell.getType() == Type.MARKET) {
        ResponseEntity<Double> responcePrice = priceProxy.getPrice(ticker);
        double price = responcePrice.getBody();
        transaction.setPrice(price);
        executionProxy.execute(ticker, seller, buyer, transaction);
      }
//      Check buy market
      if (buy.getType() == Type.MARKET) {
        double price = sell.getLimit();
        transaction.setPrice(price);
        executionProxy.execute(ticker, seller, buyer, transaction);
      }
//      Check sell market
      if (sell.getType() == Type.MARKET) {
        double price = buy.getLimit();
        transaction.setPrice(price);
        executionProxy.execute(ticker, seller, buyer, transaction);
//        both limit
      } else {
        if (sell.getLimit() > buy.getLimit()) {
          break;
        }
        double price = (sell.getLimit() + buy.getLimit()) / 2;
        transaction.setPrice(price);
        executionProxy.execute(ticker, seller, buyer, transaction);
      }
//      update order
      buy.setFilled(buy.getFilled() + quantity);
      sell.setFilled(sell.getFilled() + quantity);
//      put them back in queue if they are still open
      if (buy.checkOpen()) {
        ordersBuy.add(buy);
      }
      if (sell.checkOpen()) {
        ordersSell.add(sell);
      }
    }
  }
}
