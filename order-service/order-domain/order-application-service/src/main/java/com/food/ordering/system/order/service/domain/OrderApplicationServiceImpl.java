package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.dto.track.TrackOrderQuery;
import com.food.ordering.system.order.service.domain.dto.track.TrackOrderResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import com.food.ordering.system.order.service.domain.ports.input.service.OrderApplicationService;

import javax.validation.Valid;

@Service
@Validated
@Slf4j
@AllArgsConstructor
class OrderApplicationServiceImpl implements OrderApplicationService {

    private final OrderCreateCommandHandler orderCreateCommandHandler;

    private final OrderTrackCommandHandler orderTrackCommandHandler;


    @Override
    public CreateOrderResponse createOrder(@Valid  CreateOrderCommand createOrderCommand) {

        return orderCreateCommandHandler.createOrder(createOrderCommand);
    }


    @Override
    public TrackOrderResponse trackOrder(@Valid TrackOrderQuery trackOrderQuery) {

        return orderTrackCommandHandler.trackOrder(trackOrderQuery);
    }
}
