package vip.testops.coffee.order.entities.criteria;

import lombok.Data;

@Data
public class OrderCriteria {
    private String orderNbr;
    private int status;
    private String address;
    private int pageSize;
    private int current;
    private long accountId;
}
