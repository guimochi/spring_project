package vinci.stock.wallet.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Position {

    private String ticker;
    private int quantity;
    private double unitValue;


}
