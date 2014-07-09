package net.bitcoinguard.sheriff.rest.controllers;

import net.bitcoinguard.sheriff.rest.entities.TransactionResource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Jiri on 9. 7. 2014.
 */
@RequestMapping("/transactions")
public class TransactionController {
    @RequestMapping("/{transactionId}")
    public TransactionResource getTransaction(@PathVariable Long transactionId) {
        return null;
    }
}
