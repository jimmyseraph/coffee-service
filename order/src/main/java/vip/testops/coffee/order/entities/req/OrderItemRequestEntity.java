package vip.testops.coffee.order.entities.req;

import lombok.Data;

@Data
public class OrderItemRequestEntity {
    private long coffeeId;
    private int amount;
}
