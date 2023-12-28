package com.food.ordering.system.order.service.domain.dto.track;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Builder
@Getter
@AllArgsConstructor
public class TrackOrderResponse {

    private final UUID orderTrackingId;
    private final List<String> failureMessage;
}
