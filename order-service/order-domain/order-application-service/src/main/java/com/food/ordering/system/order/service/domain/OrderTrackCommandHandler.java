package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.domain.dto.track.TrackOrderQuery;
import com.food.ordering.system.order.service.domain.dto.track.TrackOrderResponse;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.exception.OrderNotFoundExeception;
import com.food.ordering.system.order.service.domain.mapper.OrderObjectMapper;
import com.food.ordering.system.order.service.domain.ports.output.repository.OrderRepository;
import com.food.ordering.system.order.service.domain.valueobject.TrackingId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@Slf4j
public class OrderTrackCommandHandler {

    private final OrderObjectMapper orderObjectMapper;
    private final OrderRepository orderRepository;

    public OrderTrackCommandHandler(OrderObjectMapper orderObjectMapper, OrderRepository orderRepository) {
        this.orderObjectMapper = orderObjectMapper;
        this.orderRepository = orderRepository;

    }

    @Transactional(readOnly = true)
    public TrackOrderResponse trackOrder(TrackOrderQuery trackOrderQuery){

       Optional<Order> optionalOrder =  orderRepository.findByTrackingId(new TrackingId(trackOrderQuery.getOrderTrackingId()));

       if(optionalOrder.isEmpty()){
           log.warn("Could not find order");
           throw new OrderNotFoundExeception("Could not find order");
       }
       return orderObjectMapper.orderToTrackOrderResponse(optionalOrder.get());
    }
}
