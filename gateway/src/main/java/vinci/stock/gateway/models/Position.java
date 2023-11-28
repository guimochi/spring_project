package vinci.stock.gateway.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Position {
  private String ticker;
  private int quantity;
  private int unitValue;
}
