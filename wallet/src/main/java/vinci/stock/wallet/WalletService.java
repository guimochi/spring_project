package vinci.stock.wallet;

import org.springframework.stereotype.Service;
import vinci.stock.wallet.dto.Position;
import vinci.stock.wallet.entities.Wallet;
import vinci.stock.wallet.repository.PriceProxy;
import vinci.stock.wallet.repository.WalletRepository;

import java.util.List;

@Service
public class WalletService {
    private final WalletRepository repository;
    private final PriceProxy priceProxy;

    public WalletService(WalletRepository repository, PriceProxy priceProxy) {
        this.repository = repository;
        this.priceProxy = priceProxy;
    }

    public double getNetWorth(String username) {
        Iterable<Wallet> wallet = repository.findByOwner(username);
        if (wallet == null) {
            return Integer.MIN_VALUE;
        }
        double netWorth = 0;
        for (Wallet w : wallet) {
            netWorth += w.getQuantity() * priceProxy.getPriceFromStock(w.getTicker()) ;
        }
        return netWorth;
    }

    public List<Position> openWalletByUser(String username) {
        Iterable<Wallet> wallet = repository.findOpenByOwner(username);
        if (wallet == null) {
            return null;
        }
        List<Position> positions = new java.util.ArrayList<>();
        for (Wallet w : wallet) {
            Position p = new Position();
            p.setTicker(w.getTicker());
            p.setQuantity(w.getQuantity());
            p.setUnitValue(priceProxy.getPriceFromStock(w.getTicker()));
            positions.add(p);
        }
        return positions;
    }

    public List<Position> createOrUpdateWallets(List<Position> positions, String username) {
        List<Wallet> wallet = repository.findByOwner(username);
        for (Position p : positions) {
            Wallet w = wallet.stream().filter(x -> x.getTicker().equals(p.getTicker())).findFirst().orElse(null);
            if (w == null) {
                w = new Wallet();
                w.setOwner(username);
                w.setTicker(p.getTicker());
                w.setQuantity(p.getQuantity());
            } else {
                w.setQuantity(w.getQuantity() + p.getQuantity());
            }
            repository.save(w);
        }
        wallet = repository.findByOwner(username);
        positions.clear();
        for (Wallet w : wallet) {
            Position p = new Position();
            p.setTicker(w.getTicker());
            p.setQuantity(w.getQuantity());
            positions.add(p);
        }
        return positions;
    }
}
