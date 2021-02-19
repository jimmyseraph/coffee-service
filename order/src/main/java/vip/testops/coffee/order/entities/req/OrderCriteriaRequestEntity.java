package vip.testops.coffee.order.entities.req;

import lombok.Data;

@Data
public class OrderCriteriaRequestEntity {
    private String orderNbr;
    private int status = -1;
    private String address;
    private int pageSize;
    private int current;
}
