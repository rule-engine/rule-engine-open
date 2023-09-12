create table rule_engine_condition
(
    id               int auto_increment
        primary key,
    name             varchar(50)   not null,
    description      varchar(500)  null,
    workspace_id     int           null,
    create_user_id   int           null,
    create_user_name varchar(50)   null,
    left_type        tinyint       null,
    left_value       varchar(2000) null,
    left_value_type  varchar(20)   not null,
    right_type       tinyint       null,
    right_value_type varchar(20)   not null,
    right_value      varchar(2000) null,
    symbol           varchar(20)   null,
    update_time      timestamp     null,
    create_time      timestamp     null,
    deleted          tinyint       null
);

create index rule_engine_condition_name_index
    on rule_engine_condition (name);

create index rule_engine_condition_workspace_id_index
    on rule_engine_condition (workspace_id);

INSERT INTO rule_engine_condition (id, name, description, workspace_id, create_user_id, create_user_name, left_type, left_value, left_value_type, right_type, right_value_type, right_value, symbol, update_time, create_time, deleted) VALUES (149, '测试条件', null, 2, 1, 'admin', 0, '166', 'STRING', 2, 'STRING', '123', 'EQ', '2023-08-11 12:30:59', '2023-08-11 12:30:59', 0);
INSERT INTO rule_engine_condition (id, name, description, workspace_id, create_user_id, create_user_name, left_type, left_value, left_value_type, right_type, right_value_type, right_value, symbol, update_time, create_time, deleted) VALUES (150, '测试2', null, 2, 1, 'admin', 1, '165', 'BOOLEAN', 2, 'BOOLEAN', 'true', 'EQ', '2023-08-11 12:38:34', '2023-08-11 12:38:34', 0);
create table rule_engine_condition_group
(
    id          int auto_increment
        primary key,
    name        varchar(50) not null,
    rule_id     int         null,
    order_no    int         not null,
    create_time timestamp   null,
    update_time timestamp   null,
    deleted     tinyint     null
);

create index rule_engine_condition_group_rule_id_index
    on rule_engine_condition_group (rule_id);

INSERT INTO rule_engine_condition_group (id, name, rule_id, order_no, create_time, update_time, deleted) VALUES (2028, '条件组', 646, 1, '2023-08-11 12:29:49', '2023-08-11 12:29:49', 0);
create table rule_engine_condition_group_condition
(
    id                 int auto_increment
        primary key,
    condition_id       int       not null,
    condition_group_id int       not null,
    order_no           int       not null,
    create_time        timestamp null,
    update_time        timestamp null,
    deleted            tinyint   null
);

create index rule_engine_condition_group_condition_condition_group_id_index
    on rule_engine_condition_group_condition (condition_group_id);

create index rule_engine_condition_group_condition_condition_id_index
    on rule_engine_condition_group_condition (condition_id);

