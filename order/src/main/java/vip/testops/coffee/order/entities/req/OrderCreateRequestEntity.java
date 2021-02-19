package vip.testops.coffee.order.entities.req;

import lombok.Data;

@Data
public class OrderCreateRequestEntity {
    private String address;
    private OrderItemRequestEntity[] orderItems;
}
