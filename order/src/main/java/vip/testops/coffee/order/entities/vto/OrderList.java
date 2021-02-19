package vip.testops.coffee.order.entities.vto;

import lombok.Data;
import vip.testops.coffee.order.entities.dto.OrderDTO;

@Data
public class OrderList {
    private OrderDTO[] orders;
    private Pagination pagination;
}
