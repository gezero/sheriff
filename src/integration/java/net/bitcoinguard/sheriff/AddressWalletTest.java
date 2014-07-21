package net.bitcoinguard.sheriff;

import com.bitcoinj.wallet.WalletTests;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.bitcoin.core.ECKey;
import com.google.bitcoin.core.Utils;
import net.bitcoinguard.sheriff.core.entities.Transaction;
import net.bitcoinguard.sheriff.rest.controllers.P2shAddressController;
import net.bitcoinguard.sheriff.rest.entities.P2shAddressResource;
import net.bitcoinguard.sheriff.rest.entities.TransactionResource;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Jiri on 21. 7. 2014.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class AddressWalletTest extends WalletTests {
    @Autowired
    private WebApplicationContext context;
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private P2shAddressController p2shAddressController;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void createNewAddress() throws Exception {
        P2shAddressResource requestAddress = addressRequest();

        mockMvc.perform(post("/rest/addresses")
                        .content(prepareRequest(requestAddress))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.address", is(any(String.class))))
                .andExpect(jsonPath("$.keys[*]", hasItem(requestAddress.getKeys().get(0))))
                .andExpect(jsonPath("$.keys[*]", hasItem(requestAddress.getKeys().get(1))))
                .andExpect(jsonPath("$.totalKeys", is(3)))
                .andExpect(jsonPath("$.requiredKeys", is(2)))
                .andExpect(jsonPath("$.links[*].href", hasItem(containsString("/addresses/"))));

    }
    private String prepareRequest(Object request) throws IOException {
        StringWriter writer = new StringWriter();
        mapper.writeValue(writer, request);
        return writer.toString();
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

    private <T> T getContent(MvcResult mvcResult, Class<T> cls) throws IOException {
        return mapper.readValue(mvcResult.getResponse().getContentAsString(), cls);
    }

    @Test
//    @Ignore("test is using coins")
    public void testBasicTransaction() throws Exception {
        P2shAddressResource request = addressRequest();

        MvcResult mvcResult = mockMvc.perform(post("/rest/addresses")
                        .content(prepareRequest(request))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();
        P2shAddressResource address = getContent(mvcResult, P2shAddressResource.class);


        mockMvc.perform(get("/rest/addresses/" + address.getAddress()))
                .andDo(print())
                .andExpect(jsonPath("$.balance", is(0)));

        sendMoneyToAddress(MINIMUM_TO_SEND,address.getAddress());

        Thread.sleep(10000);

        mockMvc.perform(get("/rest/addresses/" + address.getAddress()))
                .andDo(print())
                .andExpect(jsonPath("$.balance", is((int)MINIMUM_TO_SEND.longValue())));

        TransactionResource transactionRequest = new TransactionResource();
        String targetAddress = freshAddress().toString();
        transactionRequest.setTargetAddress(targetAddress);
        transactionRequest.setAmount(MINIMUM_TO_SEND.subtract(com.google.bitcoin.core.Transaction.REFERENCE_DEFAULT_MIN_TX_FEE).longValue());
        mvcResult = mockMvc.perform(post("/rest/addresses/" + address.getAddress() + "/transactions")
                        .content(prepareRequest(transactionRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        TransactionResource transaction = getContent(mvcResult, TransactionResource.class);
        assertThat(transaction.getAmount(), is(transactionRequest.getAmount()));
        assertThat(transaction.getSourceAddress(), is(address.getAddress()));
        assertThat(transaction.getTargetAddress(), is(targetAddress));

    }


}
