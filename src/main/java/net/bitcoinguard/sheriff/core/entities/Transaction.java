package net.bitcoinguard.sheriff.core.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by Jiri on 9. 7. 2014.
 */
@Entity
public class Transaction {
    Long id;
    String rawTransaction;
    private Long amount;
    private String targetAddress;
    private String sourceAddrees;

    @Id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRawTransaction() {
        return rawTransaction;
    }

    public void setRawTransaction(String rawTransaction) {
        this.rawTransaction = rawTransaction;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getTargetAddress() {
        return targetAddress;
    }

    public void setTargetAddress(String targetAddress) {
        this.targetAddress = targetAddress;
    }

    public String getSourceAddrees() {
        return sourceAddrees;
    }

    public void setSourceAddrees(String sourceAddrees) {
        this.sourceAddrees = sourceAddrees;
    }
}
