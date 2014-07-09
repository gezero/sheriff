package net.bitcoinguard.sheriff.rest.entities.asm;

import net.bitcoinguard.sheriff.core.entities.Transaction;
import net.bitcoinguard.sheriff.rest.controllers.P2shAddressController;
import net.bitcoinguard.sheriff.rest.controllers.TransactionController;
import net.bitcoinguard.sheriff.rest.entities.TransactionResource;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by Jiri on 9. 7. 2014.
 */
public class TransactionResourceAsm extends ResourceAssemblerSupport<Transaction,TransactionResource> {
    public TransactionResourceAsm() {
        super(TransactionController.class, TransactionResource.class);
    }

    @Override
    public TransactionResource toResource(Transaction entity) {
        TransactionResource resource = new TransactionResource();
        resource.setAmount(entity.getAmount());
        resource.setTargetAddress(entity.getTargetAddress());
        resource.setSourceAddress(entity.getSourceAddrees());
        Link link = linkTo(methodOn(TransactionController.class).getTransaction(entity.getId())).withSelfRel();
        //todo: add links to other entities in transaction
        resource.add(link);
        return resource;
    }
}
