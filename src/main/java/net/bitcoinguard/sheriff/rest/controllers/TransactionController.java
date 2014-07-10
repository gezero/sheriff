package net.bitcoinguard.sheriff.rest.controllers;

import net.bitcoinguard.sheriff.core.entities.Transaction;
import net.bitcoinguard.sheriff.core.services.TransactionsService;
import net.bitcoinguard.sheriff.rest.entities.TransactionResource;
import net.bitcoinguard.sheriff.rest.entities.asm.TransactionResourceAsm;
import net.bitcoinguard.sheriff.rest.exceptions.NotFoundException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Jiri on 9. 7. 2014.
 */
@RequestMapping("/rest/transactions")
public class TransactionController {
    TransactionsService transactionsService;

    public TransactionController(TransactionsService transactionsService) {
        this.transactionsService = transactionsService;
    }

    @RequestMapping("/{transactionId}")
    public @ResponseBody TransactionResource getTransaction(@PathVariable Long transactionId) {
        Transaction transaction = transactionsService.findOne(transactionId);
        if (transaction == null){
            throw new NotFoundException();
        }
        return new TransactionResourceAsm().toResource(transaction);
    }
}
