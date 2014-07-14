package net.bitcoinguard.sheriff.core.services.impl;

import com.google.bitcoin.core.Base58;
import com.google.bitcoin.core.ECKey;
import com.google.bitcoin.core.Utils;
import com.google.bitcoin.script.Script;
import com.google.bitcoin.script.ScriptBuilder;
import net.bitcoinguard.sheriff.core.services.BitcoinMagicService;
import org.springframework.stereotype.Service;

import java.util.*;

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
        Script script = ScriptBuilder.createMultiSigOutputScript(requiredKeys, keys);
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
    public String getAddressFromRedeemScript(String multiSignatureRedeemScript) {
        Script script = new Script(Utils.HEX.decode(multiSignatureRedeemScript));
        byte[] sha256hash160 = Utils.sha256hash160(script.getProgram());
        byte[] bytes = new byte[sha256hash160.length+1];
        bytes[0]= -60;
        System.arraycopy(sha256hash160,0,bytes,1,sha256hash160.length);
        byte[] checkSum = Utils.doubleDigest(bytes);
        byte[] address = new byte[bytes.length + 4];
        System.arraycopy(bytes,0,address,0,bytes.length);
        System.arraycopy(checkSum,0,address,bytes.length,4);
        return Base58.encode(address);
    }

    @Override
    public String createTransaction(String address, String targetAddress, Long amount) {
        return null;
    }
}
