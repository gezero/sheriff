package net.bitcoinguard.sheriff.rest.entities.asm;

import net.bitcoinguard.sheriff.core.entities.P2shAddress;
import net.bitcoinguard.sheriff.rest.controllers.KeyController;
import net.bitcoinguard.sheriff.rest.controllers.P2shAddressController;
import net.bitcoinguard.sheriff.rest.entities.P2shAddressResource;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by Jiri on 7. 7. 2014.
 */
@Component
public class P2shAddressResourceAsm extends ResourceAssemblerSupport<P2shAddress, P2shAddressResource> {

    public P2shAddressResourceAsm() {
        super(KeyController.class, P2shAddressResource.class);
    }

    @Override
    public P2shAddressResource toResource(P2shAddress entity) {
        P2shAddressResource resource = new P2shAddressResource();
        resource.setAddress(entity.getAddress());
        resource.setRedeemScript(entity.getRedeemScript());
        //todo: add keys
        Link link = linkTo(methodOn(P2shAddressController.class).getAddress(entity.getAddress())).withSelfRel();
        resource.add(link);
        //todo: add links to other entities in address
        return resource;
    }
}
