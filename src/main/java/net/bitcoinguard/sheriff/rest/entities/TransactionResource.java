package net.bitcoinguard.sheriff.rest.entities;

import org.springframework.hateoas.ResourceSupport;

/**
 * Created by Jiri on 9. 7. 2014.
 */
public class TransactionResource extends ResourceSupport {
    private String targetAddress;
    private Long amount;
    private String sourceAddress;
    private String rawTransaction;

    public String getTargetAddress() {
        return targetAddress;
    }

    public void setTargetAddress(String targetAddress) {
        this.targetAddress = targetAddress;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public void setSourceAddress(String sourceAddress) {
        this.sourceAddress = sourceAddress;
    }

    public String getSourceAddress() {
        return sourceAddress;
    }

    public void setRawTransaction(String rawTransaction) {
        this.rawTransaction = rawTransaction;
    }

    public String getRawTransaction() {
        return rawTransaction;
    }
}
