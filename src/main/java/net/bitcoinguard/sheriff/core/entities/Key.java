package net.bitcoinguard.sheriff.core.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Key entity represents public/private key pair used to sign things.
 * Created by Jiri on 5. 7. 2014.
 */
@Entity
public class Key {
    Long id;
    String publicKey;
    String privatekey;


    @Id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivatekey() {
        return privatekey;
    }

    public void setPrivatekey(String privatekey) {
        this.privatekey = privatekey;
    }
}
