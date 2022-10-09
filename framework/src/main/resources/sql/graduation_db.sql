DROP database `graduation_db`;
CREATE database `graduation_db`;
USE graduation_db;

-- COMMENT ''

create table `user_tab`
(
    id          int          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    role        int          NOT NULL COMMENT '用户权限',
    open_id     varchar(255) NOT NULL COMMENT '微信OpenID',
    name        varchar(255) NOT NULL COMMENT '学生姓名',
    nick_name   varchar(255) NOT NULL COMMENT '用于在交流的昵称这里可以使用随机生成器',
    school_id   varchar(10)  NOT NULL COMMENT '学号',
    avatar      varchar(255)          default '' COMMENT '头像',
    class_name  varchar(255) NOT NULL DEFAULT '' COMMENT '班级',
    create_time timestamp    NOT NULL,
    update_time timestamp    NOT NULL
);


-- 权限角色表 user admin superAdmin
create table `role_tab`
(
    id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name varchar(255) NOT NULL  COMMENT '用户权限',
    remake varchar(255)  COMMENT '备注',
    create_time timestamp NOT NULL,
    update_time timestamp NOT NULL
);

-- 失物招领的类型表 校园卡、雨伞、耳机、手表、背包、其他物品
create table `lost_type_tab`
(
    id          int          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name        varchar(255) NOT NULL,
    remark      varchar(255) NOT NULL COMMENT '备注',
    create_time timestamp    NOT NULL,
    update_time timestamp    NOT NULL
);
INSERT INTO `lost_type_tab`(id, name, remark, create_time, update_time) VALUES (1,'其他物品','',NOW(),NOW());
INSERT INTO `lost_type_tab`(id, name, remark, create_time, update_time) VALUES (2,'校园卡','',NOW(),NOW());
INSERT INTO `lost_type_tab`(id, name, remark, create_time, update_time) VALUES (3,'雨伞','',NOW(),NOW());
INSERT INTO `lost_type_tab`(id, name, remark, create_time, update_time) VALUES (4,'耳机','',NOW(),NOW());
INSERT INTO `lost_type_tab`(id, name, remark, create_time, update_time) VALUES (5,'手表','',NOW(),NOW());
INSERT INTO `lost_type_tab`(id, name, remark, create_time, update_time) VALUES (6,'背包','',NOW(),NOW());

-- 失物招领的信息表
create table `lost_info_tab`
(
    id            int       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    title         varchar(255) COMMENT '标题',
    post_user     int COMMENT 'user_tab.id 发布者id',
    found_status  int       NOT NULL DEFAULT 0 COMMENT '找寻状态 0未被领取 1被领取',
    lost_type     int       NOT NULL COMMENT '失物招领类型对应lost_type_tab.id',
    school_id     varchar(10) COMMENT '学号非校园卡非必要的字段可以为空',
    lost_name     varchar(255) COMMENT '校园卡失主的名字',
    description   varchar(255) COMMENT '简述失物信息',
    image         varchar(255) COMMENT '失物的图片(校园卡不会上传此内容)',
    location      varchar(255) COMMENT '领取地址',
    delete_status int       NOT NULL DEFAULT 0,
    create_time   timestamp NOT NULL,
    update_time   timestamp NOT NULL
);

create table `lost_review_tab`
(
    id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    info_id int NOT NULL COMMENT 'lost_info_tab.id 失物招领信息id',
    post_user int COMMENT 'user_tab.id 发布者id',
    reply_user int COMMENT 'user_tab.id 回复者id',
    content varchar(255) COMMENT '评论内容',
    delete_status int NOT NULL DEFAULT 0,
    create_time timestamp NOT NULL,
    update_time timestamp NOT NULL
);

create table `lost_location_tab`
(
    id          int       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name        varchar(255) COMMENT '失物地址',
    description varchar(255) COMMENT '地址描述',
    create_time timestamp NOT NULL,
    update_time timestamp NOT NULL
);

INSERT INTO `lost_location_tab`(id, name, description, create_time, update_time)
VALUES (1, '自定义地址', '', NOW(), NOW());
INSERT INTO `lost_location_tab`(id, name, description, create_time, update_time)
VALUES (2, '东1饭堂', '', NOW(), NOW());
INSERT INTO `lost_location_tab`(id, name, description, create_time, update_time)
VALUES (3, '东2饭堂', '', NOW(), NOW());
INSERT INTO `lost_location_tab`(id, name, description, create_time, update_time)
VALUES (4, '西3饭堂', '', NOW(), NOW());
INSERT INTO `lost_location_tab`(id, name, description, create_time, update_time)
VALUES (5, '西4饭堂', '', NOW(), NOW());

create table `lost_mq_tab`
(
    id          int       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    lost_id     int       NOT NULL UNIQUE,
    mq_status   int       NOT NULL DEFAULT 0,
    create_time timestamp NOT NULL,
    update_time timestamp NOT NULL
);