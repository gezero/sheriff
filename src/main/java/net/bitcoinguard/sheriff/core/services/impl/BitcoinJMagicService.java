package net.bitcoinguard.sheriff.core.services.impl;

import com.google.bitcoin.core.ECKey;
import com.google.bitcoin.core.Utils;
import net.bitcoinguard.sheriff.core.services.BitcoinMagicService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jiri on 11. 7. 2014.
 */
@Service
public class BitcoinJMagicService implements BitcoinMagicService {
    @Override
    public String createMultiSignatureRedeemScript(List<String> publicKeys, int requiredKeys) {
        return null;
    }

    @Override
    public Map<String, String> generateKeyPair() {
        ECKey key = new ECKey();
        Map<String, String> map = new HashMap<>();
        map.put(PUBLIC_KEY, Utils.HEX.encode(key.getPubKey()));
        map.put(PRIVATE_KEY, Utils.HEX.encode(key.getPrivKeyBytes()));
        return map;
    }
}
