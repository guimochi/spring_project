package vinci.stock.order.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.sql.Timestamp;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity(name = "orders")
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(nullable = false)
  private String guid;

  @Column(nullable = false)
  private String owner;

  @Column(nullable = false)
  private Timestamp timestamp;

  @Column(nullable = false)
  private String ticker;

  @Column(nullable = false)
  private int quantity;

  @Column(nullable = false)
  private String side;

  @Column(nullable = false)
  private String type;

  @Column(name = "_limit")
  private int limit;

  @Column()
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
}
