package vinci.stock.matching.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
   * Compares the order. Check the type first. Return the market order Then check side. If buy,
   * return the lowest If sell, return the highest In case of tie, return the oldest
   *
   * @param o the object to be compared.
   * @return
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

  public boolean checkOpen() {
    return this.getFilled() < this.getQuantity();
  }
}
