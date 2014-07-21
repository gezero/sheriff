package com.bitcoinj.wallet;

import com.google.bitcoin.core.*;
import com.google.bitcoin.crypto.TransactionSignature;
import com.google.bitcoin.script.Script;
import com.google.bitcoin.script.ScriptBuilder;
import com.google.common.util.concurrent.ListenableFuture;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * This is a proof of concept test. It shows how to create P2SH address, how to send money to it, how to watch the
 * address in bitcoinJ, how to create spending transaction and how to sign the spending transaction.
 * <p/>
 * Created by Jiri on 21. 7. 2014.
 */
public class MultisignatureTest  extends WalletTests{
    private static final Logger logger = LoggerFactory.getLogger(MultisignatureTest.class);

    /**
     * this method asks BitcoinJ to create multisignature redeem script. It will be m/n redeem script where m is
     * requiredKeys parameter and n is size of the publicKeys.
     *
     * @param publicKeys   keys used for m/n redeem script
     * @param requiredKeys amount of keys needed to sign the redeem script
     * @return the redeem script hex encoded
     */
    public String createMultiSignatureRedeemScript(List<String> publicKeys, int requiredKeys) {

        List<ECKey> keys = new ArrayList<>(publicKeys.size());
        for (String publicKey : publicKeys) {
            keys.add(ECKey.fromPublicOnly(Utils.HEX.decode(publicKey)));
        }
        Script script = ScriptBuilder.createMultiSigOutputScript(requiredKeys, keys);
        return Utils.HEX.encode(script.getProgram());
    }

    /**
     * This method creates P2SH address from the redeem script provided.
     */
    public String getAddressFromRedeemScript(String multiSignatureRedeemScript) {
        Script script = new Script(Utils.HEX.decode(multiSignatureRedeemScript));
        byte[] sha256hash160 = Utils.sha256hash160(script.getProgram());
        byte[] bytes = new byte[sha256hash160.length + 1];
        bytes[0] = -60; //todo: this is specific for test net
        System.arraycopy(sha256hash160, 0, bytes, 1, sha256hash160.length);
        byte[] checkSum = Utils.doubleDigest(bytes);
        byte[] address = new byte[bytes.length + 4];
        System.arraycopy(bytes, 0, address, 0, bytes.length);
        System.arraycopy(checkSum, 0, address, bytes.length, 4);
        return Base58.encode(address);
    }

    /**
     * This method will sign all the inputs of the transaction using the two keys provided.
     */
    private void signSpendingTransaction(ECKey key1, ECKey key2, String multiSignatureRedeemScript, Transaction spendingTransaction) {
        for (int i = 0; i < spendingTransaction.getInputs().size(); ++i) {
            //Note that we need only 2 signatures to spend the money. This was specified in the createMultiSignatureRedeemScript
            TransactionSignature signature1 = spendingTransaction.calculateSignature(i, key1, Utils.HEX.decode(multiSignatureRedeemScript), Transaction.SigHash.ALL, true);
            TransactionSignature signature2 = spendingTransaction.calculateSignature(i, key2, Utils.HEX.decode(multiSignatureRedeemScript), Transaction.SigHash.ALL, true);

            ScriptBuilder builder = new ScriptBuilder();
            builder.smallNum(0);
            builder.data(signature1.encodeToBitcoin());
            builder.data(signature2.encodeToBitcoin());
            byte[] redeemScriptBytes = Utils.HEX.decode(multiSignatureRedeemScript);
            Script redeemScript = new Script(redeemScriptBytes);
            builder.data(redeemScript.getProgram());
            Script p2SHMultiSigInputScript = builder.build();

            spendingTransaction.getInput(i).setScriptSig(p2SHMultiSigInputScript);
        }
    }

    /**
     * This method will create transaction that spends the outputs provided
     *
     */
    private Transaction createSpendingTransaction(List<TransactionOutput> watchedOutputs) {
        Transaction transaction = new Transaction(params());

        Coin total = Coin.ZERO;
        for (TransactionOutput watchedOutput : watchedOutputs) {
            transaction.addInput(watchedOutput);
            total = total.add(watchedOutput.getValue());
        }
        transaction.addOutput(total.subtract(Transaction.REFERENCE_DEFAULT_MIN_TX_FEE), freshAddress());

        return transaction;
    }



    @Test
    @Ignore("test is using coins")
    public void fullScenario() throws Exception {

        //We will create two keys that will be used in this test
        ECKey key1 = new ECKey();
        ECKey key2 = new ECKey();
        ECKey key3 = new ECKey();

        //we will create multisignature redeem script to redeem the money
        List<String> keys = new ArrayList<String>(3);
        keys.add(Utils.HEX.encode(key1.getPubKey()));
        keys.add(Utils.HEX.encode(key2.getPubKey()));
        keys.add(Utils.HEX.encode(key3.getPubKey()));
        String multiSignatureRedeemScript = createMultiSignatureRedeemScript(keys, 2);

        //we will create P2SH address
        String p2shAddress = getAddressFromRedeemScript(multiSignatureRedeemScript);

        Address address = new Address(params(), p2shAddress);

        watchAddress(address);


        //We will now try to send money to the multisign address
        Transaction depositTransaction;
        try {
            depositTransaction = sendMoneyToAddress(MINIMUM_TO_SEND, p2shAddress);
        } catch (com.google.bitcoin.core.InsufficientMoneyException e) {
            logger.error("Your balance is: {}", getBalance());
            logger.error("Send faucet money to this address {}", freshAddress());
            logger.error("Try this faucet http://faucet.xeno-genesis.com/");
            logger.error("Or Try this faucet https://tpfaucet.appspot.com/");
            throw e;
        }

        //lets get all the outputs that we are watching
        List<TransactionOutput> watchedOutputs = getWatchedOutputs(address);

        //create ne transaction to send money back to the wallet
        Transaction spendingTransaction = createSpendingTransaction(watchedOutputs);

        //sign the spending transaction using keys created
        signSpendingTransaction(key1, key2, multiSignatureRedeemScript, spendingTransaction);

        //broadcast the transaction
        broadcastSignedTransaction(spendingTransaction);

    }





}
