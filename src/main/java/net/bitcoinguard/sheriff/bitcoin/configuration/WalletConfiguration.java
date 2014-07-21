package net.bitcoinguard.sheriff.bitcoin.configuration;

import com.google.bitcoin.core.ECKey;
import com.google.bitcoin.core.NetworkParameters;
import com.google.bitcoin.core.Wallet;
import com.google.bitcoin.kits.WalletAppKit;
import com.google.bitcoin.params.TestNet3Params;
import net.bitcoinguard.sheriff.bitcoin.service.impl.BitcoinJEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

/**
 * Created by Jiri on 21. 7. 2014.
 */
@Configuration
public class WalletConfiguration {


    @Value("${bitcoin.wallet.name}")
    private String filePrefix;
    @Value("${bitcoin.wallet.directory}")
    private String walletDirectory;
    @Value("${bitcoin.wallet.network}")
    private String network;

    @Bean
    Wallet wallet() {
        WalletAppKit walletAppKit = new WalletAppKit(networkParameters(), workingDirectory(), filePrefix) {
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
        return walletAppKit.wallet();
    }

    private File workingDirectory() {
        return new File(walletDirectory);
    }

    @Bean
    public NetworkParameters networkParameters() {
        switch (network) {
            case "testnet":
                return TestNet3Params.get();
            default:
                throw new RuntimeException("Specify what network you would like to use");
        }
    }

    /**
     * I am solving chicken and egg problem here. I would love to have this entity autowired as a field and call the
     * addEventListener() method inside the wallet() method, but that does not work. I have cyclic dependencies that
     * among other things depend on creation of DataSource. The creation of the wallet is initialized before the
     * initialization of the dataSource. That would leave me the bitcoinJEventListener field not initialized...
     * <p/>
     * The solution is to use the Autowired annotation on the setter instead of on the field. Spring is first creating
     * the beans and only after that is he setting using the setters.
     * <p/>
     * Note that the wallet() call inside of this method will not execute the code from the actual wallet() method.
     * That call was already done previously when Spring initialized the Wallet Bean.
     * <p/>
     * Try to put breakpoint on the line inside of this method and also inside of the wallet() method and check for
     * yourself the order of execution... You can use any test that is configuring the spring application context, for
     * example net.bitcoinguard.sheriff.AddressTest
     * <p/>
     * Strange, funny, magical, but it works...
     *
     */
    @Autowired
    public void setBitcoinJEventListener(BitcoinJEventListener bitcoinJEventListener) {
        wallet().addEventListener(bitcoinJEventListener);
    }
}
