package vinci.stock.order.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
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

  @Enumerated
  @Column(nullable = false)
  private Side side;

  @Enumerated
  @Column(nullable = false)
  private Type type;

  @Column(name = "_limit")
  private double limit;

  @Column()
  private int filled;

  public boolean valid() {
    if (this.getGuid() != null) {
      return false;
    }
    if (this.getOwner() == null || this.getOwner().trim().isEmpty()) {
      return false;
    }
    if (this.getTimestamp() == null) {
      return false;
    }
    if (this.getTicker() == null || this.getTicker().trim().isEmpty()) {
      return false;
    }
    if (this.getSide() == null) {
      return false;
    }
    if (this.getType() == null) {
      return false;
    }
    if (this.getQuantity() <= 0) {
      return false;
    }
    if (this.getFilled() < 0) {
      return false;
    }
    if (this.getType() == Type.LIMIT && this.getLimit() <= 0) {
      return false;
    }
    return true;
  }
}
