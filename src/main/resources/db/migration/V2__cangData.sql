INSERT INTO `cyflow`.`party_class` (`class_name`, `description`) VALUES ('financer', '融资方');
INSERT INTO `cyflow`.`party_class` (`class_name`, `description`) VALUES ('harbor', '港口方');
INSERT INTO `cyflow`.`party_class` (`class_name`, `description`) VALUES ('supervisor', '监管方');
INSERT INTO `cyflow`.`party_class` (`class_name`, `description`) VALUES ('trader', '贸易商');
INSERT INTO `cyflow`.`party_class` (`class_name`, `description`) VALUES ('fundProvider', '资金方');
INSERT INTO `cyflow`.`party_class` (`class_name`, `description`) VALUES ('systemAdmin', '管理员组');

--添加组（贸易商业务，贸易商财务，资金方业务，资金方财务）
INSERT INTO `cyflow`.`party_group` (`party_class`, `gid`, `description`) VALUES ('trader', '1', '贸易方业务');
INSERT INTO `cyflow`.`party_group` (`party_class`, `gid`, `description`) VALUES ('trader', '2', '贸易方财务');
INSERT INTO `cyflow`.`party_group` (`party_class`, `gid`, `description`) VALUES ('fundProvider', '1', '资金方业务');
INSERT INTO `cyflow`.`party_group` (`party_class`, `gid`, `description`) VALUES ('fundProvider', '2', '资金方财务');

--贸易商公司
INSERT INTO `party_instance` (`id`, `party_class`, `instance_id`, `party_name`, `disable`, `ts_c`)
VALUES
	(1, 'trader', '1822', '上海瑞易供应链管理有限公司', 0, '2017-01-15 19:59:11');

--贸易商业务和财务
INSERT INTO `party_user` (`id`, `party_id`, `user_id`, `username`, `password`, `phone`, `email`, `name`, `disable`, `ts_c`)
VALUES
	(1, 1, '77777', 'trader1', '123456', '13800000001', 'tader@yimei180.com', 'trader', 0, '2017-01-12 16:26:02'),
	(2, 1, '88888', 'trader2', '123456', '13800000002', 'traderAccountant@yimei180.com', 'traderAccountant', 0, '2017-01-12 16:26:02');

--对应关系
INSERT INTO `user_group` (`id`, `party_id`, `gid`, `user_id`, `ts_c`)
VALUES
	(1, 1, '1', '77777', '2017-01-12 16:26:02'),
	(2, 1, '2', '88888', '2017-01-12 16:26:02');


--管理员
INSERT INTO `cyflow`.`party_instance` (`id`, `party_class`, `instance_id`, `party_name`) VALUES (2, 'systemAdmin', '00000000', '管理员');
INSERT INTO `cyflow`.`party_user` (`party_id`,`user_id`, `password`, `phone`, `email`, `name`,`username`) VALUES ('2','00000', '123456', '13800000001', 'systemAdmin@1.com', 'systemAdmin1', 'admin1');
