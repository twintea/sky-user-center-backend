-- auto-generated definition
create table user
(
    id            bigint auto_increment comment '主键'
        primary key,
    user_name     varchar(256)                           null comment '用户名',
    user_account  varchar(256)                           null comment '用户账号',
    avatar_url    varchar(1024)                          null comment '用户头像地址',
    gender        tinyint                                null comment '性别',
    user_password varchar(512)                           null comment '用户密码',
    email         varchar(512)                           null comment '邮箱',
    user_status   int          default 0                 null comment '用户状态 0-正常',
    phone         varchar(128)                           null comment '用户手机号码',
    create_time   datetime     default CURRENT_TIMESTAMP not null comment '用户创建时间',
    update_time   datetime     default CURRENT_TIMESTAMP not null comment '用户更新时间',
    is_delete     tinyint      default 0                 not null comment '是否被删除 0-正常',
    user_role     int          default 0                 not null comment '用户角色 0-默认 1-管理员',
    register_code varchar(256) default '0'               not null comment '注册码'
)
    comment '用户';
