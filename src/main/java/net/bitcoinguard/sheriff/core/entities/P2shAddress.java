package net.bitcoinguard.sheriff.core.entities;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Jiri on 7. 7. 2014.
 */
@Entity
public class P2shAddress {
    private Long id;
    private String address;
    private String redeemScript;
    private List<Key> keys;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    @ManyToMany(cascade = CascadeType.ALL) //TODO: I am pretty sure this is wrong... Am I not overriding all keys sometimes?
    @JoinTable(name="ADDRESS_KEYS")
    public List<Key> getKeys() {
        return keys;
    }

    public void setKeys(List<Key> keys) {
        this.keys = keys;
    }
}
