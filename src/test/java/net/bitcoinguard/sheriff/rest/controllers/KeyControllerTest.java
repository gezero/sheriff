package net.bitcoinguard.sheriff.rest.controllers;

import net.bitcoinguard.sheriff.core.entities.Key;
import net.bitcoinguard.sheriff.core.services.KeyService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class KeyControllerTest {
    @InjectMocks
    private KeyController keyController;
    private MockMvc mockMvc;

    @Mock
    KeyService keyService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(keyController).build();
    }

    @Test
    public void getExistingKey() throws Exception {
        Key key = new Key();
        key.setId(1L);
        key.setPublicKey("public");
        key.setPrivatekey("private");

        when(keyService.find(1L)).thenReturn(key);

        mockMvc.perform(get("/rest/keys/1"))
                .andDo(print())
                .andExpect(jsonPath("$.publicKey", is(key.getPublicKey())))
                .andExpect(jsonPath("$.links[*].href",hasItem(endsWith("/keys/1"))))
                .andExpect(status().isOk());
    }

    @Test
    public void getNonExistingKey() throws Exception {

        when(keyService.find(1L)).thenReturn(null);

        mockMvc.perform(get("/rest/keys/1"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

}