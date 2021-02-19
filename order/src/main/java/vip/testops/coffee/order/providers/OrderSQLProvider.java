package vip.testops.coffee.order.providers;

import vip.testops.coffee.order.entities.criteria.OrderCriteria;

/**
 * A SQL Provider for select order with criteria
 * @version 1.0
 * @author liudao
 */
public class OrderSQLProvider {

    public String criteriaSelect(OrderCriteria orderCriteria){
        StringBuilder stringBuilder = new StringBuilder("select * from t_order where buyerId = #{accountId} ");
        if(orderCriteria.getOrderNbr() != null){
            stringBuilder.append("and orderNbr like '${orderNbr}%' ");
        }
        if(orderCriteria.getStatus() >= 0){
            stringBuilder.append("and orderStatus = #{status} ");
        }
        if(orderCriteria.getAddress() != null){
            stringBuilder.append("and address like '%${address}%' ");
        }
        if(orderCriteria.getPageSize() > 0){
            int current = orderCriteria.getCurrent() > 0 ? orderCriteria.getCurrent() : 1;
            int start = orderCriteria.getPageSize() * (current - 1);
            stringBuilder.append("limit ")
                    .append(start)
                    .append(", ")
                    .append(orderCriteria.getPageSize());
        }
        return stringBuilder.toString();
    }

    public String criteriaCountSelect(OrderCriteria orderCriteria){
        StringBuilder stringBuilder = new StringBuilder("select count(*) from t_order where buyerId = #{accountId} ");
        if(orderCriteria.getOrderNbr() != null){
            stringBuilder.append("and orderNbr like '${orderNbr}%' ");
        }
        if(orderCriteria.getStatus() >= 0){
            stringBuilder.append("and orderStatus = #{status} ");
        }
        if(orderCriteria.getAddress() != null){
            stringBuilder.append("and address like '%${address}%' ");
        }
        return stringBuilder.toString();
    }
}

