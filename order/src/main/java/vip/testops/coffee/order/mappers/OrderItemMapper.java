package vip.testops.coffee.order.mappers;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import vip.testops.coffee.order.entities.dto.OrderItemDTO;

import java.util.List;

@Mapper
public interface OrderItemMapper {
    @Insert("insert into t_order_item values(null, #{coffeeId}, #{amount}, #{orderId})")
    @SelectKey(statement = "select last_insert_id()", keyProperty = "itemId", before = false, resultType = long.class)
    int addOrderItem(OrderItemDTO orderItemDTO);

    @Select("select * from t_order_item where orderId = #{orderId}")
    List<OrderItemDTO> getItemsByOrderId(long orderId);
}
