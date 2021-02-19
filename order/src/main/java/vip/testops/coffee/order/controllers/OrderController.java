package vip.testops.coffee.order.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vip.testops.coffee.order.entities.ResponseEntity;
import vip.testops.coffee.order.entities.criteria.OrderCriteria;
import vip.testops.coffee.order.entities.dto.AccountDTO;
import vip.testops.coffee.order.entities.req.OrderCreateRequestEntity;
import vip.testops.coffee.order.entities.req.OrderCriteriaRequestEntity;
import vip.testops.coffee.order.entities.vto.OrderDetail;
import vip.testops.coffee.order.entities.vto.OrderList;
import vip.testops.coffee.order.message.MessageSender;
import vip.testops.coffee.order.services.OrderService;
import vip.testops.coffee.order.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.nio.channels.FileChannel;

/**
 * order controller parse request from client which ask for order service
 * @version 1.0
 * @author liudao
 */
@RestController
@Slf4j
@RequestMapping("/order")
public class OrderController {
    private OrderService orderService;
    private MessageSender messageSender;

    @Autowired
    public void setMessageSender(MessageSender messageSender) {
        this.messageSender = messageSender;
    }
    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * create order api
     * @param orderCreateRequestEntity request json object
     * @param request request passing from filter
     * @return response object
     */
    @PostMapping("/new")
    @ResponseBody
    public ResponseEntity<Object> createOrder(
            @RequestBody OrderCreateRequestEntity orderCreateRequestEntity,
            HttpServletRequest request
    ){
        ResponseEntity<Object> responseEntity = new ResponseEntity<>();
        /*
        check address
         */
        if(StringUtils.isEmptyOrNull(orderCreateRequestEntity.getAddress())){
            responseEntity.setCode(2001);
            responseEntity.setMessage("Deliver address is missing");
            return responseEntity;
        }
        if(orderCreateRequestEntity.getAddress().length() > 50){
            responseEntity.setCode(2001);
            responseEntity.setMessage("Deliver address is too long");
            return responseEntity;
        }
        /*
        check items
         */
        if(orderCreateRequestEntity.getOrderItems() == null || orderCreateRequestEntity.getOrderItems().length == 0){
            responseEntity.setCode(2001);
            responseEntity.setMessage("Order need at least one coffee.");
            return responseEntity;
        }
        AccountDTO accountDTO = (AccountDTO) request.getAttribute("accountDTO");
        orderService.doCreateOrder(accountDTO, orderCreateRequestEntity, responseEntity);
        return responseEntity;
    }

    /**
     * modify order address api
     * @param orderNbr order number
     * @param address new address
     * @param request request passing from filter
     * @return response object
     */
    @GetMapping("/{orderNbr}/modify")
    @ResponseBody
    public ResponseEntity<Object> modify(
            @PathVariable("orderNbr") String orderNbr,
            @RequestParam(value = "address", required = false) String address,
            HttpServletRequest request
    ){
        ResponseEntity<Object> responseEntity = new ResponseEntity<>();
        /*
        check address param
         */
        if(StringUtils.isEmptyOrNull(address)){
            responseEntity.setCode(2001);
            responseEntity.setMessage("Address is null.");
            return responseEntity;
        }
        if(address.length() > 50){
            responseEntity.setCode(2001);
            responseEntity.setMessage("Address is too long.");
            return responseEntity;
        }
        /*
        get account info from request attribute
         */
        AccountDTO accountDTO = (AccountDTO) request.getAttribute("accountDTO");
        orderService.doModifyAddress(accountDTO, orderNbr, address, responseEntity);
        return responseEntity;
    }

    @PostMapping("/list")
    @ResponseBody
    public ResponseEntity<OrderList> orderList(
            @RequestBody OrderCriteriaRequestEntity orderCriteriaRequestEntity,
            HttpServletRequest request
    ){
        ResponseEntity<OrderList> responseEntity = new ResponseEntity<>();
        /*
        get account info from request attribute
         */
        AccountDTO accountDTO = (AccountDTO) request.getAttribute("accountDTO");
        OrderCriteria orderCriteria = null;
        if(orderCriteriaRequestEntity != null){
            orderCriteria = new OrderCriteria();
            orderCriteria.setAccountId(accountDTO.getAccountId());
            orderCriteria.setAddress(orderCriteriaRequestEntity.getAddress());
            orderCriteria.setOrderNbr(orderCriteriaRequestEntity.getOrderNbr());
            orderCriteria.setStatus(orderCriteriaRequestEntity.getStatus());
            orderCriteria.setPageSize(orderCriteriaRequestEntity.getPageSize());
            orderCriteria.setCurrent(orderCriteriaRequestEntity.getCurrent());
        }

        orderService.doGetOrderList(accountDTO, orderCriteria, responseEntity);
        return responseEntity;
    }

    /**
     * get order details
     * @param orderNbr order number
     * @param request  request passing from filter
     * @return response object
     */
    @GetMapping("/{orderNbr}/detail")
    @ResponseBody
    public ResponseEntity<OrderDetail> orderDetail(
            @PathVariable("orderNbr") String orderNbr,
            HttpServletRequest request
    ){
        ResponseEntity<OrderDetail> responseEntity = new ResponseEntity<>();
        AccountDTO accountDTO = (AccountDTO) request.getAttribute("accountDTO");
        orderService.doGetOrderDetail(accountDTO, orderNbr, responseEntity);
        return responseEntity;
    }

    @GetMapping("/test")
    @ResponseBody
    public ResponseEntity<?> test(@RequestParam("str") String str){
        ResponseEntity<?> responseEntity = new ResponseEntity<>();
        responseEntity.setCode(1000);
        responseEntity.setMessage("success");
        messageSender.send(str);
        return responseEntity;
    }

}
