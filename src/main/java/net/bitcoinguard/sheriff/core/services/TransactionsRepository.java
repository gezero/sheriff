package net.bitcoinguard.sheriff.core.services;

import net.bitcoinguard.sheriff.core.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Jiri on 9. 7. 2014.
 */
public interface TransactionsRepository extends JpaRepository<Transaction, Long>{
    public Transaction findOne(Long id);
}
