package net.bitcoinguard.sheriff.bitcoin.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Jiri on 9. 7. 2014.
 */
public interface BitcoinMagicService {
    public static String PUBLIC_KEY = "publicKey";
    public static String PRIVATE_KEY = "privateKey";

    /**
     * Creates multi signature redeem script from the input public keys. The resulted redeem script will require requiredKeys to spend
     *
     * @param publicKeys   public keys to use in the redeem script
     * @param requiredKeys amount of keys required to use the redeem script.
     * @return bitcoin m/n multisignature redeem script
     */
    String createMultiSignatureRedeemScript(List<String> publicKeys, int requiredKeys);

    Map<String, String> generateKeyPair();

    String getAddressFromRedeemScript(String multiSignatureRedeemScript);

    String createTransaction(String address, String targetAddress, Long amount);

    void watchAddress(String address);

    Long getBalance(String address);

    String addSignature(String rawTransaction, String privateKey);
}
