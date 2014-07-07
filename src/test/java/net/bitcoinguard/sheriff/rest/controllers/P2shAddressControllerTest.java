package net.bitcoinguard.sheriff.rest.controllers;

import net.bitcoinguard.sheriff.core.entities.P2shAddress;
import net.bitcoinguard.sheriff.core.entities.RedeemScript;
import net.bitcoinguard.sheriff.core.services.P2shAddressService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class P2shAddressControllerTest {
    @InjectMocks
    private P2shAddressController p2shAddressController;
    private MockMvc mockMvc;

    @Mock
    P2shAddressService p2shAddressService;

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

        when(p2shAddressService.find(1L)).thenReturn(address);

        mockMvc.perform(get("/rest/addresses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address", is("testAddress")))
                .andExpect(jsonPath("$.links[*].href", hasItem(endsWith("/addresses/1"))));

    }
    @Test
    public void testFindNonExistingAddress() throws Exception {

        when(p2shAddressService.find(1L)).thenReturn(null);

        mockMvc.perform(get("/rest/addresses/1"))
                .andExpect(status().isNotFound());

    }
}