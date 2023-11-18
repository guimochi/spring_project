package vinci.stock.matching;

import org.springframework.stereotype.Service;
import vinci.stock.matching.models.Order;
import vinci.stock.matching.models.Side;
import vinci.stock.matching.repositories.ExecutionProxy;
import vinci.stock.matching.repositories.OrderProxy;

@Service
public class MatchingService {

  private final OrderProxy orderProxy;
  private final ExecutionProxy executionProxy;

  public MatchingService(OrderProxy orderProxy, ExecutionProxy executionProxy) {
    this.orderProxy = orderProxy;
    this.executionProxy = executionProxy;
  }

  public void match(String ticker) {
    Iterable<Order> ordersSideBuy = orderProxy.readAllOpenByTickerAndSide(ticker,
        Side.BUY.toString()).getBody();

//    PriorityQueue<Order> ordersSideBuySorted = new PriorityQueue<Order>();
//    assert ordersSideBuy != null;
//    for (Order order : ordersSideBuy) {
//      ordersSideBuySorted.add(order);
//    }
    Iterable<Order> ordersSideSell = orderProxy.readAllOpenByTickerAndSide(ticker,
        Side.SELL.toString()).getBody();
//    TODO: implement matching algorithm
  }
}
