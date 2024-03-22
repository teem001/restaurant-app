package com.food.ordering.system.payment.service.domain.valueobject;

import com.food.ordering.system.domain.valueobject.BaseId;

import java.util.UUID;

public class CreditEntryID extends BaseId<UUID> {
    public CreditEntryID(UUID uuid) {
        super(uuid);
    }
}
