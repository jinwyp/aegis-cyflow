INSERT INTO `cyflow`.`party_class` (`class_name`, `description`) VALUES ('financer', '融资方');
INSERT INTO `cyflow`.`party_class` (`class_name`, `description`) VALUES ('harbor', '港口方');
INSERT INTO `cyflow`.`party_class` (`class_name`, `description`) VALUES ('supervisor', '监管方');
INSERT INTO `cyflow`.`party_class` (`class_name`, `description`) VALUES ('trader', '贸易商');
INSERT INTO `cyflow`.`party_class` (`class_name`, `description`) VALUES ('fundProvider', '资金方');
INSERT INTO `cyflow`.`party_class` (`class_name`, `description`) VALUES ('systemAdmin', '管理员组');

--贸易方公司（现在至于易煤网）
INSERT INTO `cyflow`.`party_instance` (`party_class`, `instance_id`, `party_name`) VALUES ('trader', '88888888', '易煤网');

INSERT INTO `cyflow`.`party_instance` (`party_class`, `instance_id`, `party_name`) VALUES ('systemAdmin', '00000000', '管理员');

--添加组（贸易商业务，贸易商财务，资金方业务，资金方财务）
INSERT INTO `cyflow`.`party_group` (`party_class`, `gid`, `description`) VALUES ('trader', '1', '贸易方业务');
INSERT INTO `cyflow`.`party_group` (`party_class`, `gid`, `description`) VALUES ('trader', '2', '贸易方财务');
INSERT INTO `cyflow`.`party_group` (`party_class`, `gid`, `description`) VALUES ('fundProvider', '1', '资金方业务');
INSERT INTO `cyflow`.`party_group` (`party_class`, `gid`, `description`) VALUES ('fundProvider', '2', '资金方财务');


--添加用户
INSERT INTO `cyflow`.`party_user` (`party_id`,`user_id`, `password`, `phone`, `email`, `name`,`username`) VALUES ('1','77777', '123456', '13800000001', '12345@12345.com', 'yimei180', 'u1');

INSERT INTO `cyflow`.`party_user` (`party_id`,`user_id`, `password`, `phone`, `email`, `name`,`username`) VALUES ('1','88888', '123456', '13800000001', '12345@12345.com', 'ymFinance', 'u2');

INSERT INTO `cyflow`.`party_user` (`party_id`,`user_id`, `password`, `phone`, `email`, `name`,`username`) VALUES ('2','00000', '123456', '13800000001', '12345@12345.com', 'admin', 'u3');

--添加贸易方财务，贸易商业务组和成员映射关系
INSERT INTO `cyflow`.`user_group` (`party_id`, `gid`, `user_id`) VALUES ('1', '1', '77777');
INSERT INTO `cyflow`.`user_group` (`party_id`, `gid`, `user_id`) VALUES ('1', '2', '88888');
