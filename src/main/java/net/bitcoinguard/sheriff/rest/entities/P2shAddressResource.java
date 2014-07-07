package net.bitcoinguard.sheriff.rest.entities;

import org.springframework.hateoas.ResourceSupport;

/**
 * Created by Jiri on 7. 7. 2014.
 */
public class P2shAddressResource extends ResourceSupport {
    String address;
    String redeemScript;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRedeemScript() {
        return redeemScript;
    }

    public void setRedeemScript(String redeemScript) {
        this.redeemScript = redeemScript;
    }
}
