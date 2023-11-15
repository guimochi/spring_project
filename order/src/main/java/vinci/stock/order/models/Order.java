package vinci.stock.order.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.util.UUID;
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
  @GeneratedValue()
  @Column(nullable = false)
  private UUID guid;

  @Column(nullable = false)
  private String owner;

  @Column(nullable = false)
  private int timestamp;

  @Column(nullable = false)
  private String ticker;

  @Column(nullable = false)
  private int quantity;

  @Column(nullable = false)
  private String side;

  @Column(nullable = false)
  private String type;

  private int limit;

  private int filled;
}
