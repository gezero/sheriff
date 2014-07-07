package net.bitcoinguard.sheriff.core.services;

import net.bitcoinguard.sheriff.core.entities.P2shAddressRename;

/**
 * Created by Jiri on 7. 7. 2014.
 */
public interface P2shAddressService {
    P2shAddressRename find(Long id);
}
