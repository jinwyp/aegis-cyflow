INSERT INTO `cyflow`.`asset` (`asset_id`, `file_type`, `busi_type`, `username`, `gid`, `description`, `url`, `origin_name`) VALUES ('1', '1', '1', '123', 'financer', '123', '123', '22');
INSERT INTO `cyflow`.`asset` (`asset_id`, `file_type`, `busi_type`, `username`, `gid`, `description`, `url`, `origin_name`) VALUES ('2', '1', '1', '123', 'financer', '123', '123', '22');
INSERT INTO `cyflow`.`asset` (`asset_id`, `file_type`, `busi_type`, `username`, `gid`, `description`, `url`, `origin_name`) VALUES ('3', '1', '1', '123', 'financer', '123', '123', '22');
INSERT INTO `cyflow`.`asset` (`asset_id`, `file_type`, `busi_type`, `username`, `gid`, `description`, `url`, `origin_name`) VALUES ('4', '1', '1', '123', 'financer', '123', '123', '22');
INSERT INTO `cyflow`.`asset` (`asset_id`, `file_type`, `busi_type`, `username`, `gid`, `description`, `url`, `origin_name`) VALUES ('5', '1', '1', '123', 'financer', '123', '123', '22');
INSERT INTO `cyflow`.`asset` (`asset_id`, `file_type`, `busi_type`, `username`, `gid`, `description`, `url`, `origin_name`) VALUES ('6', '1', '1', '123', 'financer', '123', '123', '22');
INSERT INTO `cyflow`.`asset` (`asset_id`, `file_type`, `busi_type`, `username`, `gid`, `description`, `url`, `origin_name`) VALUES ('7', '1', '1', '123', 'financer', '123', '123', '22');
INSERT INTO `cyflow`.`asset` (`asset_id`, `file_type`, `busi_type`, `username`, `gid`, `description`, `url`, `origin_name`) VALUES ('8', '1', '1', '123', 'financer', '123', '123', '22');
INSERT INTO `cyflow`.`asset` (`asset_id`, `file_type`, `busi_type`, `username`, `gid`, `description`, `url`, `origin_name`) VALUES ('9', '1', '1', '123', 'financer', '123', '123', '22');
INSERT INTO `cyflow`.`asset` (`asset_id`, `file_type`, `busi_type`, `username`, `gid`, `description`, `url`, `origin_name`) VALUES ('10', '1', '1', '123', 'financer', '123', '123', '22');



--公司
INSERT INTO `party_instance` (`id`, `party_class`, `instance_id`, `party_name`, `disable`, `ts_c`)
VALUES
	(3, 'financer', '77777777', '环球贸易有限公司', 0, '2017-01-11 11:46:08'),
	(4, 'financer', '15011102', '融资方B公司', 0, '2017-01-11 11:46:08'),
	(5, 'financer', '15011103', '融资方C公司', 0, '2017-01-11 11:46:08'),
	(6, 'harbor', '15011104', '上海港', 0, '2017-01-11 11:46:08'),
	(7, 'harbor', '15011105', '武汉港', 0, '2017-01-11 11:46:08'),
	(8, 'harbor', '15011106', '南京港', 0, '2017-01-11 11:46:08'),
	(9, 'supervisor', '15011107', '监管方A公司', 0, '2017-01-11 11:46:08'),
	(10, 'supervisor', '15011108', '监管方B公司', 0, '2017-01-11 11:46:08'),
	(11, 'supervisor', '15011109', '监管方C公司', 0, '2017-01-11 11:46:08'),
	(12, 'fundProvider', '66666666', '宇宙商贸有限公司', 0, '2017-01-11 11:46:08'),
	(13, 'fundProvider', '15011111', '资金方B公司', 0, '2017-01-11 11:46:08'),
	(14, 'fundProvider', '15011112', '资金方C公司', 0, '2017-01-11 11:46:08');

