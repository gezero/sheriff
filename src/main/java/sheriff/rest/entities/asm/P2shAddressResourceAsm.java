package sheriff.rest.entities.asm;

import sheriff.core.entities.Key;
import sheriff.core.entities.P2shAddress;
import sheriff.rest.controllers.KeyController;
import sheriff.rest.controllers.P2shAddressController;
import sheriff.rest.entities.P2shAddressResource;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
        resource.setBalance(entity.getBalance());
        Link link = linkTo(methodOn(P2shAddressController.class).getAddress(entity.getAddress())).withSelfRel();
        resource.add(link);

        List<String> keys = new ArrayList<>();
        for (Key key : entity.getKeys()) {
            keys.add(key.getPublicKey());
            //todo: add key links
        }
        resource.setKeys(keys);
        return resource;
    }
}
