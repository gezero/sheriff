package net.bitcoinguard.sheriff.rest.entities.asm;

import net.bitcoinguard.sheriff.core.entities.Key;
import net.bitcoinguard.sheriff.rest.controllers.KeyController;
import net.bitcoinguard.sheriff.rest.entities.KeyResource;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by Jiri on 7. 7. 2014.
 */
@Component
public class KeyResourceAsmRename extends ResourceAssemblerSupport<Key, KeyResource> {

    public KeyResourceAsmRename() {
        super(KeyController.class, KeyResource.class);
    }

    @Override
    public KeyResource toResource(Key entity) {
        KeyResource resource = new KeyResource();
        resource.setPublicKey(entity.getPublicKey());
        resource.setPrivateKey(entity.getPrivateKey());
        Link link = linkTo(methodOn(KeyController.class).getKey(entity.getId())).withSelfRel();
        resource.add(link);
        return resource;
    }
}
