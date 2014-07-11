package net.bitcoinguard.sheriff.core.services;

import net.bitcoinguard.sheriff.core.entities.P2shAddress;
import net.bitcoinguard.sheriff.core.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Jiri on 7. 7. 2014.
 */
@Repository
public interface P2shAddressesRepository {
    P2shAddress createNew(List<String> publicKeys);

    Transaction createNewTransaction(Long addressId, String targetAddress, Long amount);

    P2shAddress findOne(String testAddress);
}
