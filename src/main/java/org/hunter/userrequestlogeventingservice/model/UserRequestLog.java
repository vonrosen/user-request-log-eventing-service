package org.hunter.userrequestlogeventingservice.model;

import java.math.BigInteger;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("user_request_log")
public class UserRequestLog {

    @Id
    private UUID id;
    @Column(value = "user_id")
    private UUID userId;
    @Column(value = "max_payment_value_cents")
    private BigInteger maxPaymentAmountCents;

    public UserRequestLog(UUID id, UUID userId, BigInteger maxPaymentAmountCents) {
        this.id = id;
        this.userId = userId;
        this.maxPaymentAmountCents = maxPaymentAmountCents;
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public BigInteger getMaxPaymentAmountCents() {
        return maxPaymentAmountCents;
    }
}
