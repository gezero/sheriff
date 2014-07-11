package net.bitcoinguard.sheriff.core.services;

import net.bitcoinguard.sheriff.core.entities.P2shAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Jiri on 7. 7. 2014.
 */
@Repository
public interface P2shAddressesRepository extends JpaRepository<P2shAddress, Long>, P2shAddressesRepositoryCustom {
    P2shAddress findByAddress(String address);
}
