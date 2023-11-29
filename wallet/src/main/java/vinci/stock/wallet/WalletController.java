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
        return ResponseEntity.ok(netWorth);
    }

    //get all open positions for a given user
    @GetMapping("/wallet/{username}")
    public ResponseEntity<Iterable<Position>> openWalletByUser(@PathVariable String username) {
        Iterable<Position> positions = service.openWalletByUser(username);
        return ResponseEntity.ok(positions);
    }

    @PostMapping("/wallet/{username}")
    public ResponseEntity<Iterable<Position>> createOrUpdateWallets(@RequestBody Iterable<Position> positions,@PathVariable String username) {
        Iterable<Position> updatedPositions = service.createOrUpdateWallets((List<Position>) positions, username);
        return ResponseEntity.ok(updatedPositions);
    }

}
