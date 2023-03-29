CREATE TABLE `rule_engine_condition` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `description` varchar(500) DEFAULT NULL,
  `workspace_id` int(11) DEFAULT NULL,
  `create_user_id` int(11) DEFAULT NULL,
  `create_user_name` varchar(50) DEFAULT NULL,
  `left_type` tinyint(4) DEFAULT NULL,
  `left_value` varchar(2000) DEFAULT NULL,
  `left_value_type` varchar(20) NOT NULL,
  `right_type` tinyint(4) DEFAULT NULL,
  `right_value_type` varchar(20) NOT NULL,
  `right_value` varchar(2000) DEFAULT NULL,
  `symbol` varchar(20) DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `deleted` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `rule_engine_condition_name_index` (`name`),
  KEY `rule_engine_condition_workspace_id_index` (`workspace_id`)
) ENGINE=InnoDB AUTO_INCREMENT=149 DEFAULT CHARSET=utf8;

CREATE TABLE `rule_engine_condition_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `rule_id` int(11) DEFAULT NULL,
  `order_no` int(11) NOT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  `deleted` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `rule_engine_condition_group_rule_id_index` (`rule_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2028 DEFAULT CHARSET=utf8;

CREATE TABLE `rule_engine_condition_group_condition` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `condition_id` int(11) NOT NULL,
  `condition_group_id` int(11) NOT NULL,
  `order_no` int(11) NOT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  `deleted` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `rule_engine_condition_group_condition_condition_group_id_index` (`condition_group_id`),
  KEY `rule_engine_condition_group_condition_condition_id_index` (`condition_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3019 DEFAULT CHARSET=utf8;

CREATE TABLE `rule_engine_decision_table` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `code` varchar(50) NOT NULL,
  `description` varchar(500) DEFAULT NULL,
  `workspace_id` int(11) DEFAULT NULL,
  `workspace_code` varchar(20) DEFAULT NULL,
  `table_data` json DEFAULT NULL,
  `strategy_type` tinyint(4) DEFAULT NULL,
  `create_user_id` int(11) DEFAULT NULL,
  `create_user_name` varchar(100) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `current_version` varchar(10) DEFAULT NULL,
  `publish_version` varchar(10) DEFAULT NULL,
  `abnormal_alarm` json DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  `deleted` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `rule_engine_rule_code_workspace_id_index` (`workspace_id`),
  KEY `rule_engine_decision_table_code_index` (`code`),
  KEY `rule_engine_decision_table_workspace_code_index` (`workspace_code`)
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8;

CREATE TABLE `rule_engine_decision_table_publish` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `decision_table_id` int(11) NOT NULL,
  `decision_table_code` varchar(50) DEFAULT NULL,
  `workspace_id` int(11) DEFAULT NULL,
  `workspace_code` varchar(20) DEFAULT NULL,
  `data` json DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `version` varchar(10) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  `deleted` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `rule_engine_decision_table_publish_decision_table_code_index` (`decision_table_code`),
  KEY `rule_engine_decision_table_publish_decision_table_id_index` (`decision_table_id`),
  KEY `rule_engine_decision_table_publish_workspace_code_index` (`workspace_code`),
  KEY `rule_engine_decision_table_publish_workspace_id_index` (`workspace_id`)
) ENGINE=InnoDB AUTO_INCREMENT=349 DEFAULT CHARSET=utf8;

CREATE TABLE `rule_engine_input_parameter` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `code` varchar(50) NOT NULL,
  `workspace_id` int(11) DEFAULT NULL,
  `create_user_id` int(11) DEFAULT NULL,
  `create_user_name` varchar(50) DEFAULT NULL,
  `value_type` varchar(20) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  `deleted` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `rule_engine_input_parameter_name_code_index` (`name`,`code`),
  KEY `rule_engine_input_parameter_value_type_index` (`value_type`)
) ENGINE=InnoDB AUTO_INCREMENT=166 DEFAULT CHARSET=utf8;

CREATE TABLE `rule_engine_function` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  `executor` varchar(50) NOT NULL,
  `return_value_type` varchar(50) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT NULL,
  `deleted` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8;

CREATE TABLE `rule_engine_function_param` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `function_id` int(11) NOT NULL,
  `param_name` varchar(100) DEFAULT NULL,
  `param_code` varchar(100) DEFAULT NULL,
  `value_type` varchar(50) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT NULL,
  `deleted` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `rule_engine_function_param_function_id_index` (`function_id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8;

CREATE TABLE `rule_engine_function_value` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `function_id` int(11) NOT NULL,
  `variable_id` int(11) NOT NULL,
  `param_name` varchar(100) DEFAULT NULL,
  `param_code` varchar(100) DEFAULT NULL,
  `type` int(11) DEFAULT NULL COMMENT '0:规则入参，1:变量,2:固定值',
  `value_type` varchar(50) DEFAULT NULL,
  `value` varchar(2000) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  `deleted` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `rule_engine_function_value_function_id_variable_id_index` (`function_id`,`variable_id`)
) ENGINE=InnoDB AUTO_INCREMENT=491 DEFAULT CHARSET=utf8;

CREATE TABLE `rule_engine_general_rule` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `code` varchar(50) NOT NULL,
  `rule_id` int(11) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  `workspace_id` int(11) DEFAULT NULL,
  `workspace_code` varchar(20) DEFAULT NULL,
  `create_user_id` int(11) DEFAULT NULL,
  `create_user_name` varchar(100) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `current_version` varchar(10) DEFAULT NULL,
  `publish_version` varchar(10) DEFAULT NULL,
  `enable_default_action` tinyint(4) DEFAULT NULL,
  `default_action_value` varchar(2000) DEFAULT NULL,
  `default_action_type` tinyint(4) DEFAULT NULL,
  `default_action_value_type` varchar(50) DEFAULT NULL,
  `abnormal_alarm` json DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  `deleted` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `rule_engine_rule_code_workspace_id_index` (`code`,`workspace_id`),
  KEY `rule_engine_general_rule_code_index` (`code`),
  KEY `rule_engine_general_rule_workspace_code_index` (`workspace_code`)
) ENGINE=InnoDB AUTO_INCREMENT=214 DEFAULT CHARSET=utf8;

