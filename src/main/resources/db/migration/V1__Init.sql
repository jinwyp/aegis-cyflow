-- 流程实例
create table flow_instance(
  id LONG not null AUTO_INCREMENT,
  flow_id varchar(64) not null,
  type varchar(16) not null,
  user_type char(4) not null,
  user_id VARCHAR(64) not null,
  ts_c TIMESTAMP
);

-- 流程任务
create table flow_task(
  id LONG not null AUTO_INCREMENT,
  flow_id varchar(64) not null,
  taskName varchar(64) not null,
  user_type char(4) not null,
  user_id VARCHAR(64) not null,
  ts_c TIMESTAMP
)

