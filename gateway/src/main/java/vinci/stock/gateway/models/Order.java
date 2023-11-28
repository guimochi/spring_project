package vinci.stock.gateway.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Order {
  private String guid;
  private String owner;
  private int timestamp;
  private String ticker;
  private int quantity;
  private OrderSide orderSide;
  private OrderType orderType;
  private int limitPrice;
  private int filled;
}
