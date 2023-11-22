package vinci.stock.price.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="price")
public class Price {
  @Id
  @Column(nullable=false, length=4)
  private String ticker;
  @Column(nullable=false)
  private int price;
}
