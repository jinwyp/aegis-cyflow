INSERT INTO `cyflow`.`party_class` (`class_name`, `description`) VALUES ('financer', '融资方');
INSERT INTO `cyflow`.`party_class` (`class_name`, `description`) VALUES ('harbor', '港口方');
INSERT INTO `cyflow`.`party_class` (`class_name`, `description`) VALUES ('supervisor', '监管方');
INSERT INTO `cyflow`.`party_class` (`class_name`, `description`) VALUES ('trader', '贸易商');
INSERT INTO `cyflow`.`party_class` (`class_name`, `description`) VALUES ('fundProvider', '资金方');
INSERT INTO `cyflow`.`party_class` (`class_name`, `description`) VALUES ('systemAdmin', '管理员组');

-- --贸易方公司（现在至于易煤网）
-- INSERT INTO `cyflow`.`party_instance` (`party_class`, `instance_id`, `party_name`) VALUES ('trader', '88888888', '孟州市至信有限责任公司');
--
INSERT INTO `cyflow`.`party_instance` (`party_class`, `instance_id`, `party_name`) VALUES ('systemAdmin', '00000000', '管理员');
--
--
--添加组（贸易商业务，贸易商财务，资金方业务，资金方财务）
INSERT INTO `cyflow`.`party_group` (`party_class`, `gid`, `description`) VALUES ('trader', '1', '贸易方业务');
INSERT INTO `cyflow`.`party_group` (`party_class`, `gid`, `description`) VALUES ('trader', '2', '贸易方财务');
INSERT INTO `cyflow`.`party_group` (`party_class`, `gid`, `description`) VALUES ('fundProvider', '1', '资金方业务');
INSERT INTO `cyflow`.`party_group` (`party_class`, `gid`, `description`) VALUES ('fundProvider', '2', '资金方财务');
--
--
-- --添加用户
-- INSERT INTO `cyflow`.`party_user` (`party_id`,`user_id`, `password`, `phone`, `email`, `name`,`username`) VALUES ('1','77777', '123456', '13800000001', 'yimei@1.com', 'trader', 'trader1');
--
-- INSERT INTO `cyflow`.`party_user` (`party_id`,`user_id`, `password`, `phone`, `email`, `name`,`username`) VALUES ('1','88888', '123456', '13800000001', 'yimei@2.com', 'traderAccountant', 'trader2');
--
-- INSERT INTO `cyflow`.`party_user` (`party_id`,`user_id`, `password`, `phone`, `email`, `name`,`username`) VALUES ('2','00000', '123456', '13800000001', 'systemAdmin@1.com', 'systemAdmin1', 'admin1');
--
-- INSERT INTO `cyflow`.`party_user` (`party_id`,`user_id`, `password`, `phone`, `email`, `name`,`username`) VALUES ('2','00001', '123456', '13800000002', 'systemAdmin@2.com', 'systemAdmin2', 'admin2');
--
-- --添加贸易方财务，贸易商业务组和成员映射关系
-- INSERT INTO `cyflow`.`user_group` (`party_id`, `gid`, `user_id`) VALUES ('1', '1', '77777');
-- INSERT INTO `cyflow`.`user_group` (`party_id`, `gid`, `user_id`) VALUES ('1', '2', '88888');

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


-----------------------------
--资金方公司
INSERT INTO `party_instance` (`id`, `party_class`, `instance_id`, `party_name`, `disable`, `ts_c`)
VALUES
	(2, 'trader', '1868', 'B2B测试-郑州会员1', 0, '2017-01-15 19:59:11');

--资金方业务和财务
INSERT INTO `party_user` (`id`, `party_id`, `user_id`, `username`, `password`, `phone`, `email`, `name`, `disable`, `ts_c`)
VALUES
	(3, 2, '99999', 'fundProvider1', '123456', '13800000003', 'fundProvider@qq.com', 'fundProvider', 0, '2017-01-12 16:26:02'),
	(4, 2, '10101', 'fundProvider2', '123456', '13800000004', 'fundProviderAccountant@qq.com', 'fundProviderAccountant', 0, '2017-01-12 16:26:02');

--对应关系
INSERT INTO `user_group` (`id`, `party_id`, `gid`, `user_id`, `ts_c`)
VALUES
	(3, 2, '1', '99999', '2017-01-12 16:26:02'),
	(4, 2, '2', '10101', '2017-01-12 16:26:02');

