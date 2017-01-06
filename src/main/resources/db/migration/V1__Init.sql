-- 流程实例
create table flow_instance(
  id BIGINT not null auto_increment,
  flow_id varchar(64) not null,
  flow_type varchar(16) not null,     -- cang   ying
  user_type varchar(32) not null,    -- 用户类型  相当于party_id
  user_id varchar(64) not null,  -- 用户id  ?????
  data  varchar(8192),         -- 流程上下文
  state varchar(1024),
  finished TINYINT not NULL ,    -- 0：未完成 1：已完成
  ts_c timestamp default current_timestamp,
  PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;
-- flow_id 唯一索引
CREATE UNIQUE INDEX flowId_index ON flow_instance(flow_id);

-- 流程任务
create table flow_task(
  id BIGINT not null auto_increment,
  flow_id    varchar(64)   not null,
  task_id    varchar(128)  not null,
  task_name  varchar(64)   not null,
  task_submit varchar(1024) not null,   -- 用户提交数据
  user_type varchar(64) not null,
  user_id VARCHAR(64) not null,
  ts_c timestamp default current_timestamp,
  PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;
-- flow_id和task_id唯一索引
CREATE UNIQUE INDEX flowId_taskid_index ON flow_task(flow_id,task_id);

-- 用户流程设计
create table design(
  id BIGINT not null auto_increment,
  name varchar(64) not null,
  json text(65532),
  meta text(65532) not null,
  ts_c timestamp default current_timestamp,
  PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 流程部署
create table deploy(
  id BIGINT not null auto_increment,
  flow_type varchar(64) not null,
  jar blob(104857600) not null,   -- 100M
  enable bool not null,           -- 激活
  ts_c timestamp default current_timestamp,
  PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- 参与方类别
create table party_class (
  id BIGINT not null auto_increment,
  class_name varchar(32),       -- 参与方类别名称     todo: 应该改为三位编码
  description varchar(64) ,    -- 参与方类别描述
  PRIMARY KEY (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;
-- class_name 唯一索引
CREATE UNIQUE INDEX class_name_index ON party_class(class_name);

-- 参与方实体:
-- 如:
-- 融资方-1 融资方-2,
-- 港口方-1, 港口方-2
-- ...
create table  party_instance (
  id BIGINT not null auto_increment,
  party_class varchar(32) not null,    -- 参与方类别 -  zjf  rzf  myf,      三位编码
  instance_id varchar(32),            -- 比如融资方-1, 融资方-2     todo: 应该改为8位编码
  party_name varchar(256),            -- 参与方名称
  disable tinyint not null DEFAULT 0,    -- 是否被禁用  0: 未禁用,  1: 禁用
  ts_c timestamp default current_timestamp,
  PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;
-- party_class+instance_id 唯一索引
CREATE UNIQUE INDEX party_class_instance_id_index ON party_instance(party_class,instance_id);

-- 用户表
create table party_user(
  id BIGINT not null auto_increment,
  party_id BIGINT not null,           -- 参与方的实体id, 这个等价于  userType    rz1 rz2
  user_id varchar(10) not null,       -- 应该改为  todo 5位编码
  username VARCHAR(128) DEFAULT NULL COMMENT '登录名' ,

  password varchar(128) not null,
  phone varchar(32),
  email varchar(128),
  name varchar(128) not null,   -- todo 王琦:   这里是登录名?  需要让这个字段作唯一索引
  disable tinyint not null DEFAULT 0,    -- 是否被禁用  0: 未禁用,  1: 禁用
  ts_c timestamp default current_timestamp,
  PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;
-- party_id+user_id 唯一索引
CREATE UNIQUE INDEX party_id_user_id_index ON party_user(party_id,user_id);
CREATE UNIQUE INDEX part_user_name_index ON party_user(username);

-- 用户群组表
create table user_group(
  id BIGINT not null auto_increment,
  party_id BIGINT not null,
  gid varchar(32) not null,            -- 参与方组id
  user_id varchar(10) not null,
  ts_c timestamp default current_timestamp,
  PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;
-- party_id,gid,user_id
CREATE UNIQUE INDEX user_group_unq_index ON user_group(party_id,gid,user_id);



-- 每一类运营方的组是预先定义好的 字典表
--
create table party_group(
  id BIGINT not null auto_increment,
  party_class varchar(32) not null,    -- 参与方类别
  gid varchar(32) not null,            -- 参与方组id
  description varchar(256) not null,  -- 运营组描述
  ts_c timestamp default current_timestamp,
  PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 文件资源管理
create table asset(
  id BIGINT not null auto_increment,       -- 非业务主键
  asset_id varchar(36) not null,           -- 资源id
  file_type tinyint not null DEFAULT 0,    -- 文件类型  0: 未知,  1: pdf, 2: image
  busi_type varchar(30) not null DEFAULT 0,-- 业务类别
  username varchar(128) not null,          -- 上传用户
  gid varchar(32),                         -- 上传用户当时属于哪个组
  description varchar(512),                -- 可以为空
  url varchar(256) not null,               -- 文件位置信息, 可能为aliyun, filesystem  etc
  origin_name varchar(256) not null,       -- 文件原始名字
  ts_c timestamp default current_timestamp,
  PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE UNIQUE INDEX asset_index ON asset(asset_id);

