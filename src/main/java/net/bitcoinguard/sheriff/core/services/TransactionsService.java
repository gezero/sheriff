package net.bitcoinguard.sheriff.core.services;

import net.bitcoinguard.sheriff.core.entities.Transaction;

/**
 * Created by Jiri on 9. 7. 2014.
 */
public interface TransactionsService {
    public Transaction findOne(Long id);
}