CREATE TABLE `rule_engine_general_rule_publish` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `general_rule_id` int(11) NOT NULL,
  `general_rule_code` varchar(50) DEFAULT NULL,
  `workspace_id` int(11) DEFAULT NULL,
  `workspace_code` varchar(20) DEFAULT NULL,
  `data` json DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `version` varchar(10) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  `deleted` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `rule_engine_rule_publish_rule_code_index` (`general_rule_code`),
  KEY `rule_engine_rule_publish_rule_id_index` (`general_rule_id`),
  KEY `rule_engine_general_rule_publish_workspace_code_index` (`workspace_code`),
  KEY `rule_engine_general_rule_publish_workspace_id_index` (`workspace_id`)
) ENGINE=InnoDB AUTO_INCREMENT=432 DEFAULT CHARSET=utf8;

CREATE TABLE `rule_engine_group_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group_id` int(11) NOT NULL,
  `data_id` int(11) NOT NULL,
  `data_type` tinyint(4) NOT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  `deleted` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='规则用户组与数据权限';

CREATE TABLE `rule_engine_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL COMMENT '用户id',
  `description` varchar(500) DEFAULT NULL,
  `parent_id` int(11) DEFAULT NULL,
  `icon` varchar(100) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,
  `menu_path` varchar(500) NOT NULL COMMENT '菜单路径',
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;

CREATE TABLE `rule_engine_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT '名称',
  `code` varchar(50) NOT NULL,
  `description` varchar(300) DEFAULT NULL COMMENT '描述',
  `parent_id` int(11) DEFAULT NULL COMMENT '此角色的父级',
  `role_path` varchar(500) NOT NULL COMMENT '角色路径',
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `rule_engine_role_parent_id_index` (`parent_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

CREATE TABLE `rule_engine_role_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL COMMENT '用户id',
  `menu_id` varchar(500) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `rule_engine_role_menu_role_id_index` (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8;

CREATE TABLE `rule_engine_rule` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `code` varchar(50) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  `create_user_id` int(11) DEFAULT NULL,
  `create_user_name` varchar(100) DEFAULT NULL,
  `action_value` varchar(2000) DEFAULT NULL,
  `action_type` tinyint(4) DEFAULT NULL,
  `action_value_type` varchar(50) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  `deleted` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=646 DEFAULT CHARSET=utf8;

CREATE TABLE `rule_engine_rule_set` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `code` varchar(50) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  `workspace_id` int(11) DEFAULT NULL,
  `workspace_code` varchar(20) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `current_version` varchar(10) DEFAULT NULL,
  `publish_version` varchar(10) DEFAULT NULL,
  `strategy_type` tinyint(4) DEFAULT NULL,
  `create_user_id` int(11) DEFAULT NULL,
  `create_user_name` varchar(100) DEFAULT NULL,
  `enable_default_rule` bigint(20) DEFAULT NULL,
  `default_rule_id` int(11) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  `deleted` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `rule_engine_rule_set_code_index` (`code`),
  KEY `rule_engine_rule_set_workspace_code_index` (`workspace_code`),
  KEY `rule_engine_rule_set_workspace_id_index` (`workspace_id`)
) ENGINE=InnoDB AUTO_INCREMENT=178 DEFAULT CHARSET=utf8;

CREATE TABLE `rule_engine_rule_set_publish` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `rule_set_id` int(11) NOT NULL,
  `rule_set_code` varchar(50) DEFAULT NULL,
  `workspace_id` int(11) DEFAULT NULL,
  `workspace_code` varchar(20) DEFAULT NULL,
  `data` json DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `version` varchar(10) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  `deleted` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `rule_engine_rule_set_publish_rule_set_code_index` (`rule_set_code`),
  KEY `rule_engine_rule_set_publish_workspace_id_index` (`workspace_id`)
) ENGINE=InnoDB AUTO_INCREMENT=137 DEFAULT CHARSET=utf8;

CREATE TABLE `rule_engine_rule_set_rule` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `rule_set_id` int(11) DEFAULT NULL,
  `rule_id` int(11) DEFAULT NULL,
  `order_no` int(11) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  `deleted` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `rule_engine_rule_set_rule_rule_set_id_index` (`rule_set_id`)
) ENGINE=InnoDB AUTO_INCREMENT=394 DEFAULT CHARSET=utf8;

CREATE TABLE `rule_engine_system_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` int(11) DEFAULT NULL,
  `username` varchar(50) DEFAULT NULL,
  `description` varchar(300) DEFAULT NULL,
  `tag` varchar(50) DEFAULT NULL,
  `ip` varchar(30) NOT NULL COMMENT '请求ip',
  `browser` varchar(50) DEFAULT NULL COMMENT '浏览器',
  `browser_version` varchar(50) DEFAULT NULL COMMENT '浏览器版本',
  `system` varchar(100) NOT NULL COMMENT '请求者系统',
  `detailed` varchar(500) NOT NULL COMMENT '请求者系统详情',
  `mobile` tinyint(1) DEFAULT NULL COMMENT '是否为移动平台',
  `ages` mediumtext COMMENT '请求参数',
  `request_url` varchar(300) NOT NULL COMMENT '请求url',
  `end_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '请求结束时间',
  `running_time` bigint(10) DEFAULT NULL COMMENT '运行时间',
  `return_value` mediumtext,
  `exception` text COMMENT '异常',
  `request_id` varchar(200) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `rule_engine_system_log_ip_index` (`ip`),
  KEY `rule_engine_system_log_request_id_index` (`request_id`),
  KEY `rule_engine_system_log_user_id_index` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3820 DEFAULT CHARSET=utf8;

CREATE TABLE `rule_engine_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(30) NOT NULL,
  `password` varchar(50) NOT NULL,
  `email` varchar(50) DEFAULT NULL,
  `phone` bigint(16) DEFAULT NULL,
  `avatar` varchar(200) DEFAULT NULL COMMENT '头像',
  `sex` varchar(2) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  `deleted` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `rule_engine_user_email_index` (`email`),
  KEY `rule_engine_user_username_index` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='规则引擎用户表';

