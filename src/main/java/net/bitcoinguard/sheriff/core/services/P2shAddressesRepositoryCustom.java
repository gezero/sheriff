package net.bitcoinguard.sheriff.core.services;

import net.bitcoinguard.sheriff.core.entities.P2shAddress;
import net.bitcoinguard.sheriff.core.entities.Transaction;

import java.util.List;

/**
 * Created by Jiri on 11. 7. 2014.
 */
public interface P2shAddressesRepositoryCustom {
    P2shAddress createNew(List<String> publicKeys, Integer requiredKeys);
    Transaction createNewTransaction(Long addressId, String targetAddress, Long amount);
}
