package com.bitcoinj.wallet;

import com.google.bitcoin.core.*;
import com.google.bitcoin.kits.WalletAppKit;
import com.google.bitcoin.params.TestNet3Params;
import com.google.common.util.concurrent.ListenableFuture;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by Jiri on 21. 7. 2014.
 */
public class WalletTests {
    private static final Logger logger = LoggerFactory.getLogger(WalletTests.class);
    protected final Coin MINIMUM_TO_SEND = Transaction.REFERENCE_DEFAULT_MIN_TX_FEE.multiply(3);



    private final File workingDirectory = new File("build/work");
    private final NetworkParameters networkParameters = TestNet3Params.get();

    private WalletAppKit walletAppKit;
    private Wallet wallet;


    @Before
    public void createWorkingDir() {
        if (!workingDirectory.exists()) {
            if (!workingDirectory.mkdir()) {
                throw new RuntimeException("Could not create named directory.");
            }
        }
    }

    @Before
    public void initWallet() {
        walletAppKit = new WalletAppKit(networkParameters, workingDirectory, "Secure-btc-change-me") {
            @Override
            protected void onSetupCompleted() {
                // This is called in a background thread after startAndWait is called, as setting up various objects
                // can do disk and network IO that may cause UI jank/stuttering in wallet apps if it were to be done
                // on the main thread.
                while ((wallet().getImportedKeys().size() < 1)) {
                    wallet().importKey(new ECKey());
                }
            }
        };
        walletAppKit.startAsync();
        walletAppKit.awaitRunning();
        wallet = walletAppKit.wallet();
    }
    /**
     * This method will send amount provided to address provided. It can be P2SH address.
     */
    protected Transaction sendMoneyToAddress(Coin amountToSend, String address) throws AddressFormatException, InsufficientMoneyException, InterruptedException, BrokenBarrierException {
        Address multiSignatureAddress = new Address(networkParameters, address);
        final Wallet.SendResult sendResult = wallet.sendCoins(walletAppKit.peerGroup(), multiSignatureAddress, amountToSend);

        logger.info("Trying to send to address {}", address);
        ListenableFuture<Transaction> future = sendResult.broadcastComplete;
        waitForFuture(future, "Money is send, you can try to spend it");
        return sendResult.tx;
    }

    /**
     * method waiting for broadcast to complete.
     */
    private void waitForFuture(ListenableFuture<Transaction> future, final String logMessage) throws InterruptedException, BrokenBarrierException {
        final CyclicBarrier barrier = new CyclicBarrier(2);
        future.addListener(new Runnable() {
            @Override
            public void run() {
                logger.info(logMessage);
                try {
                    barrier.await();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }, new SynchronousExecutor());
        barrier.await();
    }

    protected NetworkParameters params() {
        return networkParameters;
    }

    /**
     * This method broadcasts transaction to the bitcoin network
     */
    protected void broadcastSignedTransaction(Transaction spendingTransaction) throws InterruptedException, BrokenBarrierException {
        ListenableFuture<Transaction> transactionListenableFuture = walletAppKit.peerGroup().broadcastTransaction(spendingTransaction);
        waitForFuture(transactionListenableFuture, "money should be on the way back.");
    }

    protected Address freshAddress() {
        return wallet.freshReceiveAddress();
    }

    protected void watchAddress(Address address) {
        wallet.addWatchedAddress(address);
    }

    protected Coin getBalance(){
        return wallet.getBalance();
    }
    protected List<TransactionOutput> getWatchedOutputs(Address address) {
        List<TransactionOutput> watchedOutputs = wallet.getWatchedOutputs(false);
        List<TransactionOutput> filteredOutputs = new LinkedList<>();

        //filter only the outputs that are for the address that we created in this test
        for (TransactionOutput watchedOutput : watchedOutputs) {
            Address toAddress = watchedOutput.getScriptPubKey().getToAddress(networkParameters);
            if (toAddress.equals(address)) {
                filteredOutputs.add(watchedOutput);
            }
        }
        return filteredOutputs;
    }

    private class SynchronousExecutor implements java.util.concurrent.Executor {
        @Override
        public void execute(Runnable command) {
            command.run();
        }
    }

    @Test
    @Ignore("test is using wallet")
    public void showBalance() {
        logger.info(getBalance().toFriendlyString());
    }


}