INSERT INTO rule_engine_condition_group_condition (id, condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (3019, 149, 2028, 0, '2023-08-11 12:30:59', '2023-08-11 12:30:59', 0);
INSERT INTO rule_engine_condition_group_condition (id, condition_id, condition_group_id, order_no, create_time, update_time, deleted) VALUES (3020, 150, 2028, 1, '2023-08-11 12:38:34', '2023-08-11 12:38:34', 0);
create table rule_engine_data_reference
(
    id             int auto_increment
        primary key,
    data_type      tinyint     not null,
    data_id        int         not null,
    reference_data json        null,
    version        varchar(50) null,
    create_time    timestamp   null,
    update_time    timestamp   null,
    deleted        tinyint     null
);

create index rule_engine_data_reference_data_type_data_id_index
    on rule_engine_data_reference (data_type, data_id);

INSERT INTO rule_engine_data_reference (id, data_type, data_id, reference_data, version, create_time, update_time, deleted) VALUES (507, 0, 214, '{"formulaIds": [], "variableIds": [], "generalRuleIds": [], "inputParameterIds": [166]}', '1.0', '2023-08-11 12:37:25', '2023-08-11 12:37:25', 0);
INSERT INTO rule_engine_data_reference (id, data_type, data_id, reference_data, version, create_time, update_time, deleted) VALUES (508, 0, 214, '{"formulaIds": [], "variableIds": [165], "generalRuleIds": [], "inputParameterIds": [166]}', '2.0', '2023-08-11 12:38:35', '2023-08-11 12:38:35', 0);
create table rule_engine_function
(
    id                int auto_increment
        primary key,
    name              varchar(50)                         null,
    description       varchar(500)                        null,
    executor          varchar(50)                         not null,
    return_value_type varchar(50)                         null,
    create_time       timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    update_time       timestamp                           null,
    deleted           tinyint                             null
);

INSERT INTO rule_engine_function (id, name, description, executor, return_value_type, create_time, update_time, deleted) VALUES (1, '是否为邮箱', '是否为邮箱函数', 'isEmailFunction', 'BOOLEAN', '2020-09-11 20:26:14', '2020-07-16 13:00:43', 0);
INSERT INTO rule_engine_function (id, name, description, executor, return_value_type, create_time, update_time, deleted) VALUES (2, '是否为空集合', '是否为空集合函数', 'isEmptyCollectionFunction', 'BOOLEAN', '2020-09-11 20:26:14', '2020-07-19 18:54:10', 0);
INSERT INTO rule_engine_function (id, name, description, executor, return_value_type, create_time, update_time, deleted) VALUES (3, '发送邮件', '发送邮件函数', 'sendEmailFunction', 'BOOLEAN', '2020-09-11 20:26:14', '2020-08-18 17:06:45', 0);
INSERT INTO rule_engine_function (id, name, description, executor, return_value_type, create_time, update_time, deleted) VALUES (4, '求集合大小', null, 'collectionSizeFunction', 'NUMBER', '2020-09-11 20:26:14', '2020-08-28 14:39:39', 0);
INSERT INTO rule_engine_function (id, name, description, executor, return_value_type, create_time, update_time, deleted) VALUES (6, '是否为空字符串', '', 'isEmptyFunction', 'BOOLEAN', '2020-11-15 00:28:25', '2020-08-28 14:43:52', 0);
INSERT INTO rule_engine_function (id, name, description, executor, return_value_type, create_time, update_time, deleted) VALUES (7, '求绝对值', null, 'mathAbsFunction', 'NUMBER', '2020-09-11 20:26:14', '2020-08-28 14:45:04', 0);
INSERT INTO rule_engine_function (id, name, description, executor, return_value_type, create_time, update_time, deleted) VALUES (8, '返回集合中第几个元素', '不存在则返回null', 'getCollectionElementsFunction', 'STRING', '2020-09-11 20:26:14', '2020-08-30 02:05:37', 0);
INSERT INTO rule_engine_function (id, name, description, executor, return_value_type, create_time, update_time, deleted) VALUES (9, '在..之间', '', 'isBetweenFunction', 'BOOLEAN', '2020-09-11 20:26:14', '2020-08-30 02:16:51', 0);
INSERT INTO rule_engine_function (id, name, description, executor, return_value_type, create_time, update_time, deleted) VALUES (10, '求平均值', '集合中必须为number类型的值', 'avgFunction', 'NUMBER', '2020-09-11 20:26:14', '2020-09-01 13:41:44', 0);
INSERT INTO rule_engine_function (id, name, description, executor, return_value_type, create_time, update_time, deleted) VALUES (11, '集合中最大值', '集合中必须为number类型的值', 'collectionMaxFunction', 'NUMBER', '2020-09-11 20:26:14', '2020-09-01 13:48:32', 0);
INSERT INTO rule_engine_function (id, name, description, executor, return_value_type, create_time, update_time, deleted) VALUES (12, '集合中最小值', '集合中必须为number类型的值', 'collectionMinFunction', 'NUMBER', '2020-09-11 20:26:14', '2020-09-01 13:49:12', 0);
INSERT INTO rule_engine_function (id, name, description, executor, return_value_type, create_time, update_time, deleted) VALUES (13, '字符串的长度', '', 'stringLengthFunction', 'NUMBER', '2020-09-11 20:26:14', '2020-09-01 13:50:13', 0);
INSERT INTO rule_engine_function (id, name, description, executor, return_value_type, create_time, update_time, deleted) VALUES (14, '字符串去除前后空格', null, 'stringTrimFunction', 'STRING', '2020-09-11 20:26:14', '2020-09-01 13:51:14', 0);
INSERT INTO rule_engine_function (id, name, description, executor, return_value_type, create_time, update_time, deleted) VALUES (15, '求和', '集合中必须为number类型的值', 'sumFunction', 'NUMBER', '2020-09-11 20:26:14', '2020-09-01 13:52:08', 0);
INSERT INTO rule_engine_function (id, name, description, executor, return_value_type, create_time, update_time, deleted) VALUES (16, '验证是否为手机号码', null, 'isMobileFunction', 'BOOLEAN', '2020-09-11 20:26:14', '2020-09-01 13:53:17', 0);
INSERT INTO rule_engine_function (id, name, description, executor, return_value_type, create_time, update_time, deleted) VALUES (17, '是否为身份证', null, 'isCitizenIdFunction', 'BOOLEAN', '2020-09-11 20:26:14', '2020-09-01 13:54:34', 0);
INSERT INTO rule_engine_function (id, name, description, executor, return_value_type, create_time, update_time, deleted) VALUES (18, '字符串转为集合', null, 'stringToCollectionFunction', 'COLLECTION', '2020-09-11 20:26:14', '2020-09-01 14:33:48', 0);
INSERT INTO rule_engine_function (id, name, description, executor, return_value_type, create_time, update_time, deleted) VALUES (21, '字符串替换', null, 'stringReplaceFunction', 'STRING', '2020-11-18 23:50:09', '2020-11-18 23:50:10', 0);
INSERT INTO rule_engine_function (id, name, description, executor, return_value_type, create_time, update_time, deleted) VALUES (22, '集合去除重复', null, 'collectionDeduplicationFunction', 'COLLECTION', '2020-11-19 00:00:03', '2020-11-19 00:00:05', 0);
INSERT INTO rule_engine_function (id, name, description, executor, return_value_type, create_time, update_time, deleted) VALUES (23, '获取当前时间', '格式例如：yyyy-MM-dd HH:mm:ss;时区例如：Asia/Shanghai;', 'currentDateFunction', 'STRING', '2021-01-07 14:11:21', '2020-11-19 00:37:34', 0);
INSERT INTO rule_engine_function (id, name, description, executor, return_value_type, create_time, update_time, deleted) VALUES (24, '获取手机号码所在省份', null, 'mobilePhoneProvinceFunction', 'STRING', '2020-12-13 13:28:58', '2020-12-13 13:28:59', 0);
INSERT INTO rule_engine_function (id, name, description, executor, return_value_type, create_time, update_time, deleted) VALUES (25, '获取JSON中指定的值返回STRING类型', '例如：JOSN数据为:{"name":"abc"},获取name的值通过JSON值路径配置为$.name,更多使用方法待文档补全。', 'parseJsonStringFunction', 'STRING', '2020-12-13 13:56:04', '2020-12-13 13:50:52', 0);
INSERT INTO rule_engine_function (id, name, description, executor, return_value_type, create_time, update_time, deleted) VALUES (26, '获取JSON中指定的值返回NUMBER类型', '例如：JOSN数据为:{"age":"18"},获取name的值通过JSON值路径配置为$.age,更多使用方法待文档补全。', 'parseJsonNumberFunction', 'NUMBER', '2020-12-13 14:08:28', '2020-12-13 14:08:29', 0);
INSERT INTO rule_engine_function (id, name, description, executor, return_value_type, create_time, update_time, deleted) VALUES (27, '字母转小写', null, 'letterToLowerCaseFunction', 'STRING', '2020-12-24 00:16:07', '2020-12-24 00:16:08', 0);
INSERT INTO rule_engine_function (id, name, description, executor, return_value_type, create_time, update_time, deleted) VALUES (28, '字母转大写', null, 'letterToUpperCaseFunction', 'STRING', '2020-12-24 00:16:38', '2020-12-24 00:16:39', 0);
create table rule_engine_function_param
(
    id          int auto_increment
        primary key,
    function_id int                                 not null,
    param_name  varchar(100)                        null,
    param_code  varchar(100)                        null,
    value_type  varchar(50)                         null,
    create_time timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    update_time timestamp                           null,
    deleted     tinyint                             null
);

create index rule_engine_function_param_function_id_index
    on rule_engine_function_param (function_id);

INSERT INTO rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (1, 1, '普通参数', 'value', 'STRING', '2020-08-27 17:43:54', '2020-07-16 13:01:21', 0);
INSERT INTO rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (2, 2, '集合', 'list', 'COLLECTION', '2020-08-27 17:43:53', '2020-07-19 18:54:39', 0);
INSERT INTO rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (3, 3, '服务器地址', 'mailSmtpHost', 'STRING', '2020-08-28 14:40:49', '2020-08-18 17:09:05', 0);
INSERT INTO rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (4, 3, '发送人', 'user', 'STRING', '2020-08-28 14:40:49', '2020-08-18 17:09:20', 0);
INSERT INTO rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (5, 3, '发送人密码', 'password', 'STRING', '2020-08-28 14:40:49', '2020-08-18 17:09:44', 0);
INSERT INTO rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (6, 3, '邮件接收人', 'tos', 'COLLECTION', '2020-08-27 17:43:54', '2020-08-18 17:10:07', 0);
INSERT INTO rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (7, 3, '邮件标题', 'title', 'STRING', '2020-08-27 17:43:53', '2020-08-18 17:10:33', 0);
INSERT INTO rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (8, 3, '邮件内容', 'text', 'STRING', '2020-08-27 17:43:53', '2020-08-18 17:10:50', 0);
INSERT INTO rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (9, 4, '集合', 'list', 'COLLECTION', '2020-08-28 14:40:29', '2020-08-28 14:40:31', 0);
INSERT INTO rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (11, 6, '字符串', 'value', 'STRING', '2020-08-28 14:44:22', '2020-08-28 14:44:24', 0);
INSERT INTO rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (12, 7, '数值', 'value', 'NUMBER', '2020-08-28 14:45:28', '2020-08-28 14:45:30', 0);
INSERT INTO rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (13, 3, '端口号', 'mailSmtpPort', 'NUMBER', '2020-08-29 01:36:00', '2020-08-29 01:36:02', 0);
INSERT INTO rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (14, 8, '集合', 'list', 'COLLECTION', '2020-08-30 02:06:06', '2020-08-30 02:06:07', 0);
INSERT INTO rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (15, 8, '索引', 'index', 'NUMBER', '2020-08-30 02:06:24', '2020-08-30 02:06:25', 0);
INSERT INTO rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (16, 9, '数值', 'value', 'NUMBER', '2020-08-30 02:17:18', '2020-08-30 02:17:19', 0);
INSERT INTO rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (17, 9, '最小', 'min', 'NUMBER', '2020-08-30 02:17:37', '2020-08-30 02:17:39', 0);
INSERT INTO rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (18, 9, '最大', 'max', 'NUMBER', '2020-08-30 02:17:57', '2020-08-30 02:17:58', 0);
INSERT INTO rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (19, 10, '集合', 'list', 'COLLECTION', '2020-09-01 13:55:40', '2020-09-01 13:42:10', 0);
INSERT INTO rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (20, 10, '小树位', 'scale', 'NUMBER', '2020-09-01 13:55:40', '2020-09-01 13:46:31', 0);
INSERT INTO rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (21, 11, '集合', 'list', 'COLLECTION', '2020-09-01 13:55:40', '2020-09-01 13:46:31', 0);
INSERT INTO rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (22, 12, '集合', 'list', 'COLLECTION', '2020-09-01 13:55:40', '2020-09-01 13:49:34', 0);
INSERT INTO rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (23, 13, '字符串', 'value', 'STRING', '2020-09-01 13:55:40', '2020-09-01 13:50:41', 0);
INSERT INTO rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (24, 14, '字符串', 'value', 'STRING', '2020-09-01 13:55:40', '2020-09-01 13:51:30', 0);
INSERT INTO rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (25, 15, '集合', 'list', 'COLLECTION', '2020-09-01 13:55:39', '2020-09-01 13:46:31', 0);
INSERT INTO rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (26, 16, '手机号', 'mobile', 'STRING', '2020-09-01 13:55:39', '2020-09-01 13:53:56', 0);
INSERT INTO rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (27, 17, '身份证号', 'citizenId', 'STRING', '2020-09-01 13:55:39', '2020-09-01 13:54:59', 0);
INSERT INTO rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (28, 18, '字符串', 'value', 'STRING', '2020-09-01 14:34:22', '2020-09-01 14:34:24', 0);
INSERT INTO rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (29, 18, '分隔符', 'regex', 'STRING', '2020-09-01 14:34:51', '2020-09-01 14:34:52', 0);
INSERT INTO rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (30, 21, '字符串', 'value', 'STRING', '2020-11-18 23:50:55', '2020-11-18 23:50:57', 0);
INSERT INTO rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (31, 21, '目标', 'target', 'STRING', '2020-11-18 23:51:28', '2020-11-18 23:51:30', 0);
INSERT INTO rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (32, 21, '替身', 'replacement', 'STRING', '2020-11-18 23:51:51', '2020-11-18 23:51:52', 0);
INSERT INTO rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (33, 22, '集合', 'list', 'COLLECTION', '2020-11-19 00:00:44', '2020-11-19 00:00:45', 0);
INSERT INTO rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (34, 23, '格式', 'pattern', 'STRING', '2020-11-19 00:38:14', '2020-11-19 00:38:15', 0);
INSERT INTO rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (35, 23, '时区', 'timeZone', 'STRING', '2020-11-19 00:38:44', '2020-11-19 00:38:46', 0);
INSERT INTO rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (36, 24, '手机号', 'phone', 'STRING', '2020-12-13 13:29:29', '2020-12-13 13:29:30', 0);
INSERT INTO rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (37, 25, 'JSON字符串', 'jsonString', 'STRING', '2020-12-13 13:51:39', '2020-12-13 13:51:41', 0);
INSERT INTO rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (38, 25, 'JSON值路径', 'jsonValuePath', 'STRING', '2020-12-13 13:52:35', '2020-12-13 13:52:37', 0);
INSERT INTO rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (39, 26, 'JSON字符串', 'jsonString', 'STRING', '2020-12-13 14:09:20', '2020-12-13 14:09:21', 0);
INSERT INTO rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (40, 26, 'JSON值路径', 'jsonValuePath', 'STRING', '2020-12-13 14:09:56', '2020-12-13 14:09:57', 0);
INSERT INTO rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (41, 27, '字母', 'letter', 'STRING', '2020-12-24 00:17:14', '2020-12-24 00:17:15', 0);
INSERT INTO rule_engine_function_param (id, function_id, param_name, param_code, value_type, create_time, update_time, deleted) VALUES (42, 28, '字母', 'letter', 'STRING', '2020-12-24 00:17:29', '2020-12-24 00:17:30', 0);
create table rule_engine_function_value
(
    id          int auto_increment
        primary key,
    function_id int           not null,
    variable_id int           not null,
    param_name  varchar(100)  null,
    param_code  varchar(100)  null,
    type        int           null comment '0:规则入参，1:变量,2:固定值',
    value_type  varchar(50)   null,
    value       varchar(2000) null,
    create_time timestamp     null,
    update_time timestamp     null,
    deleted     tinyint       null
);

create index rule_engine_function_value_function_id_variable_id_index
    on rule_engine_function_value (function_id, variable_id);


create table rule_engine_general_rule
(
    id                        int auto_increment
        primary key,
    name                      varchar(50)   not null,
    code                      varchar(50)   not null,
    rule_id                   int           null,
    description               varchar(500)  null,
    workspace_id              int           null,
    workspace_code            varchar(20)   null,
    create_user_id            int           null,
    create_user_name          varchar(100)  null,
    status                    tinyint       null,
    current_version           varchar(10)   null,
    publish_version           varchar(10)   null,
    enable_default_action     tinyint       null,
    default_action_value      varchar(2000) null,
    default_action_type       tinyint       null,
    default_action_value_type varchar(50)   null,
    abnormal_alarm            json          null,
    create_time               timestamp     null,
    update_time               timestamp     null,
    deleted                   tinyint       null
);

create index rule_engine_general_rule_code_index
    on rule_engine_general_rule (code);

create index rule_engine_general_rule_workspace_code_index
    on rule_engine_general_rule (workspace_code);

create index rule_engine_rule_code_workspace_id_index
    on rule_engine_general_rule (code, workspace_id);

INSERT INTO rule_engine_general_rule (id, name, code, rule_id, description, workspace_id, workspace_code, create_user_id, create_user_name, status, current_version, publish_version, enable_default_action, default_action_value, default_action_type, default_action_value_type, abnormal_alarm, create_time, update_time, deleted) VALUES (214, '测试', 'test', 646, null, 2, 'test', 1, null, 1, '2.0', '1.0', null, null, null, null, null, '2023-08-11 12:29:46', '2023-08-11 12:29:46', 0);
create table rule_engine_general_rule_publish
(
    id                int auto_increment
        primary key,
    general_rule_id   int               not null,
    general_rule_code varchar(50)       null,
    general_rule_name varchar(256)      null,
    workspace_id      int               null,
    workspace_code    varchar(20)       null,
    data              json              null,
    status            tinyint           null,
    version           varchar(10)       null,
    loading_mode      tinyint default 1 null comment '加载模式，1启动加载 2懒加载 3会话',
    value_type        varchar(20)       null,
    create_time       timestamp         null,
    update_time       timestamp         null,
    deleted           tinyint           null
);

create index rule_engine_general_rule_publish_workspace_code_index
    on rule_engine_general_rule_publish (workspace_code);

create index rule_engine_general_rule_publish_workspace_id_index
    on rule_engine_general_rule_publish (workspace_id);

create index rule_engine_rule_publish_rule_code_index
    on rule_engine_general_rule_publish (general_rule_code);

create index rule_engine_rule_publish_rule_id_index
    on rule_engine_general_rule_publish (general_rule_id);

INSERT INTO rule_engine_general_rule_publish (id, general_rule_id, general_rule_code, general_rule_name, workspace_id, workspace_code, data, status, version, loading_mode, value_type, create_time, update_time, deleted) VALUES (1513, 214, 'test', '测试', 2, 'test', '{"cn.ruleengine.core.rule.GeneralRule": {"id": 214, "code": "test", "name": "测试", "rule": {"cn.ruleengine.core.rule.Rule": {"id": null, "code": null, "name": null, "actionValue": {"cn.ruleengine.core.value.Constant": {"value": true, "valueType": {"cn.ruleengine.core.value.ValueType": "BOOLEAN"}}}, "description": null, "conditionSet": {"cn.ruleengine.core.condition.ConditionSet": {"conditionGroups": {"java.util.Collections$UnmodifiableRandomAccessList": [{"cn.ruleengine.core.condition.ConditionGroup": {"id": 2028, "name": "条件组", "orderNo": 1, "conditions": {"java.util.Collections$UnmodifiableRandomAccessList": [{"cn.ruleengine.core.condition.Condition": {"id": 149, "name": "测试条件", "orderNo": 0, "operator": {"cn.ruleengine.core.condition.Operator": "EQ"}, "leftValue": {"cn.ruleengine.core.value.InputParameter": {"valueType": {"cn.ruleengine.core.value.ValueType": "STRING"}, "inputParameterId": 166, "inputParameterCode": "name"}}, "rightValue": {"cn.ruleengine.core.value.Constant": {"value": "123", "valueType": {"cn.ruleengine.core.value.ValueType": "STRING"}}}}}]}}}]}}}}}, "version": "1.0", "description": null, "workspaceId": 2, "workspaceCode": "test", "defaultActionValue": null}}', 2, '1.0', 1, 'BOOLEAN', '2023-08-11 12:37:25', '2023-08-11 12:37:25', 0);
INSERT INTO rule_engine_general_rule_publish (id, general_rule_id, general_rule_code, general_rule_name, workspace_id, workspace_code, data, status, version, loading_mode, value_type, create_time, update_time, deleted) VALUES (1514, 214, 'test', '测试', 2, 'test', '{"cn.ruleengine.core.rule.GeneralRule": {"id": 214, "code": "test", "name": "测试", "rule": {"cn.ruleengine.core.rule.Rule": {"id": null, "code": null, "name": null, "actionValue": {"cn.ruleengine.core.value.Constant": {"value": true, "valueType": {"cn.ruleengine.core.value.ValueType": "BOOLEAN"}}}, "description": null, "conditionSet": {"cn.ruleengine.core.condition.ConditionSet": {"conditionGroups": {"java.util.Collections$UnmodifiableRandomAccessList": [{"cn.ruleengine.core.condition.ConditionGroup": {"id": 2028, "name": "条件组", "orderNo": 1, "conditions": {"java.util.Collections$UnmodifiableRandomAccessList": [{"cn.ruleengine.core.condition.Condition": {"id": 149, "name": "测试条件", "orderNo": 0, "operator": {"cn.ruleengine.core.condition.Operator": "EQ"}, "leftValue": {"cn.ruleengine.core.value.InputParameter": {"valueType": {"cn.ruleengine.core.value.ValueType": "STRING"}, "inputParameterId": 166, "inputParameterCode": "name"}}, "rightValue": {"cn.ruleengine.core.value.Constant": {"value": "123", "valueType": {"cn.ruleengine.core.value.ValueType": "STRING"}}}}}, {"cn.ruleengine.core.condition.Condition": {"id": 150, "name": "测试2", "orderNo": 1, "operator": {"cn.ruleengine.core.condition.Operator": "EQ"}, "leftValue": {"cn.ruleengine.core.value.Variable": {"valueType": {"cn.ruleengine.core.value.ValueType": "BOOLEAN"}, "variableId": 165}}, "rightValue": {"cn.ruleengine.core.value.Constant": {"value": true, "valueType": {"cn.ruleengine.core.value.ValueType": "BOOLEAN"}}}}}]}}}]}}}}}, "version": "2.0", "description": null, "workspaceId": 2, "workspaceCode": "test", "defaultActionValue": null}}', 1, '2.0', 1, 'BOOLEAN', '2023-08-11 12:38:36', '2023-08-11 12:38:36', 0);
create table rule_engine_group_data
(
    id          int auto_increment
        primary key,
    group_id    int       not null,
    data_id     int       not null,
    data_type   tinyint   not null,
    create_time timestamp null,
    update_time timestamp null,
    deleted     tinyint   null
)
    comment '规则用户组与数据权限';

INSERT INTO rule_engine_group_data (id, group_id, data_id, data_type, create_time, update_time, deleted) VALUES (1, 1, 1, 0, '2020-11-21 02:42:01', '2020-11-21 02:42:03', 0);
create table rule_engine_input_parameter
(
    id               int auto_increment
        primary key,
    name             varchar(50)  not null,
    code             varchar(50)  not null,
    workspace_id     int          null,
    create_user_id   int          null,
    create_user_name varchar(50)  null,
    value_type       varchar(20)  null,
    description      varchar(500) null,
    create_time      timestamp    null,
    update_time      timestamp    null,
    deleted          tinyint      null
);

create index rule_engine_input_parameter_name_code_index
    on rule_engine_input_parameter (name, code);

create index rule_engine_input_parameter_value_type_index
    on rule_engine_input_parameter (value_type);

INSERT INTO rule_engine_input_parameter (id, name, code, workspace_id, create_user_id, create_user_name, value_type, description, create_time, update_time, deleted) VALUES (166, '名称', 'name', 2, 1, 'admin', 'STRING', '', '2023-08-11 12:18:41', '2023-08-11 12:18:41', 0);
create table rule_engine_operation_record
(
    id             int auto_increment
        primary key,
    user_id        int          not null,
    username       varchar(50)  null,
    workspace_id   int          not null,
    workspace_code varchar(50)  null,
    description    varchar(500) null,
    operation_time timestamp    null,
    data_type      tinyint      null,
    data_id        int          null
);

INSERT INTO rule_engine_operation_record (id, user_id, username, workspace_id, workspace_code, description, operation_time, data_type, data_id) VALUES (1, 1, 'admin', 2, 'test', 'admin <a>创建</a> 了一个普通规则 <a>测试(test)</a>', '2023-08-11 12:29:47', 0, 214);
INSERT INTO rule_engine_operation_record (id, user_id, username, workspace_id, workspace_code, description, operation_time, data_type, data_id) VALUES (2, 1, 'admin', 2, 'test', 'admin <a>生成</a> 了一个测试版本普通规则 <a>测试(test)</a>，版本号：<a>1.0</a>', '2023-08-11 12:37:25', 0, 214);
INSERT INTO rule_engine_operation_record (id, user_id, username, workspace_id, workspace_code, description, operation_time, data_type, data_id) VALUES (3, 1, 'admin', 2, 'test', 'admin <a>发布</a> 了一个线上版本普通规则 <a>测试(test)</a>，版本号：<a>1.0</a>', '2023-08-11 12:37:45', 0, 214);
INSERT INTO rule_engine_operation_record (id, user_id, username, workspace_id, workspace_code, description, operation_time, data_type, data_id) VALUES (4, 1, 'admin', 2, 'test', 'admin <a>生成</a> 了一个测试版本普通规则 <a>测试(test)</a>，版本号：<a>2.0</a>', '2023-08-11 12:38:36', 0, 214);
create table rule_engine_rule
(
    id                int auto_increment
        primary key,
    name              varchar(50)   null,
    code              varchar(50)   null,
    description       varchar(500)  null,
    create_user_id    int           null,
    create_user_name  varchar(100)  null,
    action_value      varchar(2000) null,
    action_type       tinyint       null,
    action_value_type varchar(50)   null,
    create_time       timestamp     null,
    update_time       timestamp     null,
    deleted           tinyint       null
);

INSERT INTO rule_engine_rule (id, name, code, description, create_user_id, create_user_name, action_value, action_type, action_value_type, create_time, update_time, deleted) VALUES (646, '测试', 'test', null, 1, 'admin', 'true', 2, 'BOOLEAN', '2023-08-11 12:29:46', '2023-08-11 12:29:46', 0);
create table rule_engine_system_log
(
    id              int auto_increment comment 'id'
        primary key,
    user_id         int          null,
    username        varchar(50)  null,
    description     varchar(300) null,
    tag             varchar(50)  null,
    ip              varchar(30)  not null comment '请求ip',
    browser         varchar(50)  null comment '浏览器',
    browser_version varchar(50)  null comment '浏览器版本',
    `system`        varchar(100) not null comment '请求者系统',
    detailed        varchar(500) not null comment '请求者系统详情',
    mobile          tinyint(1)   null comment '是否为移动平台',
    ages            mediumtext   null comment '请求参数',
    request_url     varchar(300) not null comment '请求url',
    end_time        timestamp    null on update CURRENT_TIMESTAMP comment '请求结束时间',
    running_time    bigint(10)   null comment '运行时间',
    return_value    mediumtext   null,
    exception       text         null comment '异常',
    request_id      varchar(200) null,
    create_time     timestamp    null on update CURRENT_TIMESTAMP,
    update_time     timestamp    null on update CURRENT_TIMESTAMP,
    deleted         tinyint(1)   null
);

create index rule_engine_system_log_ip_index
    on rule_engine_system_log (ip);

create index rule_engine_system_log_request_id_index
    on rule_engine_system_log (request_id);

create index rule_engine_system_log_user_id_index
    on rule_engine_system_log (user_id);


create table rule_engine_user
(
    id          int auto_increment
        primary key,
    username    varchar(30)             not null comment '用户名',
    password    varchar(50)             not null comment '密码',
    email       varchar(50)             null comment '邮箱',
    phone       bigint(16)              null comment '手机号',
    avatar      varchar(200)            null comment '头像',
    sex         varchar(2)              null comment '性别',
    is_admin    tinyint                 null comment '是否为超级管理',
    description varchar(500) default '' null comment '个人描述',
    create_time timestamp               null,
    update_time timestamp               null,
    deleted     tinyint                 null
)
    comment '规则引擎用户表';

create index rule_engine_user_email_index
    on rule_engine_user (email);

create index rule_engine_user_username_index
    on rule_engine_user (username);

INSERT INTO rule_engine_user (id, username, password, email, phone, avatar, sex, is_admin, description, create_time, update_time, deleted) VALUES (1, 'admin', '5f329d3ac22671f7b214c461e58c27f3', 'admin5@qq.com', null, 'http://oss-boot-test.oss-cn-beijing.aliyuncs.com/ruleengine/.jpg?Expires=33162452746&OSSAccessKeyId=LTAIyEa5SulNXbQa&Signature=bW7G1yt1t%2BjeP3xRIALJbHY8m5U%3D', '男', 0, '7417171471', '2021-06-23 19:09:59', '2021-12-22 04:38:31', 0);
create table rule_engine_user_workspace
(
    id                int auto_increment
        primary key,
    user_id           int(30)   not null,
    workspace_id      int       null,
    is_administration tinyint   null comment '1是空间管理员 2是普通用户',
    create_time       timestamp null,
    update_time       timestamp null,
    deleted           tinyint   null
)
    comment '工作空间成员表';

create index rule_engine_user_workspace_user_id_index
    on rule_engine_user_workspace (user_id);

create index rule_engine_user_workspace_user_id_workspace_id_index
    on rule_engine_user_workspace (user_id, workspace_id);


create table rule_engine_variable
(
    id               int auto_increment
        primary key,
    name             varchar(50)   not null,
    description      varchar(500)  null,
    value_type       varchar(20)   null,
    workspace_id     int           null,
    create_user_id   int           null,
    create_user_name varchar(50)   null,
    type             tinyint       null,
    value            varchar(2000) null,
    create_time      timestamp     null,
    update_time      timestamp     null,
    deleted          tinyint       null
);

create index rule_engine_variable_name_index
    on rule_engine_variable (name);

create index rule_engine_variable_value_type_index
    on rule_engine_variable (value_type);

INSERT INTO rule_engine_variable (id, name, description, value_type, workspace_id, create_user_id, create_user_name, type, value, create_time, update_time, deleted) VALUES (165, '测试变量', null, 'BOOLEAN', 2, 1, 'admin', 2, 'true', '2023-08-11 12:38:10', '2023-08-11 12:38:10', 0);
create table rule_engine_workspace
(
    id                int auto_increment
        primary key,
    code              varchar(20)  null,
    name              varchar(30)  not null,
    access_key_id     varchar(100) null,
    access_key_secret varchar(100) null,
    description       varchar(500) null,
    create_time       timestamp    null,
    update_time       timestamp    null,
    deleted           tinyint      null
)
    comment '工作空间';

create index rule_engine_workspace_code_index
    on rule_engine_workspace (code);

create index rule_engine_workspace_name_index
    on rule_engine_workspace (name);

INSERT INTO rule_engine_workspace (id, code, name, access_key_id, access_key_secret, description, create_time, update_time, deleted) VALUES (1, 'default', '默认工作空间', 'root', '123456', '默认的', '2020-11-21 02:41:33', '2020-11-21 02:41:34', 0);
INSERT INTO rule_engine_workspace (id, code, name, access_key_id, access_key_secret, description, create_time, update_time, deleted) VALUES (2, 'test', '测试', 'gdfhdgfh', 'sdfasdfas', '供测试使用', '2020-11-21 19:36:12', '2020-11-21 19:36:13', 0);
INSERT INTO rule_engine_workspace (id, code, name, access_key_id, access_key_secret, description, create_time, update_time, deleted) VALUES (4, 'prd', '线上', 'asdfasdf', 'asasdfasdfas', '请勿随意修改', '2020-11-07 21:49:36', '2020-11-07 21:49:38', 0);



CREATE TABLE `rule_engine_data_permission` (
                                               `id` int(11) NOT NULL AUTO_INCREMENT,
                                               `user_id` int(11) DEFAULT NULL,
                                               `data_type` int(11) NOT NULL COMMENT '0：规则  1：规则集  2：决策表',
                                               `data_id` int(11) NOT NULL COMMENT '如果data_type=0 则此data_id为规则的id',
                                               `read_authority` tinyint(4) DEFAULT NULL COMMENT '0有读权限',
                                               `write_authority` tinyint(4) DEFAULT NULL COMMENT '0有写权限',
                                               `publish_authority` tinyint(4) DEFAULT NULL COMMENT '0有发布规则权限',
                                               `create_user_id` int(11) DEFAULT NULL COMMENT '谁添加的这个权限，可以是这个规则的创建人，也可以是管理',
                                               `create_time` timestamp NULL DEFAULT NULL,
                                               `update_time` timestamp NULL DEFAULT NULL,
                                               `deleted` tinyint(4) DEFAULT NULL,
                                               PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='用户数据权限表';
