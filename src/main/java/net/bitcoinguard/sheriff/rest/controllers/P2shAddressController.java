package net.bitcoinguard.sheriff.rest.controllers;

import net.bitcoinguard.sheriff.core.entities.P2shAddress;
import net.bitcoinguard.sheriff.core.services.P2shAddressService;
import net.bitcoinguard.sheriff.rest.entities.NewAddressRequest;
import net.bitcoinguard.sheriff.rest.entities.P2shAddressResource;
import net.bitcoinguard.sheriff.rest.entities.asm.P2shAddressResourceAsm;
import net.bitcoinguard.sheriff.rest.exceptions.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Jiri on 7. 7. 2014.
 */
@Controller
@RequestMapping("/rest/addresses")
public class P2shAddressController {
    P2shAddressService service;

    public P2shAddressController(P2shAddressService service) {
        this.service = service;
    }

    @RequestMapping(value = "/{addressId}", method = RequestMethod.GET)
    public
    @ResponseBody
    P2shAddressResource getAddress(@PathVariable Long addressId) {
        P2shAddress address = service.find(addressId);
        if (address != null) {
            return new P2shAddressResourceAsm().toResource(address);
        }
        throw new NotFoundException();
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public
    @ResponseBody
    P2shAddressResource createNewAddress(@RequestBody NewAddressRequest newAddressRequest) {
        P2shAddress address = service.createNew(newAddressRequest.getKeys());
        if (address != null) {
            return new P2shAddressResourceAsm().toResource(address);
        }
        throw new NotFoundException();
    }
}
