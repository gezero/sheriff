package net.bitcoinguard.sheriff.rest.controllers;

import net.bitcoinguard.sheriff.core.entities.Key;
import net.bitcoinguard.sheriff.core.entities.P2shAddress;
import net.bitcoinguard.sheriff.core.entities.RedeemScript;
import net.bitcoinguard.sheriff.core.services.KeysRepository;
import net.bitcoinguard.sheriff.core.services.P2shAddressesRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class P2shAddressControllerTest {
    @InjectMocks
    private P2shAddressController p2shAddressController;
    private MockMvc mockMvc;

    @Mock
    P2shAddressesRepository p2shAddressesRepository;
    @Mock
    KeysRepository keysRepository;

    @Captor
    ArgumentCaptor<List<String>> captor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(p2shAddressController).build();
    }


    @Test
    public void testFindExistingAddress() throws Exception {
        P2shAddress address = new P2shAddress();
        address.setId(1L);
        address.setAddress("testAddress");
        RedeemScript script = new RedeemScript();
        address.setScript(script);

        when(p2shAddressesRepository.findOne(1L)).thenReturn(address);

        mockMvc.perform(get("/rest/addresses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address", is("testAddress")))
                .andExpect(jsonPath("$.links[*].href", hasItem(endsWith("/addresses/1"))));

    }

    @Test
    public void testFindNonExistingAddress() throws Exception {

        when(p2shAddressesRepository.findOne(1L)).thenReturn(null);

        mockMvc.perform(get("/rest/addresses/1"))
                .andExpect(status().isNotFound());

    }


    @Test
    public void testCreateNewAddress() throws Exception {
        P2shAddress address = new P2shAddress();
        address.setId(1L);
        address.setAddress("testAddress");
        RedeemScript script = new RedeemScript();
        address.setScript(script);
        Key key = new Key();
        key.setPublicKey("testKey");

        when(keysRepository.generateNewKey()).thenReturn(key);
        when(p2shAddressesRepository.createNew(anyList())).thenReturn(address);

        mockMvc.perform(post("/rest/addresses")
                        .content("{\"keys\":[\"key1\",\"key2\"],\"requiredKeys\":2, \"totalKeys\":3}")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.address", is("testAddress")))
                .andExpect(jsonPath("$.links[*].href", hasItem(endsWith("/addresses/1"))));

        verify(p2shAddressesRepository).createNew(captor.capture());

        List<String> keys = captor.getValue();
        assertThat(keys,contains("key1", "key2",key.getPublicKey()));

    }

    @Test
    public void testToManyKeys() throws Exception {
        mockMvc.perform(post("/rest/addresses")
                        .content("{\"keys\":[\"key1\",\"key2\"],\"requiredKeys\":2, \"totalKeys\":2}")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

}