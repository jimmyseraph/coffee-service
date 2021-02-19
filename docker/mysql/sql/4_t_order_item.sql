set names utf8mb4;
drop table if exists coffeedb.t_order_item;

create table coffeedb.t_order_item(
    itemId bigint auto_increment primary key comment '订单项ID，系统自增主键',
    coffeeId bigint not null comment '咖啡商品ID',
    amount int comment '商品数量',
    orderId bigint comment '订单ID',
    foreign key (coffeeId) references t_coffee(coffeeId) on delete cascade,
    foreign key (orderId) references t_order(orderId) on delete cascade
) CHARACTER SET 'utf8mb4' comment '订单商品表';
