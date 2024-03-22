package com.food.ordering.system.payment.service.domain.valueobject;

import com.food.ordering.system.domain.valueobject.BaseId;

import java.util.UUID;

public class CreditHistoryID extends BaseId<UUID> {


    public CreditHistoryID(UUID value) {
        super(value);
    }
}
