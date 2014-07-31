package sheriff.rest.controllers;

import sheriff.core.entities.Key;
import sheriff.core.services.KeysRepository;
import sheriff.rest.entities.KeyResource;
import sheriff.rest.entities.asm.KeyResourceAsm;
import sheriff.rest.exceptions.NotFoundException;
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
