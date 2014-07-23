package net.bitcoinguard.sheriff.rest.controllers;

import net.bitcoinguard.sheriff.bitcoin.service.impl.BitcoinJMagicService;
import net.bitcoinguard.sheriff.core.entities.Key;
import net.bitcoinguard.sheriff.core.entities.P2shAddress;
import net.bitcoinguard.sheriff.core.entities.Transaction;
import net.bitcoinguard.sheriff.core.services.P2shAddressesRepository;
import net.bitcoinguard.sheriff.core.services.TransactionsRepository;
import net.bitcoinguard.sheriff.rest.entities.TransactionResource;
import net.bitcoinguard.sheriff.rest.entities.asm.TransactionResourceAsm;
import net.bitcoinguard.sheriff.rest.exceptions.AddressNotFoundException;
import net.bitcoinguard.sheriff.rest.exceptions.NotFoundException;
import net.bitcoinguard.sheriff.rest.exceptions.SomehtingGotWrongException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * Created by Jiri on 9. 7. 2014.
 */
@RequestMapping("/rest/transactions")
public class TransactionController {
    TransactionsRepository transactionsRepository;
    BitcoinJMagicService bitcoinJMagicService;
    P2shAddressesRepository p2shAddressesRepository;

    @Autowired
    public TransactionController(TransactionsRepository transactionsRepository, BitcoinJMagicService bitcoinJMagicService, P2shAddressesRepository p2shAddressesRepository) {
        this.transactionsRepository = transactionsRepository;
        this.bitcoinJMagicService = bitcoinJMagicService;
        this.p2shAddressesRepository = p2shAddressesRepository;
    }

    @RequestMapping(value = "/{transactionId}", method = RequestMethod.GET)
    public
    @ResponseBody
    TransactionResource getTransaction(@PathVariable Long transactionId) {
        Transaction transaction = transactionsRepository.findOne(transactionId);
        if (transaction == null) {
            throw new NotFoundException();
        }
        return new TransactionResourceAsm().toResource(transaction);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseEntity<TransactionResource> createTransaction(@RequestBody TransactionResource transactionResource) {
        P2shAddress address = p2shAddressesRepository.findByAddress(transactionResource.getSourceAddress());
        if (address == null) {
            throw new AddressNotFoundException();
        }
        Transaction transaction = p2shAddressesRepository.createNewTransaction(address, transactionResource.getTargetAddress(), transactionResource.getAmount());
        transaction = transactionsRepository.save(transaction);
        TransactionResource resource = new TransactionResourceAsm().toResource(transaction);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(resource.getLink("self").getHref()));
        return new ResponseEntity<>(resource, headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{transactionId}", method = RequestMethod.POST)
    public
    @ResponseBody
    TransactionResource signTransaction(@RequestBody TransactionResource transactionResource, @PathVariable Long transactionId) {
        Transaction transaction = transactionsRepository.findOne(transactionId);
        if (transaction == null) {
            throw new NotFoundException();
        }
        //todo: verify transaction
        List<Key> keys = transaction.getSourceAddress().getKeys();
        for (Key key : keys) {
            if (key.getPrivateKey() != null) {
                transaction.setRawTransaction(bitcoinJMagicService.addSignature(transactionResource.getRawTransaction(), key.getPrivateKey()));
                return new TransactionResourceAsm().toResource(transaction);
            }
        }
        throw new SomehtingGotWrongException();
    }
}
