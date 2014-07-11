package net.bitcoinguard.sheriff.rest.controllers;

import net.bitcoinguard.sheriff.core.entities.Key;
import net.bitcoinguard.sheriff.core.services.KeysRepository;
import net.bitcoinguard.sheriff.rest.entities.KeyResource;
import net.bitcoinguard.sheriff.rest.entities.asm.KeyResourceAsm;
import net.bitcoinguard.sheriff.rest.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller responsible for handling keys.
 * Created by Jiri on 5. 7. 2014.
 */
@Controller
@RequestMapping("/rest/keys")
public class KeyController {

    private KeysRepository keysRepository;

    @Autowired
    public KeyController(KeysRepository keysRepository) {
        this.keysRepository = keysRepository;
    }

    @RequestMapping(value = "/{publicKey}", method = RequestMethod.GET)
    public
    @ResponseBody
    KeyResource getKey(@PathVariable String publicKey) {
        Key key = keysRepository.findByPublicKey(publicKey);

        if (key != null) {
            return new KeyResourceAsm().toResource(key);
        }
        throw new NotFoundException();
    }
}
