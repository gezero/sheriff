package net.bitcoinguard.sheriff.core.services.impl;

import net.bitcoinguard.sheriff.core.entities.P2shAddress;
import net.bitcoinguard.sheriff.core.entities.Transaction;
import net.bitcoinguard.sheriff.core.services.P2shAddressesRepositoryCustom;

import java.util.List;

/**
 * Created by Jiri on 11. 7. 2014.
 */
public class P2shAddressesRepositoryImpl implements P2shAddressesRepositoryCustom {
    @Override
    public P2shAddress createNew(List<String> publicKeys) {
        return null;
    }

    @Override
    public Transaction createNewTransaction(Long addressId, String targetAddress, Long amount) {
        return null;
    }
}