CREATE TABLE `rule_engine_user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `role_id` int(11) NOT NULL COMMENT '角色id',
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `rule_engine_user_role_user_id_index` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

CREATE TABLE `rule_engine_user_workspace` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(30) NOT NULL,
  `workspace_id` int(11) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  `deleted` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `rule_engine_user_workspace_user_id_index` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='用户工作空间';

CREATE TABLE `rule_engine_variable` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `description` varchar(500) DEFAULT NULL,
  `value_type` varchar(20) DEFAULT NULL,
  `workspace_id` int(11) DEFAULT NULL,
  `create_user_id` int(11) DEFAULT NULL,
  `create_user_name` varchar(50) DEFAULT NULL,
  `type` tinyint(4) DEFAULT NULL,
  `value` varchar(2000) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  `deleted` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `rule_engine_variable_name_index` (`name`),
  KEY `rule_engine_variable_value_type_index` (`value_type`)
) ENGINE=InnoDB AUTO_INCREMENT=165 DEFAULT CHARSET=utf8;

CREATE TABLE `rule_engine_workspace` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(20) DEFAULT NULL,
  `name` varchar(30) NOT NULL,
  `access_key_id` varchar(100) DEFAULT NULL,
  `access_key_secret` varchar(100) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  `deleted` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `rule_engine_workspace_code_index` (`code`),
  KEY `rule_engine_workspace_name_index` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='工作空间';


INSERT INTO `rule_engine_function` (`id`, `name`, `description`, `executor`, `return_value_type`, `create_time`, `update_time`, `deleted`) VALUES ('1','是否为邮箱','是否为邮箱函数','isEmailFunction','BOOLEAN','2020-09-11 20:26:14','2020-07-16 13:00:43','0');
INSERT INTO `rule_engine_function` (`id`, `name`, `description`, `executor`, `return_value_type`, `create_time`, `update_time`, `deleted`) VALUES ('2','是否为空集合','是否为空集合函数','isEmptyCollectionFunction','BOOLEAN','2020-09-11 20:26:14','2020-07-19 18:54:10','0');
INSERT INTO `rule_engine_function` (`id`, `name`, `description`, `executor`, `return_value_type`, `create_time`, `update_time`, `deleted`) VALUES ('3','发送邮件','发送邮件函数','sendEmailFunction','BOOLEAN','2020-09-11 20:26:14','2020-08-18 17:06:45','0');
INSERT INTO `rule_engine_function` (`id`, `name`, `description`, `executor`, `return_value_type`, `create_time`, `update_time`, `deleted`) VALUES ('4','求集合大小',null,'collectionSizeFunction','NUMBER','2020-09-11 20:26:14','2020-08-28 14:39:39','0');
INSERT INTO `rule_engine_function` (`id`, `name`, `description`, `executor`, `return_value_type`, `create_time`, `update_time`, `deleted`) VALUES ('6','是否为空字符串','','isEmptyFunction','BOOLEAN','2020-11-15 00:28:25','2020-08-28 14:43:52','0');
INSERT INTO `rule_engine_function` (`id`, `name`, `description`, `executor`, `return_value_type`, `create_time`, `update_time`, `deleted`) VALUES ('7','求绝对值',null,'mathAbsFunction','NUMBER','2020-09-11 20:26:14','2020-08-28 14:45:04','0');
INSERT INTO `rule_engine_function` (`id`, `name`, `description`, `executor`, `return_value_type`, `create_time`, `update_time`, `deleted`) VALUES ('8','返回集合中第几个元素','不存在则返回null','getCollectionElementsFunction','STRING','2020-09-11 20:26:14','2020-08-30 02:05:37','0');
INSERT INTO `rule_engine_function` (`id`, `name`, `description`, `executor`, `return_value_type`, `create_time`, `update_time`, `deleted`) VALUES ('9','在..之间','','isBetweenFunction','BOOLEAN','2020-09-11 20:26:14','2020-08-30 02:16:51','0');
INSERT INTO `rule_engine_function` (`id`, `name`, `description`, `executor`, `return_value_type`, `create_time`, `update_time`, `deleted`) VALUES ('10','求平均值','集合中必须为number类型的值','avgFunction','NUMBER','2020-09-11 20:26:14','2020-09-01 13:41:44','0');
INSERT INTO `rule_engine_function` (`id`, `name`, `description`, `executor`, `return_value_type`, `create_time`, `update_time`, `deleted`) VALUES ('11','集合中最大值','集合中必须为number类型的值','collectionMaxFunction','NUMBER','2020-09-11 20:26:14','2020-09-01 13:48:32','0');
INSERT INTO `rule_engine_function` (`id`, `name`, `description`, `executor`, `return_value_type`, `create_time`, `update_time`, `deleted`) VALUES ('12','集合中最小值','集合中必须为number类型的值','collectionMinFunction','NUMBER','2020-09-11 20:26:14','2020-09-01 13:49:12','0');
INSERT INTO `rule_engine_function` (`id`, `name`, `description`, `executor`, `return_value_type`, `create_time`, `update_time`, `deleted`) VALUES ('13','字符串的长度','','stringLengthFunction','NUMBER','2020-09-11 20:26:14','2020-09-01 13:50:13','0');
INSERT INTO `rule_engine_function` (`id`, `name`, `description`, `executor`, `return_value_type`, `create_time`, `update_time`, `deleted`) VALUES ('14','字符串去除前后空格',null,'stringTrimFunction','STRING','2020-09-11 20:26:14','2020-09-01 13:51:14','0');
INSERT INTO `rule_engine_function` (`id`, `name`, `description`, `executor`, `return_value_type`, `create_time`, `update_time`, `deleted`) VALUES ('15','求和','集合中必须为number类型的值','sumFunction','NUMBER','2020-09-11 20:26:14','2020-09-01 13:52:08','0');
INSERT INTO `rule_engine_function` (`id`, `name`, `description`, `executor`, `return_value_type`, `create_time`, `update_time`, `deleted`) VALUES ('16','验证是否为手机号码',null,'isMobileFunction','BOOLEAN','2020-09-11 20:26:14','2020-09-01 13:53:17','0');
INSERT INTO `rule_engine_function` (`id`, `name`, `description`, `executor`, `return_value_type`, `create_time`, `update_time`, `deleted`) VALUES ('17','是否为身份证',null,'isCitizenIdFunction','BOOLEAN','2020-09-11 20:26:14','2020-09-01 13:54:34','0');
INSERT INTO `rule_engine_function` (`id`, `name`, `description`, `executor`, `return_value_type`, `create_time`, `update_time`, `deleted`) VALUES ('18','字符串转为集合',null,'stringToCollectionFunction','COLLECTION','2020-09-11 20:26:14','2020-09-01 14:33:48','0');
INSERT INTO `rule_engine_function` (`id`, `name`, `description`, `executor`, `return_value_type`, `create_time`, `update_time`, `deleted`) VALUES ('21','字符串替换',null,'stringReplaceFunction','STRING','2020-11-18 23:50:09','2020-11-18 23:50:10','0');
INSERT INTO `rule_engine_function` (`id`, `name`, `description`, `executor`, `return_value_type`, `create_time`, `update_time`, `deleted`) VALUES ('22','集合去除重复',null,'collectionDeduplicationFunction','COLLECTION','2020-11-19 00:00:03','2020-11-19 00:00:05','0');
INSERT INTO `rule_engine_function` (`id`, `name`, `description`, `executor`, `return_value_type`, `create_time`, `update_time`, `deleted`) VALUES ('23','获取当前时间','格式例如：yyyy-MM-dd HH:mm:ss;时区例如：Asia/Shanghai;','currentDateFunction','STRING','2021-01-07 14:11:21','2020-11-19 00:37:34','0');
INSERT INTO `rule_engine_function` (`id`, `name`, `description`, `executor`, `return_value_type`, `create_time`, `update_time`, `deleted`) VALUES ('24','获取手机号码所在省份',null,'mobilePhoneProvinceFunction','STRING','2020-12-13 13:28:58','2020-12-13 13:28:59','0');
INSERT INTO `rule_engine_function` (`id`, `name`, `description`, `executor`, `return_value_type`, `create_time`, `update_time`, `deleted`) VALUES ('25','获取JSON中指定的值返回STRING类型','例如：JOSN数据为:{"name":"abc"},获取name的值通过JSON值路径配置为$.name,更多使用方法待文档补全。','parseJsonStringFunction','STRING','2020-12-13 13:56:04','2020-12-13 13:50:52','0');
INSERT INTO `rule_engine_function` (`id`, `name`, `description`, `executor`, `return_value_type`, `create_time`, `update_time`, `deleted`) VALUES ('26','获取JSON中指定的值返回NUMBER类型','例如：JOSN数据为:{"age":"18"},获取name的值通过JSON值路径配置为$.age,更多使用方法待文档补全。','parseJsonNumberFunction','NUMBER','2020-12-13 14:08:28','2020-12-13 14:08:29','0');
INSERT INTO `rule_engine_function` (`id`, `name`, `description`, `executor`, `return_value_type`, `create_time`, `update_time`, `deleted`) VALUES ('27','字母转小写',null,'letterToLowerCaseFunction','STRING','2020-12-24 00:16:07','2020-12-24 00:16:08','0');
INSERT INTO `rule_engine_function` (`id`, `name`, `description`, `executor`, `return_value_type`, `create_time`, `update_time`, `deleted`) VALUES ('28','字母转大写',null,'letterToUpperCaseFunction','STRING','2020-12-24 00:16:38','2020-12-24 00:16:39','0');

INSERT INTO `rule_engine_function_param` (`id`, `function_id`, `param_name`, `param_code`, `value_type`, `create_time`, `update_time`, `deleted`) VALUES ('1','1','普通参数','value','STRING','2020-08-27 17:43:54','2020-07-16 13:01:21','0');
INSERT INTO `rule_engine_function_param` (`id`, `function_id`, `param_name`, `param_code`, `value_type`, `create_time`, `update_time`, `deleted`) VALUES ('2','2','集合','list','COLLECTION','2020-08-27 17:43:53','2020-07-19 18:54:39','0');
INSERT INTO `rule_engine_function_param` (`id`, `function_id`, `param_name`, `param_code`, `value_type`, `create_time`, `update_time`, `deleted`) VALUES ('3','3','服务器地址','mailSmtpHost','STRING','2020-08-28 14:40:49','2020-08-18 17:09:05','0');
INSERT INTO `rule_engine_function_param` (`id`, `function_id`, `param_name`, `param_code`, `value_type`, `create_time`, `update_time`, `deleted`) VALUES ('4','3','发送人','user','STRING','2020-08-28 14:40:49','2020-08-18 17:09:20','0');
INSERT INTO `rule_engine_function_param` (`id`, `function_id`, `param_name`, `param_code`, `value_type`, `create_time`, `update_time`, `deleted`) VALUES ('5','3','发送人密码','password','STRING','2020-08-28 14:40:49','2020-08-18 17:09:44','0');
INSERT INTO `rule_engine_function_param` (`id`, `function_id`, `param_name`, `param_code`, `value_type`, `create_time`, `update_time`, `deleted`) VALUES ('6','3','邮件接收人','tos','COLLECTION','2020-08-27 17:43:54','2020-08-18 17:10:07','0');
INSERT INTO `rule_engine_function_param` (`id`, `function_id`, `param_name`, `param_code`, `value_type`, `create_time`, `update_time`, `deleted`) VALUES ('7','3','邮件标题','title','STRING','2020-08-27 17:43:53','2020-08-18 17:10:33','0');
INSERT INTO `rule_engine_function_param` (`id`, `function_id`, `param_name`, `param_code`, `value_type`, `create_time`, `update_time`, `deleted`) VALUES ('8','3','邮件内容','text','STRING','2020-08-27 17:43:53','2020-08-18 17:10:50','0');
INSERT INTO `rule_engine_function_param` (`id`, `function_id`, `param_name`, `param_code`, `value_type`, `create_time`, `update_time`, `deleted`) VALUES ('9','4','集合','list','COLLECTION','2020-08-28 14:40:29','2020-08-28 14:40:31','0');
INSERT INTO `rule_engine_function_param` (`id`, `function_id`, `param_name`, `param_code`, `value_type`, `create_time`, `update_time`, `deleted`) VALUES ('11','6','字符串','value','STRING','2020-08-28 14:44:22','2020-08-28 14:44:24','0');
INSERT INTO `rule_engine_function_param` (`id`, `function_id`, `param_name`, `param_code`, `value_type`, `create_time`, `update_time`, `deleted`) VALUES ('12','7','数值','value','NUMBER','2020-08-28 14:45:28','2020-08-28 14:45:30','0');
INSERT INTO `rule_engine_function_param` (`id`, `function_id`, `param_name`, `param_code`, `value_type`, `create_time`, `update_time`, `deleted`) VALUES ('13','3','端口号','mailSmtpPort','NUMBER','2020-08-29 01:36:00','2020-08-29 01:36:02','0');
INSERT INTO `rule_engine_function_param` (`id`, `function_id`, `param_name`, `param_code`, `value_type`, `create_time`, `update_time`, `deleted`) VALUES ('14','8','集合','list','COLLECTION','2020-08-30 02:06:06','2020-08-30 02:06:07','0');
INSERT INTO `rule_engine_function_param` (`id`, `function_id`, `param_name`, `param_code`, `value_type`, `create_time`, `update_time`, `deleted`) VALUES ('15','8','索引','index','NUMBER','2020-08-30 02:06:24','2020-08-30 02:06:25','0');
INSERT INTO `rule_engine_function_param` (`id`, `function_id`, `param_name`, `param_code`, `value_type`, `create_time`, `update_time`, `deleted`) VALUES ('16','9','数值','value','NUMBER','2020-08-30 02:17:18','2020-08-30 02:17:19','0');
INSERT INTO `rule_engine_function_param` (`id`, `function_id`, `param_name`, `param_code`, `value_type`, `create_time`, `update_time`, `deleted`) VALUES ('17','9','最小','min','NUMBER','2020-08-30 02:17:37','2020-08-30 02:17:39','0');
INSERT INTO `rule_engine_function_param` (`id`, `function_id`, `param_name`, `param_code`, `value_type`, `create_time`, `update_time`, `deleted`) VALUES ('18','9','最大','max','NUMBER','2020-08-30 02:17:57','2020-08-30 02:17:58','0');
INSERT INTO `rule_engine_function_param` (`id`, `function_id`, `param_name`, `param_code`, `value_type`, `create_time`, `update_time`, `deleted`) VALUES ('19','10','集合','list','COLLECTION','2020-09-01 13:55:40','2020-09-01 13:42:10','0');
INSERT INTO `rule_engine_function_param` (`id`, `function_id`, `param_name`, `param_code`, `value_type`, `create_time`, `update_time`, `deleted`) VALUES ('20','10','小树位','scale','NUMBER','2020-09-01 13:55:40','2020-09-01 13:46:31','0');
INSERT INTO `rule_engine_function_param` (`id`, `function_id`, `param_name`, `param_code`, `value_type`, `create_time`, `update_time`, `deleted`) VALUES ('21','11','集合','list','COLLECTION','2020-09-01 13:55:40','2020-09-01 13:46:31','0');
INSERT INTO `rule_engine_function_param` (`id`, `function_id`, `param_name`, `param_code`, `value_type`, `create_time`, `update_time`, `deleted`) VALUES ('22','12','集合','list','COLLECTION','2020-09-01 13:55:40','2020-09-01 13:49:34','0');
INSERT INTO `rule_engine_function_param` (`id`, `function_id`, `param_name`, `param_code`, `value_type`, `create_time`, `update_time`, `deleted`) VALUES ('23','13','字符串','value','STRING','2020-09-01 13:55:40','2020-09-01 13:50:41','0');
INSERT INTO `rule_engine_function_param` (`id`, `function_id`, `param_name`, `param_code`, `value_type`, `create_time`, `update_time`, `deleted`) VALUES ('24','14','字符串','value','STRING','2020-09-01 13:55:40','2020-09-01 13:51:30','0');
INSERT INTO `rule_engine_function_param` (`id`, `function_id`, `param_name`, `param_code`, `value_type`, `create_time`, `update_time`, `deleted`) VALUES ('25','15','集合','list','COLLECTION','2020-09-01 13:55:39','2020-09-01 13:46:31','0');
INSERT INTO `rule_engine_function_param` (`id`, `function_id`, `param_name`, `param_code`, `value_type`, `create_time`, `update_time`, `deleted`) VALUES ('26','16','手机号','mobile','STRING','2020-09-01 13:55:39','2020-09-01 13:53:56','0');
INSERT INTO `rule_engine_function_param` (`id`, `function_id`, `param_name`, `param_code`, `value_type`, `create_time`, `update_time`, `deleted`) VALUES ('27','17','身份证号','citizenId','STRING','2020-09-01 13:55:39','2020-09-01 13:54:59','0');
INSERT INTO `rule_engine_function_param` (`id`, `function_id`, `param_name`, `param_code`, `value_type`, `create_time`, `update_time`, `deleted`) VALUES ('28','18','字符串','value','STRING','2020-09-01 14:34:22','2020-09-01 14:34:24','0');
INSERT INTO `rule_engine_function_param` (`id`, `function_id`, `param_name`, `param_code`, `value_type`, `create_time`, `update_time`, `deleted`) VALUES ('29','18','分隔符','regex','STRING','2020-09-01 14:34:51','2020-09-01 14:34:52','0');
INSERT INTO `rule_engine_function_param` (`id`, `function_id`, `param_name`, `param_code`, `value_type`, `create_time`, `update_time`, `deleted`) VALUES ('30','21','字符串','value','STRING','2020-11-18 23:50:55','2020-11-18 23:50:57','0');
INSERT INTO `rule_engine_function_param` (`id`, `function_id`, `param_name`, `param_code`, `value_type`, `create_time`, `update_time`, `deleted`) VALUES ('31','21','目标','target','STRING','2020-11-18 23:51:28','2020-11-18 23:51:30','0');
INSERT INTO `rule_engine_function_param` (`id`, `function_id`, `param_name`, `param_code`, `value_type`, `create_time`, `update_time`, `deleted`) VALUES ('32','21','替身','replacement','STRING','2020-11-18 23:51:51','2020-11-18 23:51:52','0');
INSERT INTO `rule_engine_function_param` (`id`, `function_id`, `param_name`, `param_code`, `value_type`, `create_time`, `update_time`, `deleted`) VALUES ('33','22','集合','list','COLLECTION','2020-11-19 00:00:44','2020-11-19 00:00:45','0');
INSERT INTO `rule_engine_function_param` (`id`, `function_id`, `param_name`, `param_code`, `value_type`, `create_time`, `update_time`, `deleted`) VALUES ('34','23','格式','pattern','STRING','2020-11-19 00:38:14','2020-11-19 00:38:15','0');
INSERT INTO `rule_engine_function_param` (`id`, `function_id`, `param_name`, `param_code`, `value_type`, `create_time`, `update_time`, `deleted`) VALUES ('35','23','时区','timeZone','STRING','2020-11-19 00:38:44','2020-11-19 00:38:46','0');
INSERT INTO `rule_engine_function_param` (`id`, `function_id`, `param_name`, `param_code`, `value_type`, `create_time`, `update_time`, `deleted`) VALUES ('36','24','手机号','phone','STRING','2020-12-13 13:29:29','2020-12-13 13:29:30','0');
INSERT INTO `rule_engine_function_param` (`id`, `function_id`, `param_name`, `param_code`, `value_type`, `create_time`, `update_time`, `deleted`) VALUES ('37','25','JSON字符串','jsonString','STRING','2020-12-13 13:51:39','2020-12-13 13:51:41','0');
INSERT INTO `rule_engine_function_param` (`id`, `function_id`, `param_name`, `param_code`, `value_type`, `create_time`, `update_time`, `deleted`) VALUES ('38','25','JSON值路径','jsonValuePath','STRING','2020-12-13 13:52:35','2020-12-13 13:52:37','0');
INSERT INTO `rule_engine_function_param` (`id`, `function_id`, `param_name`, `param_code`, `value_type`, `create_time`, `update_time`, `deleted`) VALUES ('39','26','JSON字符串','jsonString','STRING','2020-12-13 14:09:20','2020-12-13 14:09:21','0');
INSERT INTO `rule_engine_function_param` (`id`, `function_id`, `param_name`, `param_code`, `value_type`, `create_time`, `update_time`, `deleted`) VALUES ('40','26','JSON值路径','jsonValuePath','STRING','2020-12-13 14:09:56','2020-12-13 14:09:57','0');
INSERT INTO `rule_engine_function_param` (`id`, `function_id`, `param_name`, `param_code`, `value_type`, `create_time`, `update_time`, `deleted`) VALUES ('41','27','字母','letter','STRING','2020-12-24 00:17:14','2020-12-24 00:17:15','0');
INSERT INTO `rule_engine_function_param` (`id`, `function_id`, `param_name`, `param_code`, `value_type`, `create_time`, `update_time`, `deleted`) VALUES ('42','28','字母','letter','STRING','2020-12-24 00:17:29','2020-12-24 00:17:30','0');


INSERT INTO `rule_engine_group_data` (`id`, `group_id`, `data_id`, `data_type`, `create_time`, `update_time`, `deleted`) VALUES ('1','1','1','0','2020-11-21 02:42:01','2020-11-21 02:42:03','0');

INSERT INTO `rule_engine_menu` (`id`, `name`, `description`, `parent_id`, `icon`, `sort`, `menu_path`, `create_time`, `update_time`, `deleted`) VALUES ('9','规则引擎',null,null,null,'0','/','2020-11-21 03:09:56','2020-11-21 03:09:57','0');
INSERT INTO `rule_engine_menu` (`id`, `name`, `description`, `parent_id`, `icon`, `sort`, `menu_path`, `create_time`, `update_time`, `deleted`) VALUES ('10','首页',null,'9','el-icon-s-home','0','/home','2020-11-21 03:17:13','2020-11-21 03:17:13','0');
INSERT INTO `rule_engine_menu` (`id`, `name`, `description`, `parent_id`, `icon`, `sort`, `menu_path`, `create_time`, `update_time`, `deleted`) VALUES ('11','基础组件',null,'9','el-icon-menu','1','/','2020-11-21 03:17:13','2020-11-21 03:17:13','0');
INSERT INTO `rule_engine_menu` (`id`, `name`, `description`, `parent_id`, `icon`, `sort`, `menu_path`, `create_time`, `update_time`, `deleted`) VALUES ('12','规则参数',null,'11',null,'0','/inputParameter','2020-11-21 03:16:24','2020-11-21 03:16:25','0');
INSERT INTO `rule_engine_menu` (`id`, `name`, `description`, `parent_id`, `icon`, `sort`, `menu_path`, `create_time`, `update_time`, `deleted`) VALUES ('13','变量',null,'11',null,'2','/variable','2020-12-28 23:46:45','2020-12-28 23:46:45','0');
INSERT INTO `rule_engine_menu` (`id`, `name`, `description`, `parent_id`, `icon`, `sort`, `menu_path`, `create_time`, `update_time`, `deleted`) VALUES ('14','函数',null,'11',null,'3','/function','2020-12-28 23:46:45','2020-12-28 23:46:45','0');
INSERT INTO `rule_engine_menu` (`id`, `name`, `description`, `parent_id`, `icon`, `sort`, `menu_path`, `create_time`, `update_time`, `deleted`) VALUES ('15','条件',null,'11',null,'4','/condition','2020-12-28 23:46:45','2020-12-28 23:46:45','0');
INSERT INTO `rule_engine_menu` (`id`, `name`, `description`, `parent_id`, `icon`, `sort`, `menu_path`, `create_time`, `update_time`, `deleted`) VALUES ('16','功能',null,'9','el-icon-s-marketing','2','/','2020-11-21 03:17:49','2020-11-21 03:17:50','0');
INSERT INTO `rule_engine_menu` (`id`, `name`, `description`, `parent_id`, `icon`, `sort`, `menu_path`, `create_time`, `update_time`, `deleted`) VALUES ('17','普通规则',null,'16',null,'0','/generalRule','2020-12-29 21:39:59','2020-12-29 21:39:59','0');
INSERT INTO `rule_engine_menu` (`id`, `name`, `description`, `parent_id`, `icon`, `sort`, `menu_path`, `create_time`, `update_time`, `deleted`) VALUES ('18','设置',null,'9','el-icon-s-tools','4','/','2020-12-19 01:30:40','2020-12-19 01:30:40','0');
INSERT INTO `rule_engine_menu` (`id`, `name`, `description`, `parent_id`, `icon`, `sort`, `menu_path`, `create_time`, `update_time`, `deleted`) VALUES ('19','个人设置',null,'18',null,'0','/personalSettings','2020-11-21 03:52:51','2020-11-21 03:52:51','0');
INSERT INTO `rule_engine_menu` (`id`, `name`, `description`, `parent_id`, `icon`, `sort`, `menu_path`, `create_time`, `update_time`, `deleted`) VALUES ('20','系统设置',null,'18','','2','/systemSetting','2020-12-28 21:53:17','2020-12-28 21:53:17','0');
INSERT INTO `rule_engine_menu` (`id`, `name`, `description`, `parent_id`, `icon`, `sort`, `menu_path`, `create_time`, `update_time`, `deleted`) VALUES ('21','决策表',null,'16',null,'2','/decisionTable','2020-12-28 21:55:40','2020-12-28 21:55:40','0');
INSERT INTO `rule_engine_menu` (`id`, `name`, `description`, `parent_id`, `icon`, `sort`, `menu_path`, `create_time`, `update_time`, `deleted`) VALUES ('22','评分卡',null,'16',null,'3','/scoreCard','2021-01-14 09:39:48','2021-01-14 09:39:48','1');
INSERT INTO `rule_engine_menu` (`id`, `name`, `description`, `parent_id`, `icon`, `sort`, `menu_path`, `create_time`, `update_time`, `deleted`) VALUES ('23','权限管理',null,'9','el-icon-s-custom','3','/','2020-12-19 01:30:40','2020-12-19 01:30:40','0');
INSERT INTO `rule_engine_menu` (`id`, `name`, `description`, `parent_id`, `icon`, `sort`, `menu_path`, `create_time`, `update_time`, `deleted`) VALUES ('24','工作空间',null,'23',null,'1','/workspace','2020-12-19 01:31:18','2020-12-19 01:31:20','0');
INSERT INTO `rule_engine_menu` (`id`, `name`, `description`, `parent_id`, `icon`, `sort`, `menu_path`, `create_time`, `update_time`, `deleted`) VALUES ('25','规则集',null,'16',null,'1','/ruleSet','2020-12-28 21:53:08','2020-12-28 21:53:09','0');

INSERT INTO `rule_engine_role` (`id`, `name`, `code`, `description`, `parent_id`, `role_path`, `create_time`, `update_time`, `deleted`) VALUES ('1','系统管理员','admin',null,null,'1','2020-09-25 22:18:42','2020-09-25 22:18:42','0');
INSERT INTO `rule_engine_role` (`id`, `name`, `code`, `description`, `parent_id`, `role_path`, `create_time`, `update_time`, `deleted`) VALUES ('2','用户','user','','1','1@2','2020-09-25 22:19:30','2020-09-25 22:19:31','0');

INSERT INTO `rule_engine_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `deleted`) VALUES ('8','1','9','2020-11-21 03:20:17','2020-11-21 03:20:18','0');
INSERT INTO `rule_engine_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `deleted`) VALUES ('9','1','10','2020-11-21 03:20:22','2020-11-21 03:20:19','0');
INSERT INTO `rule_engine_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `deleted`) VALUES ('10','1','11','2020-11-21 03:20:23','2020-11-21 03:20:20','0');
INSERT INTO `rule_engine_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `deleted`) VALUES ('11','1','12','2020-11-21 03:20:24','2020-11-21 03:20:34','0');
INSERT INTO `rule_engine_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `deleted`) VALUES ('12','1','13','2020-11-21 03:20:26','2020-11-21 03:20:35','0');
INSERT INTO `rule_engine_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `deleted`) VALUES ('13','1','14','2020-11-21 03:20:28','2020-11-21 03:20:37','0');
INSERT INTO `rule_engine_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `deleted`) VALUES ('14','1','15','2020-11-21 03:20:29','2020-11-21 03:20:39','0');
INSERT INTO `rule_engine_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `deleted`) VALUES ('15','1','16','2020-11-21 03:20:30','2020-11-21 03:20:41','0');
INSERT INTO `rule_engine_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `deleted`) VALUES ('16','1','17','2020-11-21 03:20:32','2020-11-21 03:20:42','0');
INSERT INTO `rule_engine_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `deleted`) VALUES ('17','1','18','2020-11-21 03:55:02','2020-11-21 03:55:04','0');
INSERT INTO `rule_engine_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `deleted`) VALUES ('18','1','19','2020-11-21 03:55:08','2020-11-21 03:55:05','0');
INSERT INTO `rule_engine_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `deleted`) VALUES ('19','1','20','2020-11-21 03:55:09','2020-11-21 03:55:06','0');
INSERT INTO `rule_engine_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `deleted`) VALUES ('20','1','21','2020-11-21 03:55:09','2020-11-21 03:55:07','0');
INSERT INTO `rule_engine_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `deleted`) VALUES ('22','2','9','2020-11-21 20:50:38','2020-11-21 20:50:39','0');
INSERT INTO `rule_engine_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `deleted`) VALUES ('23','2','10','2020-11-21 20:50:45','2020-11-21 20:50:40','0');
INSERT INTO `rule_engine_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `deleted`) VALUES ('24','2','11','2020-11-21 20:50:46','2020-11-21 20:50:41','0');
INSERT INTO `rule_engine_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `deleted`) VALUES ('25','2','12','2020-11-21 20:50:47','2020-11-21 20:50:42','0');
INSERT INTO `rule_engine_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `deleted`) VALUES ('26','2','13','2020-11-21 20:50:47','2020-11-21 20:50:42','0');
INSERT INTO `rule_engine_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `deleted`) VALUES ('27','2','14','2020-11-21 20:50:48','2020-11-21 20:50:43','0');
INSERT INTO `rule_engine_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `deleted`) VALUES ('28','2','15','2020-11-21 20:50:49','2020-11-21 20:50:44','0');
INSERT INTO `rule_engine_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `deleted`) VALUES ('29','2','16','2020-11-21 20:50:49','2020-11-21 20:50:44','0');
INSERT INTO `rule_engine_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `deleted`) VALUES ('30','2','17','2020-11-21 20:51:39','2020-11-21 20:51:40','0');
INSERT INTO `rule_engine_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `deleted`) VALUES ('31','2','18','2020-11-21 20:51:38','2020-11-21 20:51:40','0');
INSERT INTO `rule_engine_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `deleted`) VALUES ('32','2','19','2020-11-21 20:51:37','2020-11-21 20:51:44','0');
INSERT INTO `rule_engine_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `deleted`) VALUES ('35','1','23','2020-12-19 01:29:48','2020-12-19 01:29:49','0');
INSERT INTO `rule_engine_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `deleted`) VALUES ('36','1','24','2020-12-19 01:31:32','2020-12-19 01:31:34','0');
INSERT INTO `rule_engine_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `deleted`) VALUES ('37','2','23','2020-12-19 01:53:04','2020-12-19 01:53:05','0');
INSERT INTO `rule_engine_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `deleted`) VALUES ('38','2','24','2020-12-19 01:53:14','2020-12-19 01:53:16','0');
INSERT INTO `rule_engine_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `deleted`) VALUES ('39','2','21','2020-12-19 15:56:26','2020-12-19 15:56:28','0');
INSERT INTO `rule_engine_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `deleted`) VALUES ('40','1','25','2020-12-28 21:53:35','2020-12-28 21:53:36','0');
INSERT INTO `rule_engine_role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `deleted`) VALUES ('41','2','25','2020-12-28 21:53:37','2020-12-28 21:53:37','0');



