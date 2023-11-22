package vinci.stock.matching.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

  private String ticker;
  private String seller;
  private String buyer;
  private String buy_order_guid;
  private String sell_order_guid;
  private int quantity;
  private double price;
}
