package vinci.stock.execution.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Position {
    private String username;
    private String ticker;
    private int quantity;
    private double unitValue;

}
