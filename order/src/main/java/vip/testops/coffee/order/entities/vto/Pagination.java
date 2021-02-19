package vip.testops.coffee.order.entities.vto;

import lombok.Data;

@Data
public class Pagination {
    private int total;
    private int pageSize;
    private int current;
}

