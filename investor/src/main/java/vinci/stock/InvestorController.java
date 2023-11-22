package vinci.stock;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vinci.stock.dto.InvestorCreate;
import vinci.stock.dto.InvestorData;
import vinci.stock.entities.Investor;

import java.util.Objects;
import java.util.Optional;


@RestController
public class InvestorController {

    private final InvestorService service;

    public InvestorController(InvestorService service) {
        this.service = service;
    }

    @GetMapping("/investor/{username}")
    public ResponseEntity<Investor> readInvestor(@PathVariable String username) {
        Optional<Investor> investor = service.readOne(username);
        return investor.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/investor/{username}")
    public ResponseEntity<Void> createInvestor(@PathVariable String username, @RequestBody InvestorCreate investor) {

        if (!Objects.equals(investor.getInvestorData().getUsername(), username))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (!investor.getInvestorData().toInvestor().isPresent()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return service.createOne(investor.getInvestorData(), investor.getPassword()) ? new ResponseEntity<>(HttpStatus.CREATED) : new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @PutMapping("/investor/{username}")
    public ResponseEntity<Void> updateInvestor(@PathVariable String username, @RequestBody InvestorData investor) {
        if (!Objects.equals(investor.getUsername(), username)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (investor.toInvestor().isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return service.updateOne(investor.toInvestor().get()) ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/investor/{username}")
    public ResponseEntity<Void> deleteInvestor(@PathVariable String username) {
        return service.deleteOne(username) ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
