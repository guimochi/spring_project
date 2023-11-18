package vinci.stock.matching.models;

import java.sql.Timestamp;
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

  private Timestamp timestamp;

  private String ticker;

  private int quantity;

  private String side;

  private String type;

  private int limit;

  private int filled;

  public boolean valid() {
    if (this.getGuid() != null) {
      return false;
    }
    if (this.getOwner() == null || this.getOwner().isEmpty()) {
      return false;
    }
    if (this.getTimestamp() == null) {
      return false;
    }
    if (this.getTicker() == null || this.getTicker().isEmpty()) {
      return false;
    }
    if (this.getSide() == null || this.getSide().isEmpty()) {
      return false;
    }
    if (this.getType() == null || this.getType().isEmpty()) {
      return false;
    }
    if (this.getQuantity() <= 0) {
      return false;
    }
    if (this.getFilled() < 0) {
      return false;
    }
    try {
      Side.valueOf(this.getSide());
      Type.valueOf(this.getType());
    } catch (IllegalArgumentException e) {
      return false;
    }
    if (Type.valueOf(this.getType()) == Type.LIMIT && this.getLimit() <= 0) {
      return false;
    }
    return true;
  }


  /**
   * Compares the order. Check the type first. Return the market order Then check side. If buy,
   * return the lowest If sell, return the highest In case of tie, return the oldest
   *
   * @param o the object to be compared.
   * @return
   */
  @Override
  public int compareTo(Order o) {
    if (Type.valueOf(this.getType()) == Type.MARKET && Type.valueOf(o.getType()) == Type.MARKET) {
      return this.getTimestamp().compareTo(o.getTimestamp());
    }
    if (Type.valueOf(o.getType()) == Type.MARKET) {
      return 1;
    }
    if (Type.valueOf(this.getType()) == Type.MARKET) {
      return -1;
    }
    if (Side.valueOf(this.getSide()) == Side.BUY) {
      int compare = Integer.compare(this.getLimit(), o.getLimit());
      if (compare != 0) {
        return compare;
      }
      return this.getTimestamp().compareTo(o.getTimestamp());
    }
    int compare = Integer.compare(o.getLimit(), this.getLimit());
    if (compare != 0) {
      return compare;
    }
    return this.getTimestamp().compareTo(o.getTimestamp());
  }
}
