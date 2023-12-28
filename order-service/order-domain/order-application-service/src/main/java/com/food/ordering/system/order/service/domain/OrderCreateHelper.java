package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.domain.exception.OrderDomainException;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class OrderCreateHelper {
    private final OrderDomainService orderDomainService;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final RestaurantRepository restaurantRepository;
    private final OrderObjectMapper orderObjectMapper;

    public OrderCreateHelper(OrderDomainService orderDomainService,
                             OrderRepository orderRepository,
                             CustomerRepository customerRepository,
                             RestaurantRepository restaurantRepository,
                             OrderObjectMapper orderObjectMapper) {
        this.orderDomainService = orderDomainService;
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.restaurantRepository = restaurantRepository;
        this.orderObjectMapper = orderObjectMapper;
    }


    @Transactional
    public OrderCreateEvent persistOrder(CreateOrderCommand createOrderCommand){

        checkCustomer(createOrderCommand.getCustomerId());
        Restaurant restaurant = checkRestaurant(createOrderCommand);
        Order order = orderObjectMapper.createOrderCommandToOrder(createOrderCommand);

        OrderCreateEvent orderCreateEvent = orderDomainService.validateAndInitiateOrder(order, restaurant);
        saveOrder(order);
        log.info("Order  {}", orderCreateEvent);
        return orderCreateEvent;

    }


    private Restaurant checkRestaurant(CreateOrderCommand createOrderCommand) {

        Restaurant restaurant = orderObjectMapper.createOrderCommandToRestaurant(createOrderCommand);

        Optional<Restaurant> restaurantOptional = restaurantRepository.findRestaurant(restaurant);

        if (restaurantOptional.isEmpty()) {
            log.warn("Restaurant do not exist");
            throw new OrderDomainException("Restaurant do not exist");

        }
        return restaurantOptional.get();

    }

    private void checkCustomer(UUID customerId) {
        Optional<Customer> customer = customerRepository.findCustomer(customerId);

        if (customer.isEmpty()) {
            log.warn("could not find customer with id " + customerId);
            throw new OrderDomainException("could not find customer with id " + customerId);
        }
    }

    private Order saveOrder(Order order) {
        Order orderResult = orderRepository.save(order);

        if (orderResult == null) {
            log.warn("Could not save order");
            throw new OrderDomainException("Could not save order");
        }

        return orderResult;
    }

}
