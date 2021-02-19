set names utf8mb4;
drop table if exists coffeedb.t_coffee;

create table coffeedb.t_coffee(
    coffeeId bigint auto_increment primary key comment '咖啡商品ID',
    coffeeName varchar(20) not null comment '咖啡名称',
    price decimal(5,2) comment '咖啡价格'
) CHARACTER SET 'utf8mb4' comment '咖啡商品表';

insert into coffeedb.t_coffee values
    (null, '拿铁', 29.00),
    (null, '香草拿铁', 32.00),
    (null, '焦糖拿铁', 32.00),
    (null, '卡布奇诺', 33.00),
    (null, '福瑞白', 34.00),
    (null, '美式咖啡', 20.00),
    (null, '意式浓缩', 21.00);
