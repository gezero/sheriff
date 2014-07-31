package sheriff.rest.controllers;

import sheriff.bitcoin.service.impl.BitcoinJMagicService;
import sheriff.core.entities.Key;
import sheriff.core.entities.P2shAddress;
import sheriff.core.entities.Transaction;
import sheriff.core.services.P2shAddressesRepository;
import sheriff.core.services.TransactionsRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TransactionControllerTest {
    @InjectMocks
    private TransactionController transactionController;
    private MockMvc mockMvc;

    @Mock
    TransactionsRepository transactionsRepository;
    @Mock
    P2shAddressesRepository p2shAddressesRepository;
    @Mock
    BitcoinJMagicService bitcoinJMagicService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
    }


    @Test
    public void testFindExistingTransaction() throws Exception {
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setAmount(100000L);
        transaction.setRawTransaction("rawTransaction");
        transaction.setSourceAddress(new P2shAddress());


        when(transactionsRepository.findOne(1L)).thenReturn(transaction);

        mockMvc.perform(get("/rest/transactions/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount", is(100000)))
                .andExpect(jsonPath("$.rawTransaction", is("rawTransaction")))
                .andExpect(jsonPath("$.links[*].href", hasItem(endsWith("/transactions/1"))));
    }

    @Test
    public void testCreateTransaction() throws Exception{
        Transaction transaction = createTransaction();
        Transaction transactionWithId = createTransaction();
        transactionWithId.setId(1L);


        P2shAddress address = new P2shAddress();
        transactionWithId.setSourceAddress(address);

        when(p2shAddressesRepository.findByAddress("sourceAddress")).thenReturn(address);
        when(p2shAddressesRepository.createNewTransaction(address, transaction.getTargetAddress(), transaction.getAmount())).thenReturn(transaction);
        when(transactionsRepository.save(transaction)).thenReturn(transactionWithId);

        mockMvc.perform(post("/rest/transactions")
                        .content("{\"amount\":10000,\"sourceAddress\":\"sourceAddress\",\"targetAddress\":\"targetAddress\"}")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.links[*].href", hasItem(endsWith("/transactions/1"))));
        verify(transactionsRepository).save(transaction);
    }

    private Transaction createTransaction() {
        Transaction transaction = new Transaction();
        transaction.setTargetAddress("targetAddress");
        transaction.setAmount(10000L);
        transaction.setRawTransaction("rawTransaction");
        return transaction;
    }

    @Test
    public void testCanCreateTransactionOnlyOnExistingAddress() throws Exception{

        mockMvc.perform(post("/rest/transactions")
                        .content("{\"amount\":10000,\"sourceAddress\":\"sourceAddress\",\"targetAddress\":\"targetAddress\"}")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isNotFound());
    }


    @Test
    public void testSignTransaction() throws Exception {
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setAmount(100000L);
        transaction.setRawTransaction("rawTransaction");
        P2shAddress sourceAddress = new P2shAddress();
        transaction.setSourceAddress(sourceAddress);
        List<Key> keys = new LinkedList<>();
        sourceAddress.setKeys(keys);
        Key key = new Key();
        keys.add(key);
        key.setPrivateKey("privateKey");
        when(transactionsRepository.findOne(1L)).thenReturn(transaction);
        when(bitcoinJMagicService.addSignature("rawData","privateKey")).thenReturn("signedTransaction");
        mockMvc.perform(post("/rest/transactions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"rawTransaction\":\"rawData\"}")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount", is(100000)))
                .andExpect(jsonPath("$.rawTransaction", is("signedTransaction")))
                .andExpect(jsonPath("$.links[*].href", hasItem(endsWith("/transactions/1"))));

    }

    @Test
    public void testFindNonExistingAddress() throws Exception {

        when(transactionsRepository.findOne(1L)).thenReturn(null);

        mockMvc.perform(get("/rest/transactions/1"))
                .andExpect(status().isNotFound());

    }
}