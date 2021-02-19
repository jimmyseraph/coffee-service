package vip.testops.coffee.order.mappers;

import org.apache.ibatis.annotations.*;
import vip.testops.coffee.order.entities.criteria.OrderCriteria;
import vip.testops.coffee.order.entities.dto.OrderDTO;
import vip.testops.coffee.order.providers.OrderSQLProvider;

import java.util.Date;
import java.util.List;

@Mapper
public interface OrderMapper {
    @Insert("insert into t_order values(" +
            "null, " +
            "#{orderNbr}, " +
            "#{orderStatus}, " +
            "#{buyerId}, " +
            "#{address}, " +
            "#{createTime}, " +
            "null)")
    @SelectKey(statement = "select last_insert_id()", keyProperty = "orderId", before = false, resultType = long.class)
    int addOrder(OrderDTO orderDTO);

    @Select("select * from t_order where orderNbr = #{orderNbr}")
    OrderDTO getOrderByOrderNbr(String orderNbr);

    @Update("update t_order set address = #{address}, updateTime = #{updateTime} where orderNbr = #{orderNbr}")
    int updateAddressByOrderNbr(Date updateTime, String address, String orderNbr);

    @Select("select * from t_order where buyerId = #{accountId}")
    List<OrderDTO> getOrderListByAccountId(long accountId);

    @SelectProvider(type = OrderSQLProvider.class, method = "criteriaSelect")
    List<OrderDTO> getOrderListByCriteria(OrderCriteria orderCriteria);

    @SelectProvider(type = OrderSQLProvider.class, method = "criteriaCountSelect")
    int getOrderNumByCriteria(OrderCriteria orderCriteria);

}
