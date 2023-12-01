package vinci.stock.wallet.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vinci.stock.wallet.entities.Wallet;

import java.util.List;

@Repository
public interface WalletRepository extends CrudRepository<Wallet, Long> {
    List<Wallet> findByOwner(String username);

    @Query("SELECT w FROM wallets w WHERE w.owner = ?1 AND w.quantity > 0")
    List<Wallet> findOpenByOwner(String username);

}
