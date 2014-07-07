package net.bitcoinguard.sheriff.rest.entities;

import org.springframework.hateoas.ResourceSupport;

/**
 * Key resource represents a key.
 * Created by Jiri on 5. 7. 2014.
 */
public class KeyResource extends ResourceSupport{
    private String publicKey;
    private String privateKey;

    public KeyResource() {
        //for serialization
    }

    public KeyResource(String publicKey, String privateKey) {

        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }
}
