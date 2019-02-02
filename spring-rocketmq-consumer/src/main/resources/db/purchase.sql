-- ----------------------------
-- Table structure for `awifi_msg_confirmed`
-- ----------------------------
DROP TABLE IF EXISTS `awifi_msg_confirmed`;
CREATE TABLE `awifi_msg_confirmed` (
  `id` varchar(32) NOT NULL,
  `order_number` varchar(64) NOT NULL,
  `stocks` varchar(32) DEFAULT NULL,
  `point` varchar(32) DEFAULT NULL,
  `crt_time` datetime DEFAULT NULL,
  `upd_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `awifi_purchase_list`
-- ----------------------------
DROP TABLE IF EXISTS `awifi_purchase_list`;
CREATE TABLE `awifi_purchase_list` (
  `id` varchar(64) NOT NULL,
  `user_id` varchar(32) NOT NULL,
  `order_number` varchar(64) NOT NULL,
  `product_id` varchar(32) NOT NULL,
  `crt_time` datetime DEFAULT NULL,
  `upd_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `awifi_stocks_info`
-- ----------------------------
DROP TABLE IF EXISTS `awifi_stocks_info`;
CREATE TABLE `awifi_stocks_info` (
  `id` varchar(32) NOT NULL,
  `address` varchar(32) NOT NULL,
  `product_id` varchar(32) NOT NULL,
  `stocks_number` bigint(20) NOT NULL,
  `crt_time` datetime DEFAULT NULL,
  `upd_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of awifi_stocks_info
-- ----------------------------
INSERT INTO `awifi_stocks_info` VALUES ('1', '', 'pd123', '9870', '2018-11-20 16:55:08', '2018-12-04 00:00:00');

-- ----------------------------
-- Table structure for `awifi_user_info`
-- ----------------------------
DROP TABLE IF EXISTS `awifi_user_info`;
CREATE TABLE `awifi_user_info` (
  `id` varchar(32) NOT NULL,
  `user_id` varchar(32) NOT NULL,
  `user_point` bigint(20) DEFAULT NULL,
  `account` varchar(32) NOT NULL,
  `fund_amount` bigint(20) DEFAULT NULL,
  `crt_time` datetime DEFAULT NULL,
  `upd_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of awifi_user_info
-- ----------------------------
INSERT INTO `awifi_user_info` VALUES ('1', 'qq123', '1296', '524111', '6666', '2018-11-20 16:46:57', '2018-12-04 00:00:00');

