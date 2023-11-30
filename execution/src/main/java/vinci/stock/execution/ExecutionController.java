package vinci.stock.execution;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import vinci.stock.execution.dto.Transaction;

import java.util.Objects;

@RestController
public class ExecutionController {

    private final ExecutionService executionService;

    public ExecutionController(ExecutionService executionService) {
        this.executionService = executionService;
    }

    @PostMapping("/execute/{ticker}/{seller}/{buyer}")
    public ResponseEntity<Void> executeOrder(@PathVariable String ticker, @PathVariable String seller,
                                             @PathVariable String buyer, @RequestBody Transaction transaction) {
        if (!Objects.equals(transaction.getTicker(), ticker) || !Objects.equals(transaction.getSeller(),
                seller) || !Objects.equals(transaction.getBuyer(), buyer)) {
            return ResponseEntity.badRequest().build();
        }
        if (!transaction.checkTransaction()) {
            return ResponseEntity.badRequest().build();
        }
        if (executionService.executeOrder(transaction)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

}
