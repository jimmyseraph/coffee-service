package vip.testops.coffee.order.entities.vto;

import lombok.Data;

@Data
public class OrderItem {
    private String coffeeName;
    private double price;
    private int amount;
}
