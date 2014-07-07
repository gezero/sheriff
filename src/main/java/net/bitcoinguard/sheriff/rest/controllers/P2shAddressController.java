package net.bitcoinguard.sheriff.rest.controllers;

import net.bitcoinguard.sheriff.core.entities.P2shAddress;
import net.bitcoinguard.sheriff.core.services.P2shAddressService;
import net.bitcoinguard.sheriff.rest.entities.P2shAddressResource;
import net.bitcoinguard.sheriff.rest.entities.asm.P2shAddressResourceAsm;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @RequestMapping("/{addressId}")
    public
    @ResponseBody
    P2shAddressResource getAddress(@PathVariable Long addressId) {
        P2shAddress address = service.find(addressId);
        return new P2shAddressResourceAsm().toResource(address);
    }
}
