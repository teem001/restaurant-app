package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.domain.exception.OrderDomainException;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.entity.Customer;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.event.OrderCreateEvent;
import com.food.ordering.system.order.service.domain.mapper.OrderObjectMapper;
import com.food.ordering.system.order.service.domain.ports.output.repository.CustomerRepository;
import com.food.ordering.system.order.service.domain.ports.output.repository.OrderRepository;
import com.food.ordering.system.order.service.domain.ports.output.repository.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@Slf4j

public class OrderCreateCommandHandler {

    private final OrderRepository orderRepository;
    private final OrderDomainService orderDomainService;
    private final RestaurantRepository restaurantRepository;
    private final CustomerRepository customerRepository;
    private final OrderObjectMapper orderObjectMapper;

    private final ApplicationEventDomainPublisher applicationEventDomainPublisher;

    public OrderCreateCommandHandler(OrderRepository orderRepository,
                                     OrderDomainService orderDomainService,
                                     RestaurantRepository restaurantRepository,
                                     CustomerRepository customerRepository,
                                     OrderObjectMapper orderObjectMapper,
                                     ApplicationEventDomainPublisher applicationEventDomainPublisher) {
        this.orderRepository = orderRepository;
        this.orderDomainService = orderDomainService;
        this.restaurantRepository = restaurantRepository;
        this.customerRepository = customerRepository;
        this.orderObjectMapper = orderObjectMapper;
        this.applicationEventDomainPublisher = applicationEventDomainPublisher;
    }


    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {

        checkCustomer(createOrderCommand.getCustomerId());
        Restaurant restaurant = checkRestaurant(createOrderCommand);
        Order order = orderObjectMapper.createOrderCommandToOrder(createOrderCommand);
        OrderCreateEvent orderCreateEvent = orderDomainService.validateAndInitiateOrder(order, restaurant);
        Order orderResult = saveOrder(order);
        log.info("Order is created with id : {}", orderResult.getId().getValue());
        applicationEventDomainPublisher.publish(orderCreateEvent);
        return orderObjectMapper.orderToCreateOrderResponse(orderCreateEvent.getOrder(), "");

    }

    private Order saveOrder(Order order) {

        return orderRepository.save(order);
    }

    private Restaurant checkRestaurant(CreateOrderCommand createOrderCommand) {

        Restaurant restaurant = orderObjectMapper.createOrderCommandToRestaurant(createOrderCommand);
        Optional<Restaurant> restaurantOptional = restaurantRepository.findRestaurant(restaurant);

        if(restaurantOptional.isEmpty()){
            log.warn("Restaurant with id {}", restaurant.getId());
            throw new OrderDomainException("Restaurant do not exist !!!");
        }
        return  restaurantOptional.get();
    }

    private void checkCustomer(UUID customerId) {

        Optional<Customer> customerOptional = customerRepository.findCustomer(customerId);

        if(customerOptional.isEmpty()){
            log.warn("Customer does not exist with id: {}", customerId);
            throw  new OrderDomainException("Customer does not exist");
        }
    }


}
