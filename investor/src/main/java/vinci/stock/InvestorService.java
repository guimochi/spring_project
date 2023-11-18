package vinci.stock;

import feign.FeignException;
import org.springframework.stereotype.Service;
import vinci.stock.dto.InvestorData;
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

    public InvestorService(InvestorRepository investorRepository, AuthentificationProxy authentificationProxy, WalletProxy walletProxy) {
        this.investorRepository = investorRepository;
        this.authentificationProxy = authentificationProxy;
        this.walletProxy = walletProxy;
    }


    /**
     * @param investorData Investor to create
     * @return true if the investor was created, false if another investor exists with the same username or the investor is invalid
     */
    public boolean createOne(InvestorData investorData, String password) {
        if (investorRepository.existsById(investorData.getUsername())) return false;
        try {
            //authentificationProxy.createCredentials(investorWithPassword.getUsername(), new Credentials(investorWithPassword.getUsername(), password));
        } catch (FeignException e) {
            return false;
        }
        investorRepository.save(investorData.toInvestor());
        return true;
    }

    public Optional<Investor> readOne(String username) {
        return investorRepository.findById(username);
    }

    public boolean updateOne(Investor investor) {
        if (!investor.isValid()) return false;
        if (!investorRepository.existsById(investor.getUsername())) return false;
        investorRepository.save(investor);
        return true;
    }

    public boolean deleteOne(String username) {
        if (!investorRepository.existsById(username)) return false;
        if (this.walletProxy.readOne(username).iterator().hasNext()) return false;
        try {
            authentificationProxy.deleteCredentials(username);
        } catch (FeignException e) {
            return false;
        }
        investorRepository.deleteById(username);
        return true;
    }
}