INSERT INTO `rule_engine_user` (`id`, `username`, `password`, `email`, `phone`, `avatar`, `sex`, `create_time`, `update_time`, `deleted`) VALUES ('2','lq','5f329d3ac22671f7b214c461e58c27f3','23123','1021231','http://oss-boot-test.oss-cn-beijing.aliyuncs.com/ruleengine/73865651.png?Expires=33148420103&OSSAccessKeyId=LTAIyEa5SulNXbQa&Signature=pBQA%2B8N7P5s3JUAK0R3UF3th5Pw%3D','女','2020-09-25 23:05:06','2021-02-04 06:28:24','0');
INSERT INTO `rule_engine_user` (`id`, `username`, `password`, `email`, `phone`, `avatar`, `sex`, `create_time`, `update_time`, `deleted`) VALUES ('3','test','5f329d3ac22671f7b214c461e58c27f3','5f329d3ac22671f7b214c461e58c27f3',null,'/static/avatar.jpg','男','2020-11-22 00:53:08','2020-11-22 00:53:09','0');

INSERT INTO `rule_engine_user_role` (`id`, `user_id`, `role_id`, `create_time`, `update_time`, `deleted`) VALUES ('3','1','1','2020-09-25 22:20:31','2020-09-25 22:20:32','0');
INSERT INTO `rule_engine_user_role` (`id`, `user_id`, `role_id`, `create_time`, `update_time`, `deleted`) VALUES ('4','2','2','2020-09-25 23:05:20','2020-09-25 23:05:21','0');
INSERT INTO `rule_engine_user_role` (`id`, `user_id`, `role_id`, `create_time`, `update_time`, `deleted`) VALUES ('5','3','2','2020-11-22 00:53:38','2020-11-22 00:53:39','0');

