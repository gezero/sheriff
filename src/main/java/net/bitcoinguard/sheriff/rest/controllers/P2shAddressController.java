package net.bitcoinguard.sheriff.rest.controllers;

import net.bitcoinguard.sheriff.core.entities.Key;
import net.bitcoinguard.sheriff.core.entities.P2shAddress;
import net.bitcoinguard.sheriff.core.entities.Transaction;
import net.bitcoinguard.sheriff.core.services.KeysRepositoryCustom;
import net.bitcoinguard.sheriff.core.services.P2shAddressesRepository;
import net.bitcoinguard.sheriff.rest.entities.P2shAddressResource;
import net.bitcoinguard.sheriff.rest.entities.TransactionResource;
import net.bitcoinguard.sheriff.rest.entities.asm.P2shAddressResourceAsm;
import net.bitcoinguard.sheriff.rest.entities.asm.TransactionResourceAsm;
import net.bitcoinguard.sheriff.rest.exceptions.AddressNotFoundException;
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
    KeysRepositoryCustom keysRepository;

    @Autowired
    public P2shAddressController(P2shAddressesRepository p2shAddressesRepository, KeysRepositoryCustom keysRepository) {
        this.p2shAddressesRepository = p2shAddressesRepository;
        this.keysRepository = keysRepository;

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
        if ( newAddress.getKeys().size() >= newAddress.getTotalKeys()) {
            throw new ToManyKeysException();
        }
        for (int i = newAddress.getKeys().size(); i < newAddress.getTotalKeys(); i++) {
            Key key = keysRepository.generateNewKey();
            newAddress.getKeys().add(key.getPublicKey());
        }
        P2shAddress address = p2shAddressesRepository.createNew(newAddress.getKeys(), newAddress.getRequiredKeys());
        address = p2shAddressesRepository.save(address);
        P2shAddressResource p2shAddressResource = new P2shAddressResourceAsm().toResource(address);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(p2shAddressResource.getLink("self").getHref()));
        return new ResponseEntity<>(p2shAddressResource, headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{addressString}/transactions", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseEntity<TransactionResource> createTransaction(@PathVariable String addressString, @RequestBody TransactionResource transactionResource) {
        P2shAddress address = p2shAddressesRepository.findByAddress(addressString);
        if (address == null){
            throw new AddressNotFoundException();
        }
        Transaction transaction = p2shAddressesRepository.createNewTransaction(address, transactionResource.getTargetAddress(), transactionResource.getAmount());
        TransactionResource resource = new TransactionResourceAsm().toResource(transaction);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(resource.getLink("self").getHref()));
        return new ResponseEntity<>(resource, headers, HttpStatus.CREATED);
    }
}
