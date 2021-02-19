package vip.testops.coffee.order.entities.vto;

import lombok.Data;

@Data
public class OrderDetail {
    private String orderNbr;
    private String buyer;
    private String address;
    private String status;
    private double total;
    private OrderItem[] orderItems;
}