--用户
INSERT INTO `party_user` (`id`, `party_id`, `user_id`, `username`, `password`, `phone`, `email`, `name`, `disable`, `ts_c`)
VALUES
	(6, 3, '66661', 'rza1', '123456', '13800000001', 'financer@a1.com', 'financera1', 0, '2017-01-11 11:46:08'),
	(7, 3, '66662', 'rza2', '123456', '13800000002', 'financer@a2.com', 'financera2', 0, '2017-01-11 11:46:08'),
	(8, 4, '66663', 'rzb1', '123456', '13800000003', 'financer@b1.com', 'financerb1', 0, '2017-01-11 11:46:08'),
	(9, 4, '66664', 'rzb2', '123456', '13800000004', 'financer@b2.com', 'financerb2', 0, '2017-01-11 11:46:08'),
	(10, 5, '66665', 'rzc1', '123456', '13800000005', 'financer@c1.com', 'financerc1', 0, '2017-01-11 11:46:08'),
	(11, 5, '66666', 'rzc2', '123456', '13800000006', 'financer@c2.com', 'financerc2', 0, '2017-01-11 11:46:08'),
	(12, 6, '55551', 'sh1', '123456', '13800000007', 'harbor_shanghai@1.com', 'harborsh1', 0, '2017-01-11 11:46:08'),
	(13, 6, '55552', 'sh2', '123456', '13800000008', 'harbor_shanghai@2.com', 'harborsh2', 0, '2017-01-11 11:46:08'),
	(14, 7, '55553', 'wh1', '123456', '13800000009', 'harbor_wuhan@1.com', 'harborwh1', 0, '2017-01-11 11:46:08'),
	(15, 7, '55554', 'wh2', '123456', '13800000010', 'harbor_wuhan@2.com', 'harborwh2', 0, '2017-01-11 11:46:08'),
	(16, 8, '55555', 'nj1', '123456', '13800000011', 'harbor_nanjing@1.com', 'harbornj1', 0, '2017-01-11 11:46:08'),
	(17, 8, '55556', 'nj2', '123456', '13800000012', 'harbor_nanjing@2.com', 'harbornj2', 0, '2017-01-11 11:46:08'),
	(18, 9, '22221', 'jga1', '123456', '13800000013', 'supervisor@a1.com', 'supervisora1', 0, '2017-01-11 11:46:08'),
	(19, 9, '22222', 'jga2', '123456', '13800000014', 'supervisor@a2.com', 'supervisora2', 0, '2017-01-11 11:46:08'),
	(20, 10, '22223', 'jgb1', '123456', '13800000015', 'supervisor@b1.com', 'supervisorb1', 0, '2017-01-11 11:46:08'),
	(21, 10, '22224', 'jgb2', '123456', '13800000016', 'supervisor@b2.com', 'supervisorb2', 0, '2017-01-11 11:46:08'),
	(22, 11, '22225', 'jgc1', '123456', '13800000017', 'supervisor@c1.com', 'supervisorc1', 0, '2017-01-11 11:46:08'),
	(23, 11, '22226', 'jgc2', '123456', '13800000018', 'supervisor@c2.com', 'supervisorc2', 0, '2017-01-11 11:46:08'),
	(24, 12, '33331', 'zja1', '123456', '13800000019', 'fundProvider@a1.com', 'fundProvider', 0, '2017-01-11 11:46:08'),
	(25, 12, '33332', 'zja2', '123456', '13800000020', 'fundProvider@a2.com', 'fundProviderAccountant', 0, '2017-01-11 11:46:08'),
	(26, 13, '33333', 'zjb1', '123456', '13800000021', 'fundProvider@b1.com', 'fundProvider', 0, '2017-01-11 11:46:08'),
	(27, 13, '33334', 'zjb2', '123456', '13800000022', 'fundProvider@b2.com', 'fundProviderAccountant', 0, '2017-01-11 11:46:08'),
	(28, 14, '33335', 'zjc1', '123456', '13800000023', 'fundProvider@c1.com', 'fundProvider', 0, '2017-01-11 11:46:08'),
	(29, 14, '33336', 'zjc2', '123456', '13800000024', 'fundProvider@c2.com', 'fundProviderAccountant', 0, '2017-01-11 11:46:08');

--组关系
INSERT INTO `user_group` (`id`, `party_id`, `gid`, `user_id`, `ts_c`)
VALUES
	(3, 12, '1', '33331', '2017-01-11 18:12:30'),
	(4, 12, '2', '33332', '2017-01-11 18:12:30'),
	(5, 13, '1', '33333', '2017-01-11 18:12:30'),
	(6, 13, '2', '33334', '2017-01-11 18:12:30'),
	(7, 14, '1', '33335', '2017-01-11 18:12:30'),
	(8, 14, '2', '33336', '2017-01-11 18:12:30');


