package vinci.stock.execution.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    private String ticker;
    private String seller;
    private String buyer;
    @JsonProperty("sell_order_guid")
    private String sellOrderGuid;
    @JsonProperty("buy_order_guid")
    private String buyOrderGuid;
    private int quantity;
    private int price;

    public boolean checkTransaction() {
        return checkNull() && checkPositive() && checkEmptyString();
    }

    private boolean checkEmptyString() {
        return (!ticker.isEmpty() || !ticker.isBlank()) && (!seller.isEmpty() || !seller.isBlank()) && (!buyer.isEmpty() || !buyer.isBlank()) && (!sellOrderGuid.isEmpty() || !sellOrderGuid.isBlank()) && (!buyOrderGuid.isEmpty() || !buyOrderGuid.isBlank());
    }

    private boolean checkNull() {
        return ticker != null && seller != null && buyer != null && sellOrderGuid != null && buyOrderGuid != null;
    }

    private boolean checkPositive() {
        return quantity > 0 && price > 0;
    }
}
