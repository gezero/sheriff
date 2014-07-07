package net.bitcoinguard.sheriff.rest.controllers;

import net.bitcoinguard.sheriff.core.entities.Key;
import net.bitcoinguard.sheriff.core.services.KeyService;
import net.bitcoinguard.sheriff.rest.entities.KeyResource;
import net.bitcoinguard.sheriff.rest.entities.asm.KeyResourceAsm;
import net.bitcoinguard.sheriff.rest.exceptions.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Controller responsible for handling keys.
 * Created by Jiri on 5. 7. 2014.
 */
@Controller
@RequestMapping("/rest/keys")
public class KeyController {

    private KeyService keyService;

    public KeyController(KeyService keyService) {
        this.keyService = keyService;
    }

    @RequestMapping(value = "/{keyId}", method = RequestMethod.GET)
    public @ResponseBody KeyResource getKey(@PathVariable Long keyId) {
        Key key = keyService.find(keyId);

        if (key != null) {
            KeyResource keyResource = new KeyResourceAsm().toResource(key);

            return keyResource;
        }
        throw new NotFoundException();
    }
}
