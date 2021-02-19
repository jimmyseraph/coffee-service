set names utf8mb4;
drop table if exists coffeedb.t_order;

create table coffeedb.t_order(
    orderId bigint auto_increment primary key comment '订单ID',
    orderNbr char(21) not null unique comment '订单号',
    orderStatus smallint comment '0-未付款，1-已付款，2-已发货，3-已签收完成',
    buyerId bigint comment '购买者ID',
    address varchar(50) comment '寄送地址',
    createTime datetime comment '订单创建时间',
    updateTime datetime comment '订单更新时间'
) CHARACTER SET 'utf8mb4' comment '订单信息表';
