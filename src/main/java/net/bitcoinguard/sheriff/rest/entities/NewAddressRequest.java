package net.bitcoinguard.sheriff.rest.entities;

import java.util.List;

/**
 * Created by Jiri on 7. 7. 2014.
 */
public class NewAddressRequest {
    List<String> keys;

    public List<String> getKeys() {
        return keys;
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;
    }
}
