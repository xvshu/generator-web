/*
Navicat MySQL Data Transfer

Source Server         : 192.168.2.16
Source Server Version : 50612
Source Host           : 192.168.2.16:3316
Source Database       : eloancn_framework

Target Server Type    : MYSQL
Target Server Version : 50612
File Encoding         : 65001

Date: 2017-11-09 17:20:02
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for el_company
-- ----------------------------
DROP TABLE IF EXISTS `el_company`;
CREATE TABLE `el_company` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name_id` int(11) DEFAULT NULL,
  `company` varchar(255) DEFAULT NULL COMMENT '姓名',
  `create_date` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_date` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of el_company
-- ----------------------------

-- ----------------------------
-- Table structure for el_mq_log
-- ----------------------------
DROP TABLE IF EXISTS `el_mq_log`;
CREATE TABLE `el_mq_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `message_id` varchar(128) DEFAULT NULL,
  `exchange` varchar(128) DEFAULT NULL,
  `routingKey` varchar(128) DEFAULT NULL,
  `error` text,
  `message_body` text COMMENT '消息体',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_message_id` (`message_id`)
) ENGINE=InnoDB AUTO_INCREMENT=433748 DEFAULT CHARSET=utf8 COMMENT='RabbitMQ记录表';

-- ----------------------------
-- Records of el_mq_log
-- ----------------------------

-- ----------------------------
-- Table structure for el_name
-- ----------------------------
DROP TABLE IF EXISTS `el_name`;
CREATE TABLE `el_name` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(255) DEFAULT NULL COMMENT '姓名',
  `create_date` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_date` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of el_name
-- ----------------------------
