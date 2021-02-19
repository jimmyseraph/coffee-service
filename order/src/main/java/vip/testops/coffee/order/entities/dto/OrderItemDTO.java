package vip.testops.coffee.order.entities.dto;

import lombok.Data;

/**
 * Object mapping to t_order_item table
 * @version 1.0
 * @author liudao
 */
@Data
public class OrderItemDTO {
    private long itemId;
    private long coffeeId;
    private int amount;
    private long orderId;
}

