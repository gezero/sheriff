package net.bitcoinguard.sheriff.core.services;

import net.bitcoinguard.sheriff.core.entities.P2shAddress;

/**
 * Created by Jiri on 7. 7. 2014.
 */
public interface P2shAddressService {
    P2shAddress find(Long id);

    P2shAddress createNew(String publicKey1, String publicKey2);
}
