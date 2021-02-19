package vip.testops.coffee.order;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import vip.testops.coffee.order.controllers.OrderController;
import vip.testops.coffee.order.entities.ResponseEntity;
import vip.testops.coffee.order.entities.dto.AccountDTO;
import vip.testops.coffee.order.entities.dto.CoffeeDTO;
import vip.testops.coffee.order.entities.dto.OrderDTO;
import vip.testops.coffee.order.entities.req.OrderCreateRequestEntity;
import vip.testops.coffee.order.entities.req.OrderItemRequestEntity;
import vip.testops.coffee.order.mappers.CoffeeMapper;
import vip.testops.coffee.order.mappers.OrderItemMapper;
import vip.testops.coffee.order.mappers.OrderMapper;
import vip.testops.coffee.order.message.MessageSender;
import vip.testops.coffee.order.services.OrderService;
import vip.testops.coffee.order.services.impl.OrderServiceImpl;
import vip.testops.coffee.order.utils.UniqueID;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    @InjectMocks
    private final OrderService orderService = new OrderServiceImpl();

    @InjectMocks
    private final OrderController orderController = new OrderController();

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private OrderItemMapper orderItemMapper;

    @Mock
    private CoffeeMapper coffeeMapper;

    @Mock
    private MessageSender messageSender;

    @Mock
    private HttpServletRequest httpServletRequest;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.initMocks(this);
        orderController.setOrderService(orderService);
    }

    @Test
    public void testCreateOrder(){
        /*
        param orderCreateRequestEntity prepare
         */
        OrderCreateRequestEntity orderCreateRequestEntity = new OrderCreateRequestEntity();
        orderCreateRequestEntity.setAddress("Shanghai");
        OrderItemRequestEntity[] orderItemRequestEntities = new OrderItemRequestEntity[]{
                new OrderItemRequestEntity()
        };
        orderItemRequestEntities[0].setCoffeeId(1L);
        orderItemRequestEntities[0].setAmount(2);
        orderCreateRequestEntity.setOrderItems(orderItemRequestEntities);
        /*
        param account prepare
         */
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setAccountId(1);
        accountDTO.setAccountName("Liudao");
        accountDTO.setGender(0);

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setAddress(orderCreateRequestEntity.getAddress());
        orderDTO.setOrderNbr(UniqueID.getUniqueNumber());
        orderDTO.setOrderStatus(0);
        orderDTO.setBuyerId(accountDTO.getAccountId());
        orderDTO.setCreateTime(new Date());
        orderDTO.setOrderId(1);

        /*
        mock
         */
        Mockito.when(orderMapper.addOrder(Mockito.any())).thenReturn(1);
        Mockito.when(orderItemMapper.addOrderItem(Mockito.any())).thenReturn(1);
        Mockito.when(coffeeMapper.getCoffeeById(Mockito.anyLong())).thenReturn(new CoffeeDTO());
        Mockito.when(httpServletRequest.getAttribute("accountDTO")).thenReturn(accountDTO);

        /*
        do the test
         */
        ResponseEntity<?> responseEntity = orderController.createOrder(orderCreateRequestEntity, httpServletRequest);

        Assertions.assertEquals(1000, responseEntity.getCode());
    }
}
