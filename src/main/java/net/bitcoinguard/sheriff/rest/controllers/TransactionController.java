package net.bitcoinguard.sheriff.rest.controllers;

import net.bitcoinguard.sheriff.bitcoin.service.impl.BitcoinJMagicService;
import net.bitcoinguard.sheriff.core.entities.Key;
import net.bitcoinguard.sheriff.core.entities.Transaction;
import net.bitcoinguard.sheriff.core.services.TransactionsRepository;
import net.bitcoinguard.sheriff.rest.entities.TransactionResource;
import net.bitcoinguard.sheriff.rest.entities.asm.TransactionResourceAsm;
import net.bitcoinguard.sheriff.rest.exceptions.NotFoundException;
import net.bitcoinguard.sheriff.rest.exceptions.SomehtingGotWrongException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Jiri on 9. 7. 2014.
 */
@RequestMapping("/rest/transactions")
public class TransactionController {
    TransactionsRepository transactionsRepository;
    BitcoinJMagicService bitcoinJMagicService;

    @Autowired
    public TransactionController(TransactionsRepository transactionsRepository, BitcoinJMagicService bitcoinJMagicService) {
        this.transactionsRepository = transactionsRepository;
        this.bitcoinJMagicService = bitcoinJMagicService;
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
