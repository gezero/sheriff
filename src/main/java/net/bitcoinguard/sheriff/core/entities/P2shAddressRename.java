package net.bitcoinguard.sheriff.core.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by Jiri on 7. 7. 2014.
 */
@Entity
public class P2shAddressRename {
    private Long id;
    private String address;
    private RedeemScript script;

    @Id
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

    public RedeemScript getScript() {
        return script;
    }

    public void setScript(RedeemScript script) {
        this.script = script;
    }
}
