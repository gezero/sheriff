package net.bitcoinguard.sheriff.core.services.impl;

import net.bitcoinguard.sheriff.core.services.BitcoinMagicService;

import java.util.List;
import java.util.Map;

/**
 * Created by Jiri on 11. 7. 2014.
 */

public class BitcoinJMagicService implements BitcoinMagicService {
    @Override
    public String createMultiSignatureRedeemScript(List<String> publicKeys, int requiredKeys) {
        return null;
    }

    @Override
    public Map<String, String> generateKeyPair() {
        return null;
    }
}
