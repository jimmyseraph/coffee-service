package vip.testops.coffee.order.mappers;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import vip.testops.coffee.order.entities.dto.CoffeeDTO;

@Mapper
public interface CoffeeMapper {
    @Select("select * from t_coffee where coffeeId = #{coffeeId}")
    CoffeeDTO getCoffeeById(long coffeeId);
}
