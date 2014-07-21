package net.bitcoinguard.sheriff.bitcoin.service.impl;

import com.google.bitcoin.core.*;
import com.google.bitcoin.script.Script;
import net.bitcoinguard.sheriff.core.entities.P2shAddress;
import net.bitcoinguard.sheriff.core.services.P2shAddressesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Jiri on 21. 7. 2014.
 */
@Service
public class BitcoinJEventListener implements WalletEventListener {


    P2shAddressesRepository addressesRepository;
    private NetworkParameters params;

    @Autowired
    public BitcoinJEventListener(P2shAddressesRepository addressesRepository, NetworkParameters params) {
        this.addressesRepository = addressesRepository;
        this.params = params;
    }


    @Override
    public void onCoinsReceived(Wallet wallet, Transaction tx, Coin prevBalance, Coin newBalance) {
        List<Address> addressesToUpdate = new ArrayList<>(tx.getOutputs().size());
        for (TransactionOutput output : tx.getOutputs()) {
            Address toAddress = output.getScriptPubKey().getToAddress(params);
            if (wallet.isAddressWatched(toAddress)) {
                addressesToUpdate.add(toAddress);
            }
        }
        updateAddresses(wallet, addressesToUpdate);
    }

    private void updateAddresses(Wallet wallet, List<Address> addressesToUpdate) {
        LinkedList<TransactionOutput> watchedOutputs = wallet.getWatchedOutputs(true);
        Map<Address, Coin> balances = new HashMap<>();
        for (TransactionOutput watchedOutput : watchedOutputs) {
            Address toAddress = watchedOutput.getScriptPubKey().getToAddress(params);
            if (addressesToUpdate.contains(toAddress)) {
                Coin coin = balances.get(toAddress);
                if (coin == null) {
                    coin = Coin.ZERO;
                }
                balances.put(toAddress,coin.add(watchedOutput.getValue()));
            }
        }
        for (Address address : balances.keySet()) {
            P2shAddress byAddress = addressesRepository.findByAddress(address.toString());
            byAddress.setBalance(balances.get(address).longValue());
            addressesRepository.save(byAddress);
        }
    }

    @Override
    public void onCoinsSent(Wallet wallet, Transaction tx, Coin prevBalance, Coin newBalance) {

    }

    @Override
    public void onReorganize(Wallet wallet) {

    }

    @Override
    public void onTransactionConfidenceChanged(Wallet wallet, Transaction tx) {

    }

    @Override
    public void onWalletChanged(Wallet wallet) {

    }

    @Override
    public void onScriptsAdded(Wallet wallet, List<Script> scripts) {

    }

    @Override
    public void onKeysAdded(List<ECKey> keys) {

    }
}
