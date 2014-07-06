package net.bitcoinguard.sheriff.rest.controllers;

import net.bitcoinguard.sheriff.rest.entities.KeyResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Controller responsible for handling keys.
 * Created by Jiri on 5. 7. 2014.
 */
@Controller
@RequestMapping("/keys")
public class KeyController {

    @RequestMapping("/{publicKey}")
    public @ResponseBody KeyResource getKey(@PathVariable String publicKey) {
        return new KeyResource(publicKey, "private");
    }

    @RequestMapping(value="",method = RequestMethod.POST)
    public @ResponseBody KeyResource storeKey(@RequestBody KeyResource key) {
        return key;
    }
}
