package com.food.ordering.system.order.service.domain.mapper;

import com.food.ordering.system.domain.valueobject.CustomerId;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.domain.valueobject.ProductId;
import com.food.ordering.system.domain.valueobject.RestaurantId;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.dto.create.OrderAddress;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.OrderItem;
import com.food.ordering.system.order.service.domain.entity.Product;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.valueobject.StreetAddress;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class OrderObjectMapper {


   public Restaurant createOrderCommandToRestaurant(CreateOrderCommand createOrderCommand) {

        List<Product> productList = createOrderCommand.getItems().stream().map(p -> new Product(new ProductId(p.getProductId())))
                .toList();

        return new Restaurant.Builder(productList)
                .id(new RestaurantId(createOrderCommand.getRestaurantId()))
                .build();

    }

    public Order createOrderCommandToOrder(CreateOrderCommand createOrderCommand){

       return new Order.Builder(new CustomerId(createOrderCommand.getCustomerId()),
               new RestaurantId(createOrderCommand.getRestaurantId()),
               orderAddressToStreetAddress(createOrderCommand.getAddress()),
               new Money(createOrderCommand.getPrice()),
               orderItemsToOrderItemEntities(createOrderCommand.getItems())
               ).build();
    }

    private List<OrderItem> orderItemsToOrderItemEntities(List<com.food.ordering.system.order.service.domain.dto.create.OrderItem> items) {
       return items.stream()
               .map(p-> OrderItem.builder()
                       .product(new Product(new ProductId(p.getProductId())))
                       .price(new Money(p.getPrice()))
                       .quantity(p.getQuantity())
                       .subtotal(new Money(p.getSubTotal()))
                       .build())
               .toList();
    }

    private StreetAddress orderAddressToStreetAddress(OrderAddress address) {

       return new StreetAddress(UUID.randomUUID(), address.getStreet(), address.getPostalCode(), address.getCity());
    }

    public CreateOrderResponse orderToCreateOrderResponse(Order order, String message){

       return CreateOrderResponse.builder()
               .orderTrackingId(order.getTrackingId().getValue())
               .orderStatus(order.getOrderStatus())
               .message(message)
               .build();
    }


}
