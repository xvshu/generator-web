CREATE TABLE `mq_log` (
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