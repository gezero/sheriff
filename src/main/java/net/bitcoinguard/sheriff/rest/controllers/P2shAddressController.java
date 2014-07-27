package net.bitcoinguard.sheriff.rest.controllers;

import net.bitcoinguard.sheriff.core.entities.Key;
import net.bitcoinguard.sheriff.core.entities.P2shAddress;
import net.bitcoinguard.sheriff.core.services.KeysRepository;
import net.bitcoinguard.sheriff.core.services.P2shAddressesRepository;
import net.bitcoinguard.sheriff.core.services.TransactionsRepository;
import net.bitcoinguard.sheriff.rest.entities.P2shAddressResource;
import net.bitcoinguard.sheriff.rest.entities.asm.P2shAddressResourceAsm;
import net.bitcoinguard.sheriff.rest.exceptions.NoKeysProvidedException;
import net.bitcoinguard.sheriff.rest.exceptions.NotFoundException;
import net.bitcoinguard.sheriff.rest.exceptions.ToManyKeysException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

/**
 * Created by Jiri on 7. 7. 2014.
 */
@Controller
@RequestMapping("/rest/addresses")
public class P2shAddressController {
    P2shAddressesRepository p2shAddressesRepository;
    KeysRepository keysRepository;
    TransactionsRepository transactionsRepository;

    @Autowired
    public P2shAddressController(P2shAddressesRepository p2shAddressesRepository, KeysRepository keysRepository, TransactionsRepository transactionsRepository) {
        this.p2shAddressesRepository = p2shAddressesRepository;
        this.keysRepository = keysRepository;
        this.transactionsRepository = transactionsRepository;

    }

    @RequestMapping(value = "/{addressId}", method = RequestMethod.GET)
    public
    @ResponseBody
    P2shAddressResource getAddress(@PathVariable String addressId) {
        P2shAddress address = p2shAddressesRepository.findByAddress(addressId);
        if (address != null) {
            return new P2shAddressResourceAsm().toResource(address);
        }
        throw new NotFoundException();
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseEntity<P2shAddressResource> createNewAddress(@RequestBody P2shAddressResource newAddress) {
        if (newAddress.getKeys() == null) {
            throw new NoKeysProvidedException();
        }
        if (newAddress.getKeys().size() >= newAddress.getTotalKeys()) {
            throw new ToManyKeysException();
        }
        for (int i = newAddress.getKeys().size(); i < newAddress.getTotalKeys(); i++) {
            Key key = keysRepository.generateNewKey();
            key = keysRepository.save(key);
            newAddress.getKeys().add(key.getPublicKey());
        }
        P2shAddress address = p2shAddressesRepository.createNew(newAddress.getKeys(), newAddress.getRequiredKeys());
        address = p2shAddressesRepository.save(address);
        P2shAddressResource p2shAddressResource = new P2shAddressResourceAsm().toResource(address);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(p2shAddressResource.getLink("self").getHref()));
        return new ResponseEntity<>(p2shAddressResource, headers, HttpStatus.CREATED);
    }
}

