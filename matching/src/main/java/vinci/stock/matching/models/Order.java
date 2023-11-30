package vinci.stock.matching.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Order class
 *
 * <p>Order class is the class that represents the order. It contains all the information of an
 * order.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Order implements Comparable<Order> {

  private String guid;

  private String owner;

  private int timestamp;

  private String ticker;

  private int quantity;

  private Side side;

  private Type type;

  private double limit;

  private int filled;

  /**
   * Compare order to know how to sort them. First, compare the type of order. If both are market,
   * compare the timestamp. If one is market, the other is not, the market order is always first. If
   * both are limit, check the side. If the side is buy, the order with higher limit is first. If
   * the limit is the same, compare the timestamp. If the side is sell, the order with lower limit
   * is first. If the limit is the same, compare the timestamp.
   *
   * @param o the object to be compared.
   * @return a negative integer, zero, or a positive integer
   */
  @Override
  public int compareTo(Order o) {
    if (this.getType() == Type.MARKET && o.getType() == Type.MARKET) {
      return Integer.compare(this.getTimestamp(), (o.getTimestamp()));
    }
    if (o.getType() == Type.MARKET) {
      return 1;
    }
    if (this.getType() == Type.MARKET) {
      return -1;
    }
    if (this.getSide() == Side.BUY) {
      int compare = Double.compare(o.getLimit(), this.getLimit());
      if (compare != 0) {
        return compare;
      }
      return Integer.compare(this.getTimestamp(), (o.getTimestamp()));
    }
    int compare = Double.compare(this.getLimit(), o.getLimit());
    if (compare != 0) {
      return compare;
    }
    return Integer.compare(this.getTimestamp(), (o.getTimestamp()));
  }

  /**
   * Check if the order is open. If the filled quantity is less than the quantity, the order is
   * open.
   *
   * @return true if the order is open, false otherwise.
   */
  public boolean checkOpen() {
    return this.getFilled() < this.getQuantity();
  }
}
