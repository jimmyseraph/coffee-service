package vip.testops.coffee.order.services;

import vip.testops.coffee.order.entities.ResponseEntity;
import vip.testops.coffee.order.entities.criteria.OrderCriteria;
import vip.testops.coffee.order.entities.dto.AccountDTO;
import vip.testops.coffee.order.entities.req.OrderCreateRequestEntity;
import vip.testops.coffee.order.entities.vto.OrderDetail;
import vip.testops.coffee.order.entities.vto.OrderList;

/**
 * order service
 * @version 1.0
 * @author liudao
 */
public interface OrderService {
    /**
     * create new order
     * @param accountDTO account entity
     * @param orderCreateRequestEntity request from client
     * @param responseEntity response object
     */
    void doCreateOrder(
            AccountDTO accountDTO,
            OrderCreateRequestEntity orderCreateRequestEntity,
            ResponseEntity<Object> responseEntity
    );

    /**
     * service to modify address
     * @param accountDTO account entity
     * @param orderNbr orderId
     * @param newAddress new address
     * @param responseEntity response object
     */
    void doModifyAddress(
            AccountDTO accountDTO,
            String orderNbr,
            String newAddress,
            ResponseEntity<Object> responseEntity
    );

    /**
     * service to get order list
     * @param accountDTO account entity
     * @param orderCriteria order list criteria
     * @param responseEntity response object
     */
    void doGetOrderList(
            AccountDTO accountDTO,
            OrderCriteria orderCriteria,
            ResponseEntity<OrderList> responseEntity
    );

    /**
     * service to get order details
     * @param accountDTO account entity
     * @param orderNbr order number
     * @param responseEntity response object
     */
    void doGetOrderDetail(
            AccountDTO accountDTO,
            String orderNbr,
            ResponseEntity<OrderDetail> responseEntity
    );
}
