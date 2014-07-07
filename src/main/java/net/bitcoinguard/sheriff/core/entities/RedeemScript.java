package net.bitcoinguard.sheriff.core.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

/**
 * Created by Jiri on 7. 7. 2014.
 */
@Entity
public class RedeemScript {

    private Long id;
    private String script;
    private List<Key> keys;


    @Id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public List<Key> getKeys() {
        return keys;
    }

    public void setKeys(List<Key> keys) {
        this.keys = keys;
    }
}
