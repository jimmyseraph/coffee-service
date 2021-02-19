package vip.testops.coffee.order.entities.dto;

import lombok.Data;

/**
 * Object mapping to t_coffee table
 * @version 1.0
 * @author liudao
 */
@Data
public class CoffeeDTO {
    private long coffeeId;
    private String coffeeName;
    private double price;
}