INSERT INTO `rule_engine_user_workspace` (`id`, `user_id`, `workspace_id`, `create_time`, `update_time`, `deleted`) VALUES ('2','2','2','2020-11-22 03:53:37','2020-11-22 03:53:39','1');
INSERT INTO `rule_engine_user_workspace` (`id`, `user_id`, `workspace_id`, `create_time`, `update_time`, `deleted`) VALUES ('3','3','2','2020-11-22 14:42:50','2020-11-22 14:42:51','0');
INSERT INTO `rule_engine_user_workspace` (`id`, `user_id`, `workspace_id`, `create_time`, `update_time`, `deleted`) VALUES ('4','2','1','2020-11-22 14:50:33','2020-11-22 14:50:34','0');
INSERT INTO `rule_engine_user_workspace` (`id`, `user_id`, `workspace_id`, `create_time`, `update_time`, `deleted`) VALUES ('5','3','1','2020-12-21 17:20:16','2020-12-21 17:20:18','0');


INSERT INTO `rule_engine_workspace` (`id`, `code`, `name`, `access_key_id`, `access_key_secret`, `description`, `create_time`, `update_time`, `deleted`) VALUES ('1','default','默认工作空间','root','123456','默认的','2020-11-21 02:41:33','2020-11-21 02:41:34','0');
INSERT INTO `rule_engine_workspace` (`id`, `code`, `name`, `access_key_id`, `access_key_secret`, `description`, `create_time`, `update_time`, `deleted`) VALUES ('2','test','测试','gdfhdgfh','sdfasdfas','供测试使用','2020-11-21 19:36:12','2020-11-21 19:36:13','0');
INSERT INTO `rule_engine_workspace` (`id`, `code`, `name`, `access_key_id`, `access_key_secret`, `description`, `create_time`, `update_time`, `deleted`) VALUES ('4','prd','线上','asdfasdf','asasdfasdfas','请勿随意修改','2020-11-07 21:49:36','2020-11-07 21:49:38','0');

