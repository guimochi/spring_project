package vinci.stock;

import feign.FeignException;
import org.springframework.stereotype.Service;
import vinci.stock.dto.Credentials;
import vinci.stock.dto.InvestorData;
import vinci.stock.dto.Position;
import vinci.stock.entities.Investor;
import vinci.stock.repositories.AuthentificationProxy;
import vinci.stock.repositories.InvestorRepository;
import vinci.stock.repositories.WalletProxy;

import java.util.Optional;

@Service
public class InvestorService {

    private final InvestorRepository investorRepository;
    private final AuthentificationProxy authentificationProxy;
    private final WalletProxy walletProxy;

    public InvestorService(InvestorRepository investorRepository,
                           AuthentificationProxy authentificationProxy, WalletProxy walletProxy) {
        this.investorRepository = investorRepository;
        this.authentificationProxy = authentificationProxy;
        this.walletProxy = walletProxy;
    }


    /**
     * @param investorData Investor to create
     * @return true if the investor was created, false if another investor exists with the same
     * username or the investor is invalid
     */
    public boolean createOne(InvestorData investorData, String password) {
        if (investorRepository.existsById(investorData.getUsername())) {
            return false;
        }
        try {
            authentificationProxy.createCredentials(investorData.getUsername(), new Credentials(investorData.getUsername(), password));
        } catch (FeignException e) {
            return false;
        }
        Optional<Investor> investor = investorData.toInvestor();

        return investor.map(value -> {
            investorRepository.save(value);
            return true;
        }).orElse(false);
    }

    /**
     * @param username the username of the investor to read
     * @return an Optional of the investor if it exists, otherwise an empty Optional
     */
    public Optional<Investor> readOne(String username) {
        return investorRepository.findById(username);
    }

    /**
     * @param investor the investor to update
     * @return true if the investor was updated, false if the investor doesn't exist
     */
    public boolean updateOne(Investor investor) {
        if (!investorRepository.existsById(investor.getUsername())) {
            return false;
        }
        investorRepository.save(investor);
        return true;
    }

    /**
     * @param username the username of the investor to delete
     * @return true if the investor was deleted, false if the investor doesn't exist or has a wallet
     */
    public boolean deleteOne(String username) {
        if (!investorRepository.existsById(username)) {
            return false;
        }

        try {
            if (this.walletProxy.readOne(username).iterator().hasNext()) {
                return false;
            }
            authentificationProxy.deleteCredentials(username);
        } catch (FeignException e) {
            return false;
        }
        investorRepository.deleteById(username);
        return true;
    }
}
