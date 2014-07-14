package net.bitcoinguard.endtoend;

import com.google.bitcoin.core.ECKey;
import com.google.bitcoin.core.Utils;
import net.bitcoinguard.sheriff.Application;
import net.bitcoinguard.sheriff.rest.controllers.P2shAddressController;
import net.bitcoinguard.sheriff.rest.entities.P2shAddressResource;
import net.bitcoinguard.sheriff.rest.entities.TransactionResource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/**
 * Created by Jiri on 11. 7. 2014.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class AddressTest {
    @Autowired
    private P2shAddressController p2shAddressController;


    @Test
    public void createNewAddress() throws Exception {
        P2shAddressResource request = addressRequest();

        ResponseEntity<P2shAddressResource> responseEntity = p2shAddressController.createNewAddress(request);
        P2shAddressResource response = responseEntity.getBody();

        assertThat(response.getTotalKeys(), is(3));
        assertThat(response.getRequiredKeys(), is(2));
        assertThat(response.getKeys(), hasItems(request.getKeys().get(0), request.getKeys().get(1)));
    }

    private P2shAddressResource addressRequest() {
        ECKey key1 = new ECKey();
        ECKey key2 = new ECKey();

        P2shAddressResource request = new P2shAddressResource();
        List<String> keys = new ArrayList<>();
        keys.add(Utils.HEX.encode(key1.getPubKey()));
        keys.add(Utils.HEX.encode(key2.getPubKey()));
        request.setKeys(keys);
        return request;
    }

    @Test
    public void createNewTransaction() throws Exception{
        P2shAddressResource request = addressRequest();

        ResponseEntity<P2shAddressResource> responseEntity = p2shAddressController.createNewAddress(request);
        P2shAddressResource address1 = responseEntity.getBody();

        request = addressRequest();
        responseEntity = p2shAddressController.createNewAddress(request);
        P2shAddressResource address2 = responseEntity.getBody();

        assertThat(address1.getAddress(),is(not(address2.getAddress())));

        TransactionResource transactionResource = new TransactionResource();
        transactionResource.setTargetAddress(address2.getAddress());
        transactionResource.setAmount(1000L);
        ResponseEntity<TransactionResource> transactionResponse = p2shAddressController.createTransaction(address1.getAddress(), transactionResource);

        TransactionResource transaction = transactionResponse.getBody();
        assertThat(transaction.getAmount(),is(transactionResource.getAmount()));
        assertThat(transaction.getSourceAddress(),is(address1.getAddress()));
        assertThat(transaction.getTargetAddress(),is(address2.getAddress()));
    }
}
