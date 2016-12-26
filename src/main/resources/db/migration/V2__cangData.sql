INSERT INTO `cyflow`.`party_class` (`class_name`, `description`) VALUES ('rzf', '融资方');
INSERT INTO `cyflow`.`party_class` (`class_name`, `description`) VALUES ('gkf', '港口方');
INSERT INTO `cyflow`.`party_class` (`class_name`, `description`) VALUES ('jgf', '监管方');
INSERT INTO `cyflow`.`party_class` (`class_name`, `description`) VALUES ('myf', '贸易商');
INSERT INTO `cyflow`.`party_class` (`class_name`, `description`) VALUES ('zjf', '资金方');
INSERT INTO `cyflow`.`party_class` (`class_name`, `description`) VALUES ('adm', '管理员组');

--贸易方公司（现在至于易煤网）
INSERT INTO `cyflow`.`party_instance` (`party_class`, `instance_id`, `party_name`) VALUES ('myf', '88888888', '易煤网');

INSERT INTO `cyflow`.`party_instance` (`party_class`, `instance_id`, `party_name`) VALUES ('adm', '00000000', '管理员');

INSERT INTO `cyflow`.`party_user` (`party_id`,`user_id`, `password`, `phone`, `email`, `name`) VALUES ('1','77777', '123456', '13800000001', '12345@12345.com', 'yimei180');

INSERT INTO `cyflow`.`party_user` (`party_id`,`user_id`, `password`, `phone`, `email`, `name`) VALUES ('1','88888', '123456', '13800000001', '12345@12345.com', 'ymFinance');

INSERT INTO `cyflow`.`party_user` (`party_id`,`user_id`, `password`, `phone`, `email`, `name`) VALUES ('2','00000', '123456', '13800000001', '12345@12345.com', 'admin');

