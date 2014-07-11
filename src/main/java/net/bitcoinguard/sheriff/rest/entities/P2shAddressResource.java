package net.bitcoinguard.sheriff.rest.entities;

import org.springframework.hateoas.ResourceSupport;

import java.util.List;

/**
 * Created by Jiri on 7. 7. 2014.
 */
public class P2shAddressResource extends ResourceSupport {
    String address;
    String redeemScript;
    Integer totalKeys = 3;
    Integer requiredKeys = 2;
    List<String> keys;

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

    public Integer getTotalKeys() {
        return totalKeys;
    }

    public void setTotalKeys(Integer totalKeys) {
        this.totalKeys = totalKeys;
    }

    public Integer getRequiredKeys() {
        return requiredKeys;
    }

    public void setRequiredKeys(Integer requiredKeys) {
        this.requiredKeys = requiredKeys;
    }

    public List<String> getKeys() {
        return keys;
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;
    }
}
