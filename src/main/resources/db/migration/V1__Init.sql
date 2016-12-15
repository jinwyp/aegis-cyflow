-- 流程实例
create table flow_instance(
  id long not null auto_increment,

  flow_id varchar(64) not null,
  type varchar(16) not null,
  user_type char(4) not null,    -- 用户类型  相当于party_id
  user_id VARCHAR(64) not null,  --

  ts_c timestamp default current_timestamp
);

-- 流程任务
create table flow_task(
  id LONG not null auto_increment,
  flow_id varchar(64) not null,
  taskName varchar(64) not null,
  user_type char(4) not null,
  user_id VARCHAR(64) not null,

  ts_c timestamp default current_timestamp
);

-- 参与方列表字典表
create table party_dict(
  id long not null auto_increment,

  party_class varchar(2) not null,    -- 参与方类别
  description VARCHAR(256) not null, -- 参与方类别描述

  ts_c timestamp default current_timestamp
);

-- 参与方实体:
-- 如:
-- 融资方-1 融资方-2,
-- 港口方-1, 港口方-2
-- ...
create table  party_instance (
  id long not null auto_increment,
  party_class varchar(2) not null,    -- 参与方类别
  instance_id int,                    -- 比如融资方-1, 融资方-2
  name varchar(256),                  -- 参与方名称
  ts_c timestamp default current_timestamp
);

-- 用户表
create table party_user(
  id long not null auto_increment,

  party_id long not null,           -- 参与方的实体id, 这个等价于  userType
  user_id varchar(10) not null,     --

  gid varchar(2) not null,          -- 用户所属的组
  ts_c timestamp default current_timestamp
);

-- 每一类运营方的组是预先定义好的 字典表
--
create table user_group(

  id long not null auto_increment,

  party_class varchar(2) not null,    -- 参与方类别
  gid varchar(2) not null,            -- 参与方组id
  description varchar(256) not null,  -- 运营组描述

  ts_c timestamp default current_timestamp
);

insert into user_group(party_class, gid, description) values();


