package net.bitcoinguard.sheriff.core.services;

import net.bitcoinguard.sheriff.core.entities.Key;

/**
 * Created by Jiri on 7. 7. 2014.
 */
public interface KeyService {
    Key find(Long id);
}
