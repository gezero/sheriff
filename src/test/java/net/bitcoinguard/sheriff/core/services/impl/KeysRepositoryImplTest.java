package net.bitcoinguard.sheriff.core.services.impl;

import net.bitcoinguard.sheriff.core.entities.Key;
import net.bitcoinguard.sheriff.bitcoin.service.BitcoinMagicService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class KeysRepositoryImplTest {

    @InjectMocks
    KeysRepositoryImpl keysRepository;

    @Mock
    BitcoinMagicService bitcoinMagicService;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGenerateNewKey() throws Exception {

        Map<String, String> pair = new HashMap<>();
        String testPublicKey = "testPublicKey";
        pair.put(BitcoinMagicService.PUBLIC_KEY, testPublicKey);
        String testPrivateKey = "testPrivateKey";
        pair.put(BitcoinMagicService.PRIVATE_KEY, testPrivateKey);
        when(bitcoinMagicService.generateKeyPair()).thenReturn(pair);

        Key key = keysRepository.generateNewKey();
        assertThat(key.getPublicKey(),is(testPublicKey));
        assertThat(key.getPrivateKey(),is(testPrivateKey));

    }
}