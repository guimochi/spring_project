package vinci.stock.execution;

import feign.FeignException;
import org.springframework.stereotype.Service;
import vinci.stock.execution.dto.FilledUpdateRequest;
import vinci.stock.execution.dto.Position;
import vinci.stock.execution.dto.Transaction;
import vinci.stock.execution.repositories.OrderProxy;
import vinci.stock.execution.repositories.PriceProxy;
import vinci.stock.execution.repositories.WalletProxy;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExecutionService {
    private final PriceProxy priceProxy;
    private final OrderProxy orderProxy;
    private final WalletProxy walletProxy;

    public ExecutionService(PriceProxy priceProxy, OrderProxy orderProxy, WalletProxy walletProxy) {
        this.priceProxy = priceProxy;
        this.orderProxy = orderProxy;
        this.walletProxy = walletProxy;
    }

    public boolean executeOrder(Transaction transaction) {
        List<Position> positionsSeller = new ArrayList<>();
        positionsSeller.add(new Position(transaction.getSeller(), "CASH", transaction.getPrice() * transaction.getQuantity(), 1));
        positionsSeller.add(new Position(transaction.getSeller(), transaction.getTicker(), transaction.getQuantity(), transaction.getPrice()));
        List<Position> positionsBuyer = new ArrayList<>();
        positionsBuyer.add(new Position(transaction.getBuyer(), "CASH", -transaction.getPrice() * transaction.getQuantity(), 1));
        positionsBuyer.add(new Position(transaction.getBuyer(), transaction.getTicker(), -transaction.getQuantity(), transaction.getPrice()));

        try {
            //walletProxy.updateOne(transaction.getSeller(), positionsSeller); TODO
            //walletProxy.updateOne(transaction.getBuyer(), positionsBuyer); TODO
            orderProxy.updateOne(transaction.getSellOrderGuid(), new FilledUpdateRequest(transaction.getQuantity()));
            orderProxy.updateOne(transaction.getBuyOrderGuid(), new FilledUpdateRequest(transaction.getQuantity()));
            priceProxy.updateOne(transaction.getTicker(), transaction.getPrice());
            return true;
        } catch (FeignException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }


}
