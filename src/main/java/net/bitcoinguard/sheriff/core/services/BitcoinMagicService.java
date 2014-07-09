package net.bitcoinguard.sheriff.core.services;

import java.util.List;

/**
 * Created by Jiri on 9. 7. 2014.
 */
public interface BitcoinMagicService {
    /**
     * Creates multi signature redeem script from the input public keys. The resulted redeem script will require requiredKeys to spend
     *
     * @param publicKeys  public keys to use in the redeem script
     * @param requiredKeys amount of keys required to use the redeem script.
     * @return bitcoin m/n multisignature redeem script
     */
    String createMultiSignatureRedeemScript(List<String> publicKeys, int requiredKeys);

}
