package sheriff.core.entities;

import javax.persistence.*;

/**
 * Created by Jiri on 9. 7. 2014.
 */
@Entity
public class Transaction {
    Long id;
    String rawTransaction;
    private Long amount;
    private String targetAddress;
    private P2shAddress sourceAddress;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
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

    @ManyToOne
    public P2shAddress getSourceAddress() {
        return sourceAddress;
    }

    public void setSourceAddress(P2shAddress sourceAddress) {
        this.sourceAddress = sourceAddress;
    }
}
