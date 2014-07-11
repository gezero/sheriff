package net.bitcoinguard.sheriff.core.services.impl;

import net.bitcoinguard.sheriff.core.entities.P2shAddress;
import net.bitcoinguard.sheriff.core.services.BitcoinMagicService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class P2shAddressesRepositoryImplTest {
    @InjectMocks
    P2shAddressesRepositoryImpl addressesRepository;

    @Mock
    BitcoinMagicService bitcoinMagicService;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateNew() throws Exception {
        List<String> keys = new ArrayList<>();
        keys.add("testKey1");
        keys.add("testKey2");
        keys.add("testKey3");

        when(bitcoinMagicService.createMultiSignatureRedeemScript(keys,2)).thenReturn("redeemScript");
        when(bitcoinMagicService.getAddressFromRedeemScript("redeemScript")).thenReturn("address");
        P2shAddress address = addressesRepository.createNew(keys, 2);
        assertThat(address.getRedeemScript(),is("redeemScript"));
        assertThat(address.getAddress(),is("address"));
    }
}