-- 	#用户信息
-- INSERT INTO `kitt`.`users` (`id`, `securephone`, `password`, `registertime`, `isactive`, `verifystatus`, `verifytime`, `clienttype`, `email`, `traderid`) VALUES
-- ('10000', '15000000001', 'e10adc3949ba59abbe56e057f20f883e', '2016-07-01 14:44:45', '1', '审核通过', '2016-07-01 15:27:25', '1', 'liyuan@yimei180.com', '93');
--
-- #用户信息
-- INSERT INTO `kitt`.`users` (`id`, `securephone`, `password`, `registertime`, `isactive`, `verifystatus`, `verifytime`, `clienttype`, `email`, `traderid`) VALUES
-- ('10001', '15000000002', 'e10adc3949ba59abbe56e057f20f883e', '2016-07-01 14:44:45', '1', '审核通过', '2016-07-01 15:27:25', '1', 'liyuan@yimei180.com', '93');
--
-- #用户信息
-- INSERT INTO `kitt`.`users` (`id`, `securephone`, `password`, `registertime`, `isactive`, `verifystatus`, `verifytime`, `clienttype`, `email`, `traderid`) VALUES
-- ('10002', '15000000003', 'e10adc3949ba59abbe56e057f20f883e', '2016-07-01 14:44:45', '1', '审核通过', '2016-07-01 15:27:25', '1', 'liyuan@yimei180.com', '93');
--
--
-- ##公司信息
-- INSERT INTO `kitt`.`companies` (`id`, `name`, `phone`, `fax`, `legalperson`, `businesslicense`, `identificationnumber`, `organizationcode`, `operatinglicense`, `userid`, `verifystatus`, `remarks`, `account`, `legalpersonname`, `traderphone`, `openinglicense`, `logopic`, `bannerpic`, `istender`, `province`, `city`, `country`, `detailaddress`, `provinceCode`, `cityCode`, `countryCode`, `childBankCode`, `legalpersonVest`, `certificateType`, `IPO`, `socialCreditCode`, `certificateUnite`, `certificateValidType`, `registeredCapital`, `manageProvince`, `manageCity`, `manageCountry`, `manageProvinceCode`, `manageCityCode`, `manageCountryCode`, `manageDetailAddress`, `enterpriseType`, `enterpriseProperty`, `companyType`, `companyProperty`, `certificateUnitePic`, `rcType`, `licenseCode`, `creditCodePic`) VALUES
-- ('66666666', '宇宙商贸有限公司', '021-11111111', '021-22222234', '', '/files/upload/1963365c-959c-435b-9d54-a39087f9b5b2.jpg', '', '', '', '10000', '审核通过', '', '1234567890', '媛', '', '/files/upload/6deb8512-8589-4f17-b45c-866117a8bf05.jpg', '', '', '0', '重庆市', '重庆市', '大渡口区', '啊啊啊啊啊啊', '42', '42100', '42100104', '102221000366', '1', '1', '2', '和答复客户可水电费可', '1', '1', '123454321', '江西省', '抚州市', '临川区', '26', '26100', '26100104', '的法规和大发光火', '2', '2', '煤炭贸易类企业', '内资企业', '', '1', '', '/files/upload/f27ac3c6-6420-4f83-80f8-6c0b8ada62fa.jpeg');
--
-- INSERT INTO `kitt`.`companies` (`id`, `name`, `phone`, `fax`, `legalperson`, `businesslicense`, `identificationnumber`, `organizationcode`, `operatinglicense`, `userid`, `verifystatus`, `remarks`, `account`, `legalpersonname`, `traderphone`, `openinglicense`, `logopic`, `bannerpic`, `istender`, `province`, `city`, `country`, `detailaddress`, `provinceCode`, `cityCode`, `countryCode`, `childBankCode`, `legalpersonVest`, `certificateType`, `IPO`, `socialCreditCode`, `certificateUnite`, `certificateValidType`, `registeredCapital`, `manageProvince`, `manageCity`, `manageCountry`, `manageProvinceCode`, `manageCityCode`, `manageCountryCode`, `manageDetailAddress`, `enterpriseType`, `enterpriseProperty`, `companyType`, `companyProperty`, `certificateUnitePic`, `rcType`, `licenseCode`, `creditCodePic`) VALUES
-- ('77777777', '环球贸易有限公司', '021-11111111', '021-22222234', '', '/files/upload/1963365c-959c-435b-9d54-a39087f9b5b2.jpg', '', '', '', '10001', '审核通过', '', '1234567890', '媛', '', '/files/upload/6deb8512-8589-4f17-b45c-866117a8bf05.jpg', '', '', '0', '重庆市', '重庆市', '大渡口区', '啊啊啊啊啊啊', '42', '42100', '42100104', '102221000366', '1', '1', '2', '和答复客户可水电费可', '1', '1', '123454321', '江西省', '抚州市', '临川区', '26', '26100', '26100104', '的法规和大发光火', '2', '2', '煤炭贸易类企业', '内资企业', '', '1', '', '/files/upload/f27ac3c6-6420-4f83-80f8-6c0b8ada62fa.jpeg');
--
-- INSERT INTO `kitt`.`companies` (`id`, `name`, `phone`, `fax`, `legalperson`, `businesslicense`, `identificationnumber`, `organizationcode`, `operatinglicense`, `userid`, `verifystatus`, `remarks`, `account`, `legalpersonname`, `traderphone`, `openinglicense`, `logopic`, `bannerpic`, `istender`, `province`, `city`, `country`, `detailaddress`, `provinceCode`, `cityCode`, `countryCode`, `childBankCode`, `legalpersonVest`, `certificateType`, `IPO`, `socialCreditCode`, `certificateUnite`, `certificateValidType`, `registeredCapital`, `manageProvince`, `manageCity`, `manageCountry`, `manageProvinceCode`, `manageCityCode`, `manageCountryCode`, `manageDetailAddress`, `enterpriseType`, `enterpriseProperty`, `companyType`, `companyProperty`, `certificateUnitePic`, `rcType`, `licenseCode`, `creditCodePic`) VALUES
-- ('88888888', '孟州市至信有限责任公司', '021-11111111', '021-22222234', '', '/files/upload/1963365c-959c-435b-9d54-a39087f9b5b2.jpg', '', '', '', '10002', '审核通过', '', '1234567890', '媛', '', '/files/upload/6deb8512-8589-4f17-b45c-866117a8bf05.jpg', '', '', '0', '重庆市', '重庆市', '大渡口区', '啊啊啊啊啊啊', '42', '42100', '42100104', '102221000366', '1', '1', '2', '和答复客户可水电费可', '1', '1', '123454321', '江西省', '抚州市', '临川区', '26', '26100', '26100104', '的法规和大发光火', '2', '2', '煤炭贸易类企业', '内资企业', '', '1', '', '/files/upload/f27ac3c6-6420-4f83-80f8-6c0b8ada62fa.jpeg');
--
--
-- INSERT INTO `kitt`.`pay_userfundaccounts` (`id`, `userId`, `account`, `password`, `status`, `payPhone`, `companyName`, `createTime`, `lastUpdateTime`, `accountBankName`, `deleted`, `accountChildBankName`,`flag`) VALUES
-- ('1000', '10000', '3110710006261382225', 'eeafb716f93fa090d7716749a6eefa72', '2', '15500000001', '宇宙商贸有限公司', '2016-07-01 15:44:17', '2016-08-29 14:35:38', '中信银行', '0', '中信银行上海自贸区分行',2);
-- INSERT INTO `kitt`.`pay_userfundaccounts` (`id`, `userId`, `account`, `password`, `status`, `payPhone`, `companyName`, `createTime`, `lastUpdateTime`, `accountBankName`, `deleted`, `accountChildBankName`,`flag`) VALUES
-- ('1001', '10001', '3110710006261382229', '9a440f1f071dad3043b6c8ba93bb761c', '2', '15500000002', '环球贸易有限公司', '2016-07-01 15:44:17', '2016-08-29 14:41:08', '中信银行', '0', '中信银行上海自贸区分行',2);
-- INSERT INTO `kitt`.`pay_userfundaccounts` (`id`, `userId`, `account`, `password`, `status`, `payPhone`, `companyName`, `createTime`, `lastUpdateTime`, `accountBankName`, `deleted`, `accountChildBankName`,`flag`) VALUES
-- ('1002', '10002', '3110710006261346971', 'ea8753722c0e8ecde195d6adb8ba7c0d', '2', '15500000003', '孟州市至信有限责任公司', '2016-07-01 15:44:17', '2016-08-29 14:41:08', '中信银行', '0', '中信银行上海自贸区分行',2);

