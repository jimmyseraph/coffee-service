package vip.testops.coffee.order.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.testops.coffee.order.entities.ResponseEntity;
import vip.testops.coffee.order.entities.criteria.OrderCriteria;
import vip.testops.coffee.order.entities.dto.AccountDTO;
import vip.testops.coffee.order.entities.dto.CoffeeDTO;
import vip.testops.coffee.order.entities.dto.OrderDTO;
import vip.testops.coffee.order.entities.dto.OrderItemDTO;
import vip.testops.coffee.order.entities.req.OrderCreateRequestEntity;
import vip.testops.coffee.order.entities.req.OrderItemRequestEntity;
import vip.testops.coffee.order.entities.vto.*;
import vip.testops.coffee.order.mappers.CoffeeMapper;
import vip.testops.coffee.order.mappers.OrderItemMapper;
import vip.testops.coffee.order.mappers.OrderMapper;
import vip.testops.coffee.order.message.MessageSender;
import vip.testops.coffee.order.services.OrderService;
import vip.testops.coffee.order.utils.UniqueID;

import java.util.Date;
import java.util.List;

@Slf4j
@Transactional
@Service
public class OrderServiceImpl implements OrderService {

    private OrderMapper orderMapper;
    private OrderItemMapper orderItemMapper;
    private CoffeeMapper coffeeMapper;
    private MessageSender messageSender;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired(required = false)
    public void setOrderMapper(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    @Autowired(required = false)
    public void setOrderItemMapper(OrderItemMapper orderItemMapper) {
        this.orderItemMapper = orderItemMapper;
    }

    @Autowired(required = false)
    public void setCoffeeMapper(CoffeeMapper coffeeMapper) {
        this.coffeeMapper = coffeeMapper;
    }

    @Autowired
    public void setMessageSender(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    @Override
    public void doCreateOrder(AccountDTO accountDTO, OrderCreateRequestEntity orderCreateRequestEntity, ResponseEntity<Object> responseEntity) {
        OrderItemRequestEntity[] orderItems = orderCreateRequestEntity.getOrderItems();
         /*
        check every item data is correct 
         */
        for (int i = 0; i < orderItems.length; i++) {
            if (orderItems[i].getAmount() <= 0 ||
                    coffeeMapper.getCoffeeById(orderItems[i].getCoffeeId()) == null) {
                responseEntity.setCode(3001);
                responseEntity.setMessage("Order item " + i + " is illegal");
                log.info("error order item {}: {} ", i, orderItems[i]);
                return;
            }
        }
        /*
        insert order into order table
         */
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setAddress(orderCreateRequestEntity.getAddress());
        orderDTO.setOrderNbr(UniqueID.getUniqueNumber());
        orderDTO.setOrderStatus(0);
        orderDTO.setBuyerId(accountDTO.getAccountId());
        orderDTO.setCreateTime(new Date());
        if (orderMapper.addOrder(orderDTO) != 1) {
            responseEntity.setCode(4001);
            responseEntity.setMessage("Error occurred while adding order into db.");
            log.error("Error occurred while adding order into db");
            return;
        }
        /*
        insert order items into order_item table
         */
        for (OrderItemRequestEntity orderItem : orderItems) {
            OrderItemDTO orderItemDTO = new OrderItemDTO();
            orderItemDTO.setCoffeeId(orderItem.getCoffeeId());
            orderItemDTO.setAmount(orderItem.getAmount());
            orderItemDTO.setOrderId(orderDTO.getOrderId());
            if (orderItemMapper.addOrderItem(orderItemDTO) != 1) {
                responseEntity.setCode(4001);
                responseEntity.setMessage("Error occurred while adding order item into db.");
                log.error("Error occurred while adding order item into db");
                return;
            }
        }
        // send message to mq
        try {
            String message = objectMapper.writeValueAsString(orderCreateRequestEntity);
            messageSender.send(message);
        } catch (JsonProcessingException e) {
            log.error("Cannot parse Object 'orderCreateRequestEntity' to json string.", e);
        }
        responseEntity.setCode(1000);
        responseEntity.setMessage("Add a new order success.");
    }

    @Override
    public void doModifyAddress(AccountDTO accountDTO, String orderNbr, String newAddress, ResponseEntity<Object> responseEntity) {
        /*
         check the order number is exist
         */
        OrderDTO orderDTO = orderMapper.getOrderByOrderNbr(orderNbr);
        if(orderDTO == null){
            responseEntity.setCode(3001);
            responseEntity.setMessage("Order number is invalid.");
            log.error("Order number is invalid.");
            return;
        }
        /*
        check the order number is belong to this account
         */
        if(orderDTO.getBuyerId() != accountDTO.getAccountId()){
            responseEntity.setCode(3001);
            responseEntity.setMessage("Order number is invalid");
            log.error("This order is not belong to this account " + accountDTO.getAccountName());
            return;
        }
        /*
        modify address
         */
        if(orderMapper.updateAddressByOrderNbr(new Date(), newAddress, orderNbr) != 1){
            responseEntity.setCode(4001);
            responseEntity.setMessage("Error occurred while updating order address in db");
            return;
        }

        responseEntity.setCode(1000);
        responseEntity.setMessage("Modify address success");
    }

    @Override
    public void doGetOrderList(AccountDTO accountDTO, OrderCriteria orderCriteria, ResponseEntity<OrderList> responseEntity) {
        if(orderCriteria == null){
            List<OrderDTO> orders = orderMapper.getOrderListByAccountId(accountDTO.getAccountId());
            responseEntity.setCode(1000);
            responseEntity.setMessage("Success");
            OrderList orderList = new OrderList();
            OrderDTO[] orderDTOS = new OrderDTO[orders.size()];
            orderList.setOrders(orders.toArray(orderDTOS));
            responseEntity.setData(orderList);
            return;
        }
        List<OrderDTO> orders = orderMapper.getOrderListByCriteria(orderCriteria);
        responseEntity.setCode(1000);
        responseEntity.setMessage("success");
        OrderList orderList = new OrderList();
        OrderDTO[] orderDTOS = new OrderDTO[orders.size()];
        orderList.setOrders(orders.toArray(orderDTOS));
        if(orderCriteria.getPageSize() > 0) {
            Pagination pagination = new Pagination();
            pagination.setCurrent(orderCriteria.getCurrent());
            pagination.setPageSize(orderCriteria.getPageSize());
            pagination.setTotal(orderMapper.getOrderNumByCriteria(orderCriteria));
            orderList.setPagination(pagination);
        }
        responseEntity.setData(orderList);

    }

    @Override
    public void doGetOrderDetail(AccountDTO accountDTO, String orderNbr, ResponseEntity<OrderDetail> responseEntity) {
        /*
         check the order number is exist
         */
        OrderDTO orderDTO = orderMapper.getOrderByOrderNbr(orderNbr);
        if(orderDTO == null){
            responseEntity.setCode(3001);
            responseEntity.setMessage("Order number is invalid");
            log.error("Order number is invalid");
            return;
        }
        /*
        check the order number is belong to this account
         */
        if(orderDTO.getBuyerId() != accountDTO.getAccountId()){
            responseEntity.setCode(3001);
            responseEntity.setMessage("Order number is invalid");
            log.error("This order is not belong to this account {}", accountDTO.getAccountName());
            return;
        }
        /*
        prepare response data
         */
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setBuyer(accountDTO.getAccountName());
        orderDetail.setOrderNbr(orderNbr);
        orderDetail.setAddress(orderDTO.getAddress());
        orderDetail.setStatus(OrderStatus.values()[orderDTO.getOrderStatus()].getValue());

        List<OrderItemDTO> orderItemDTOList = orderItemMapper.getItemsByOrderId(orderDTO.getOrderId());
        if(orderItemDTOList == null || orderItemDTOList.size() == 0){
            responseEntity.setCode(3001);
            responseEntity.setMessage("Order item is empty");
            log.error("there are no items in this order {}", orderNbr);
            return;
        }
        OrderItem[] orderItems = new OrderItem[orderItemDTOList.size()];
        double sum = 0.0;
        for(int i = 0; i < orderItemDTOList.size(); i++){
            orderItems[i] = new OrderItem();
            CoffeeDTO coffeeDTO = coffeeMapper.getCoffeeById(orderItemDTOList.get(i).getCoffeeId());
            orderItems[i].setCoffeeName(coffeeDTO.getCoffeeName());
            orderItems[i].setPrice(coffeeDTO.getPrice());
            orderItems[i].setAmount(orderItemDTOList.get(i).getAmount());
            sum += orderItems[i].getAmount() * orderItems[i].getPrice();
        }
        orderDetail.setTotal(sum);
        orderDetail.setOrderItems(orderItems);
        log.info("ready getting order detail: {}", orderDetail);
        responseEntity.setCode(1000);
        responseEntity.setMessage("Get order detail successful.");
        responseEntity.setData(orderDetail);

    }
}
