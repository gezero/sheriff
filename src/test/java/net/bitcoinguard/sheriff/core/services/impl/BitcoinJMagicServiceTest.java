package net.bitcoinguard.sheriff.core.services.impl;

import com.google.bitcoin.core.ECKey;
import com.google.bitcoin.core.Utils;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class BitcoinJMagicServiceTest {
    BitcoinJMagicService bitcoinJMagicService = new BitcoinJMagicService();

    @Test
    public void testGenerateKeyPair() throws Exception {

        Map<String, String> stringStringMap = bitcoinJMagicService.generateKeyPair();

        ECKey key = ECKey.fromPrivateAndPrecalculatedPublic(Utils.HEX.decode(stringStringMap.get(BitcoinJMagicService.PRIVATE_KEY)),Utils.HEX.decode(stringStringMap.get(BitcoinJMagicService.PUBLIC_KEY)));

        String testMessageSignature = key.signMessage("testMessage");
        key.verifyMessage("testMessage",testMessageSignature);

    }
}