package vip.testops.coffee.order.entities.dto;

import lombok.Data;

import java.util.Date;

/**
 * Object mapping to t_order table
 * @version 1.0
 * @author liudao
 */
@Data
public class OrderDTO {
    private long orderId;
    private String orderNbr;
    private int orderStatus;
    private long buyerId;
    private String address;
    private Date createTime;
    private Date updateTime;
}

