package vinci.stock.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vinci.stock.entities.Investor;

@Repository
public interface InvestorRepository extends CrudRepository<Investor, String> {

}
