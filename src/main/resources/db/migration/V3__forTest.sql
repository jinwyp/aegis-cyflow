INSERT INTO `cyflow`.`asset` (`asset_id`, `file_type`, `busi_type`, `username`, `gid`, `description`, `url`, `origin_name`) VALUES ('1', '1', '1', '123', '123', '123', '123', '22');
INSERT INTO `cyflow`.`asset` (`asset_id`, `file_type`, `busi_type`, `username`, `gid`, `description`, `url`, `origin_name`) VALUES ('2', '1', '1', '123', '123', '123', '123', '22');
INSERT INTO `cyflow`.`asset` (`asset_id`, `file_type`, `busi_type`, `username`, `gid`, `description`, `url`, `origin_name`) VALUES ('3', '1', '1', '123', '123', '123', '123', '22');
INSERT INTO `cyflow`.`asset` (`asset_id`, `file_type`, `busi_type`, `username`, `gid`, `description`, `url`, `origin_name`) VALUES ('4', '1', '1', '123', '123', '123', '123', '22');
INSERT INTO `cyflow`.`asset` (`asset_id`, `file_type`, `busi_type`, `username`, `gid`, `description`, `url`, `origin_name`) VALUES ('5', '1', '1', '123', '123', '123', '123', '22');
INSERT INTO `cyflow`.`asset` (`asset_id`, `file_type`, `busi_type`, `username`, `gid`, `description`, `url`, `origin_name`) VALUES ('6', '1', '1', '123', '123', '123', '123', '22');
INSERT INTO `cyflow`.`asset` (`asset_id`, `file_type`, `busi_type`, `username`, `gid`, `description`, `url`, `origin_name`) VALUES ('7', '1', '1', '123', '123', '123', '123', '22');
INSERT INTO `cyflow`.`asset` (`asset_id`, `file_type`, `busi_type`, `username`, `gid`, `description`, `url`, `origin_name`) VALUES ('8', '1', '1', '123', '123', '123', '123', '22');
INSERT INTO `cyflow`.`asset` (`asset_id`, `file_type`, `busi_type`, `username`, `gid`, `description`, `url`, `origin_name`) VALUES ('9', '1', '1', '123', '123', '123', '123', '22');
INSERT INTO `cyflow`.`asset` (`asset_id`, `file_type`, `busi_type`, `username`, `gid`, `description`, `url`, `origin_name`) VALUES ('10', '1', '1', '123', '123', '123', '123', '22');


--保证金
INSERT INTO `deposit` (`id`, `flowId`, `expectedAmount`, `actuallyAmount`, `state`, `memo`, `operator`, `ts_c`, `ts_u`)
VALUES
	(1, '123456', 50.00, 100.00, 'notified', 'hehe, this is just a test', 'haha', '2017-01-11 09:37:06', '2017-01-10 19:38:46');
