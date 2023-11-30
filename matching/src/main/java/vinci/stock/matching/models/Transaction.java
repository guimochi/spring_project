package vinci.stock.matching.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Transaction class
 *
 * <p>Transaction class is the class that represents the transaction to be given for execute. It
 * contains all the information of a transaction.
 */
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
