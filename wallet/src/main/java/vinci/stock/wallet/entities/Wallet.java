package vinci.stock.wallet.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// create table wallet with (owner varchar(255) primary key, list<position> positions);
@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity(name = "wallets")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long guid;

    @Column(nullable = false)
    private String owner;

    @Column(nullable = false)
    private String ticker;

    @Column(nullable = false)
    private int quantity;

}
