package net.bitcoinguard.sheriff.core.services.impl;

import net.bitcoinguard.sheriff.core.entities.Key;
import net.bitcoinguard.sheriff.core.services.KeysRepositoryCustom;
import org.springframework.stereotype.Service;

/**
 * Created by Jiri on 11. 7. 2014.
 */
@Service
public class KeysRepositoryImpl implements KeysRepositoryCustom {
    @Override
    public Key generateNewKey() {
        return null;
    }
}
