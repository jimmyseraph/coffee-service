set names utf8mb4;
drop table if exists coffeedb.t_account;
create table coffeedb.t_account(
    `accountId` bigint auto_increment primary key comment '账号系统id',
    `accountName` varchar(20) not null unique comment '账户名',
    `salt` char(6) not null comment '密码加密用词缀',
    `password` char(64) not null comment 'sha256摘要后的密码',
    `cellphone` varchar(20) comment '手机号',
    `gender` smallint comment '性别',
    `createTime` datetime comment '账号创建时间',
    `lastLoginTime` datetime comment '最后登录时间'
) CHARACTER SET 'utf8mb4' comment '账户信息表';
