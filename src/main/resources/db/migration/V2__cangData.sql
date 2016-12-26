INSERT INTO `cyflow`.`party_class` (`class_name`, `description`) VALUES ('rz', '融资方');
INSERT INTO `cyflow`.`party_class` (`class_name`, `description`) VALUES ('gk', '港口方');
INSERT INTO `cyflow`.`party_class` (`class_name`, `description`) VALUES ('jg', '监管方');
INSERT INTO `cyflow`.`party_class` (`class_name`, `description`) VALUES ('my', '贸易商');
INSERT INTO `cyflow`.`party_class` (`class_name`, `description`) VALUES ('zj', '资金方');
INSERT INTO `cyflow`.`party_class` (`class_name`, `description`) VALUES ('admin', '管理员组');

--贸易方公司（现在至于易煤网）
INSERT INTO `cyflow`.`party_instance` (`party_class`, `instance_id`, `party_name`) VALUES ('my', '1', '易煤网');

INSERT INTO `cyflow`.`party_instance` (`party_class`, `instance_id`, `party_name`) VALUES ('admin', '1', '管理员');

INSERT INTO `cyflow`.`party_user` (`party_id`,`user_id`, `password`, `phone`, `email`, `name`) VALUES ('1','7777', '123456', '13800000001', '12345@12345.com', 'yimei180');

INSERT INTO `cyflow`.`party_user` (`party_id`,`user_id`, `password`, `phone`, `email`, `name`) VALUES ('1','8888', '123456', '13800000001', '12345@12345.com', 'ymFinance');

INSERT INTO `cyflow`.`party_user` (`party_id`,`user_id`, `password`, `phone`, `email`, `name`) VALUES ('2','0000', '123456', '13800000001', '12345@12345.com', 'admin');

