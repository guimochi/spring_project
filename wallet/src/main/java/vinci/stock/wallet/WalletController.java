package vinci.stock.wallet;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vinci.stock.wallet.dto.Position;

import java.util.List;

@RestController
public class WalletController {
    private final WalletService service;

    public WalletController(WalletService service) {
        this.service = service;
    }

    @GetMapping("/wallet/{username}/net-worth")
    public ResponseEntity<Double> getNetWorth(@PathVariable String username) {
        double netWorth = service.getNetWorth(username);
        if (netWorth == Integer.MIN_VALUE) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(netWorth);
    }

    @GetMapping("/wallet/{username}")
    public ResponseEntity<Iterable<Position>> openWalletByUser(@PathVariable String username) {
        Iterable<Position> positions = service.openWalletByUser(username);
        if (positions == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(positions);
        }
    }

    @PostMapping("/wallet/{username}")
    public ResponseEntity<Iterable<Position>> createOrUpdateWallets(@RequestBody Iterable<Position> positions,@PathVariable String username) {
        if (positions == null) {
            return ResponseEntity.badRequest().build();
        }

        Iterable<Position> updatedPositions = service.createOrUpdateWallets((List<Position>) positions, username);
        if (updatedPositions == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedPositions);
    }

}
