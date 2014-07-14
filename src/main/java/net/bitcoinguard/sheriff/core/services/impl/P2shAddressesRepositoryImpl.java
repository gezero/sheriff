package net.bitcoinguard.sheriff.core.services.impl;

import net.bitcoinguard.sheriff.core.entities.Key;
import net.bitcoinguard.sheriff.core.entities.P2shAddress;
import net.bitcoinguard.sheriff.core.entities.Transaction;
import net.bitcoinguard.sheriff.core.services.BitcoinMagicService;
import net.bitcoinguard.sheriff.core.services.KeysRepository;
import net.bitcoinguard.sheriff.core.services.P2shAddressesRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jiri on 11. 7. 2014.
 */
@Service
public class P2shAddressesRepositoryImpl implements P2shAddressesRepositoryCustom {
    BitcoinMagicService bitcoinMagicService;
    KeysRepository keysRepository;

    @Autowired
    public P2shAddressesRepositoryImpl(BitcoinMagicService bitcoinMagicService,KeysRepository keysRepository) {
        this.bitcoinMagicService = bitcoinMagicService;
        this.keysRepository = keysRepository;
    }

    @Override
    public P2shAddress createNew(List<String> publicKeys, Integer requiredKeys) {
        P2shAddress address = new P2shAddress();
        address.setRedeemScript(bitcoinMagicService.createMultiSignatureRedeemScript(publicKeys,requiredKeys));
        address.setAddress(bitcoinMagicService.getAddressFromRedeemScript(address.getRedeemScript()));
        List<Key> keys = new ArrayList<>(publicKeys.size());
        for (String publicKey : publicKeys) {
            Key key = new Key();
            key.setPublicKey(publicKey);
            keys.add(key);
        }
        address.setKeys(keys);
        return address;
    }

    @Override
    public Transaction createNewTransaction(P2shAddress address, String targetAddress, Long amount) {
        return null;
    }
}
