package vinci.stock.gateway.models;

import jakarta.persistence.Enumerated;
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
  @Enumerated
  private Side side;
  @Enumerated
  private Type type;
  private double limit;
  private int filled;
}
