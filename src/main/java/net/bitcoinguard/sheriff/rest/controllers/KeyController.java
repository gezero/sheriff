package net.bitcoinguard.sheriff.rest.controllers;

import net.bitcoinguard.sheriff.core.entities.Key;
import net.bitcoinguard.sheriff.core.services.KeysRepository;
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

    private KeysRepository keysRepository;

    public KeyController(KeysRepository keysRepository) {
        this.keysRepository = keysRepository;
    }

    @RequestMapping(value = "/{keyId}", method = RequestMethod.GET)
    public @ResponseBody KeyResource getKey(@PathVariable Long keyId) {
        Key key = keysRepository.findOne(keyId);

        if (key != null) {
            KeyResource keyResource = new KeyResourceAsm().toResource(key); //todo: create bean out of this...

            return keyResource;
        }
        throw new NotFoundException();
    }
}
