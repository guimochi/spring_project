package vinci.stock.authentication;

import vinci.stock.authentication.models.UnsafeCredentials;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
public class AuthenticationController {

    private final AuthenticationService service;

    public AuthenticationController(AuthenticationService service) {
        this.service = service;
    }


    @PostMapping("/authentication/connect")
    public ResponseEntity<String> connect(@RequestBody UnsafeCredentials password) {
        if (password.invalid()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        String token =  service.connect(password);

        if (token == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }


    @PostMapping("/authentication/verify")
    public ResponseEntity<String> verify(@RequestBody String token) {
        String username = service.verify(token);

        if (username == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(username, HttpStatus.OK);
    }


    @PostMapping("/authentication/{username}")
    public ResponseEntity<Void> createOne(@PathVariable String username, @RequestBody UnsafeCredentials password) {
        if (!Objects.equals(password.getUsername(), username)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (password.invalid()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        boolean created = service.createOne(password);

        if (!created) return new ResponseEntity<>(HttpStatus.CONFLICT);
        else return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/authentication/{username}")
    public ResponseEntity<Void> updateOne(@PathVariable String username, @RequestBody UnsafeCredentials password) {
        if (!Objects.equals(password.getUsername(), username)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (password.invalid()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        boolean found = service.updateOne(password);

        if (!found) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/authentication/{username}")
    public ResponseEntity<Void> deleteCredentials(@PathVariable String username) {
        boolean found = service.deleteOne(username);

        if (!found) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(HttpStatus.OK);
    }

}

