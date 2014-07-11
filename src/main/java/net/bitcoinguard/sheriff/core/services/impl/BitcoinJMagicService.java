package net.bitcoinguard.sheriff.core.services.impl;

import com.google.bitcoin.core.ECKey;
import com.google.bitcoin.core.Utils;
import com.google.bitcoin.script.Script;
import com.google.bitcoin.script.ScriptBuilder;
import net.bitcoinguard.sheriff.core.services.BitcoinMagicService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

        List<ECKey> keys = new ArrayList<>(publicKeys.size());
        for (String publicKey : publicKeys) {
            keys.add(ECKey.fromPublicOnly(Utils.HEX.decode(publicKey)));
        }
        Script script = ScriptBuilder.createMultiSigOutputScript(requiredKeys,keys);
        return Utils.HEX.encode(script.getProgram());
    }

    @Override
    public Map<String, String> generateKeyPair() {
        ECKey key = new ECKey();
        Map<String, String> map = new HashMap<>();
        map.put(PUBLIC_KEY, Utils.HEX.encode(key.getPubKey()));
        map.put(PRIVATE_KEY, Utils.HEX.encode(key.getPrivKeyBytes()));
        return map;
    }

    @Override
    public String getHashOfScript(String redeemScript) {
        return null;
    }

    @Override
    public String getAddressFromRedeemScript(String multiSignatureRedeemScript) {
        return null;
    }
}
