-- =================================================================
-- 用户管理相关表 (User Management)
-- =================================================================
use leon0;
-- 1. 企业信息表 (sys_company)
DROP TABLE IF EXISTS `sys_company`;
CREATE TABLE `sys_company` (
                               `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '企业ID',
                               `company_name` VARCHAR(100) NOT NULL COMMENT '企业名称',
                               `contact_person` VARCHAR(50) DEFAULT NULL COMMENT '企业联系人',
                               `contact_phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
                               `contact_email` VARCHAR(100) DEFAULT NULL COMMENT '联系邮箱',
                               `status` TINYINT NOT NULL DEFAULT '1' COMMENT '企业状态 (0:禁用, 1:正常)',
                               `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                               PRIMARY KEY (`id`),
                               UNIQUE KEY `uk_company_name` (`company_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='企业信息表';

-- 测试数据 for sys_company
INSERT INTO `sys_company` (`id`, `company_name`, `contact_person`, `contact_phone`, `contact_email`, `status`) VALUES
                                                                                                                   (1, '数智未来科技有限公司', '张三', '13800138000', 'zhangsan@futuretech.com', 1),
                                                                                                                   (2, '绿色能源集团', '李四', '13900139000', 'lisi@greenenergy.com', 1),
                                                                                                                   (3, '新风医疗股份有限公司', '王五', '13700137000', 'wangwu@newhealth.com', 0);


-- 2. 用户表 (sys_user)
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
                            `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
                            `username` VARCHAR(50) NOT NULL COMMENT '用户名/账号',
                            `password` VARCHAR(255) NOT NULL COMMENT '密码（哈希存储）',
                            `nickname` VARCHAR(50) DEFAULT NULL COMMENT '用户昵称',
                            `phone_number` VARCHAR(20) DEFAULT NULL COMMENT '手机号码',
                            `email` VARCHAR(100) DEFAULT NULL COMMENT '电子邮箱',
                            `gender` TINYINT DEFAULT '0' COMMENT '性别 (0:未知, 1:男, 2:女)',
                            `user_type` TINYINT NOT NULL DEFAULT '1' COMMENT '平台用户类型 (1:企业用户, 2:平台超级管理员)',
                            `company_id` BIGINT DEFAULT NULL COMMENT '所属企业ID',
                            `company_role` TINYINT NOT NULL DEFAULT '1' COMMENT '企业内部角色 (1:普通员工, 2:企业管理员)',
                            `status` TINYINT NOT NULL DEFAULT '1' COMMENT '用户状态 (0:禁用, 1:正常)',
                            `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `uk_username` (`username`),
                            KEY `idx_company_id` (`company_id`),
                            CONSTRAINT `fk_user_company` FOREIGN KEY (`company_id`) REFERENCES `sys_company` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 测试数据 for sys_user (密码通常是哈希值，这里用 'password' 占位)
INSERT INTO `sys_user` (`id`, `username`, `password`, `nickname`, `phone_number`, `email`, `gender`, `user_type`, `company_id`, `company_role`, `status`) VALUES
                                                                                                                                                              (1, 'admin', '$2a$10$fL3n3v9v5b.npL/E/e4BGe.xRz.w6A7D9E0b6A5A4A3A2A1A0A', '平台超级管理员', '18888888888', 'admin@platform.com', 1, 2, NULL, 1, 1),
                                                                                                                                                              (2, 'zhangsan', '$2a$10$fL3n3v9v5b.npL/E/e4BGe.xRz.w6A7D9E0b6A5A4A3A2A1A0A', '张三', '13800138000', 'zhangsan@futuretech.com', 1, 1, 1, 2, 1),
                                                                                                                                                              (3, 'lisi', '$2a$10$fL3n3v9v5b.npL/E/e4BGe.xRz.w6A7D9E0b6A5A4A3A2A1A0A', '李四', '13900139000', 'lisi@greenenergy.com', 1, 1, 2, 1, 1),
                                                                                                                                                              (4, 'wangwu', '$2a$10$fL3n3v9v5b.npL/E/e4BGe.xRz.w6A7D9E0b6A5A4A3A2A1A0A', '王五', '13700137000', 'wangwu@newhealth.com', 2, 1, 3, 1, 0);


-- =================================================================
-- 行业动态管理相关表 (Industry Dynamics Management)
-- =================================================================

-- 3. 行业动态/资讯表 (biz_news)
DROP TABLE IF EXISTS `biz_news`;
CREATE TABLE `biz_news` (
                            `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '动态ID',
                            `title` VARCHAR(255) NOT NULL COMMENT '动态标题',
                            `cover_image_url` VARCHAR(255) DEFAULT NULL COMMENT '新闻图片URL',
                            `summary` VARCHAR(500) DEFAULT NULL COMMENT '新闻简介',
                            `content` TEXT COMMENT '动态内容（富文本）',
                            `author_id` BIGINT NOT NULL COMMENT '作者ID (发布者)',
                            `author_name` VARCHAR(100) DEFAULT NULL COMMENT '作者名称',
                            `company_id` BIGINT DEFAULT NULL COMMENT '发布企业ID',
                            `status` TINYINT NOT NULL DEFAULT '0' COMMENT '审核状态 (0:待审核, 1:已发布, 2:审核未通过)',
                            `view_count` INT NOT NULL DEFAULT '0' COMMENT '浏览次数',
                            `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                            PRIMARY KEY (`id`),
                            KEY `idx_author_id` (`author_id`),
                            KEY `idx_company_id_news` (`company_id`),
                            CONSTRAINT `fk_news_author` FOREIGN KEY (`author_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE,
                            CONSTRAINT `fk_news_company` FOREIGN KEY (`company_id`) REFERENCES `sys_company` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='行业动态/资讯表';

-- 测试数据 for biz_news
INSERT INTO `biz_news` (`id`, `title`, `summary`, `content`, `author_id`, `author_name`, `company_id`, `status`, `view_count`) VALUES
                                                                                                                                   (1, 'AI技术如何赋能传统制造业转型升级', '本文探讨人工智能在制造业中的应用场景...', '<p>详细内容...</p>', 2, '张三', 1, 1, 1024),
                                                                                                                                   (2, '全球光伏产业发展趋势年度报告', '报告分析了2024年全球光伏市场的最新动态...', '<p>详细内容...</p>', 3, '李四', 2, 1, 512),
                                                                                                                                   (3, '关于新药研发的初步构想', '一项关于XXX新药的研发思路正在审核中...', '<p>内部审核内容...</p>', 4, '王五', 3, 0, 10);


-- =================================================================
-- 课程管理相关表 (Course Management)
-- =================================================================

-- 4. 课程表 (biz_course)
DROP TABLE IF EXISTS `biz_course`;
CREATE TABLE `biz_course` (
                              `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '课程ID',
                              `course_name` VARCHAR(255) NOT NULL COMMENT '课程名称',
                              `cover_image_url` VARCHAR(255) DEFAULT NULL COMMENT '课程封面URL',
                              `summary` VARCHAR(500) DEFAULT NULL COMMENT '课程简介',
                              `course_video_url` VARCHAR(255) DEFAULT NULL COMMENT '课程视频URL',
                              `sort_order` INT NOT NULL DEFAULT '0' COMMENT '课程排序值',
                              `author_id` BIGINT NOT NULL COMMENT '作者ID (发布者)',
                              `author_name` VARCHAR(100) DEFAULT NULL COMMENT '作者名称',
                              `company_id` BIGINT DEFAULT NULL COMMENT '发布企业ID',
                              `status` TINYINT NOT NULL DEFAULT '0' COMMENT '审核状态 (0:待审核, 1:已发布, 2:审核未通过)',
                              `view_count` INT NOT NULL DEFAULT '0' COMMENT '观看次数',
                              `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                              PRIMARY KEY (`id`),
                              KEY `idx_author_id_course` (`author_id`),
                              CONSTRAINT `fk_course_author` FOREIGN KEY (`author_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='课程表';

-- 测试数据 for biz_course
INSERT INTO `biz_course` (`id`, `course_name`, `summary`, `sort_order`, `author_id`, `author_name`, `company_id`, `status`, `view_count`) VALUES
                                                                                                                                              (1, '企业数字化管理入门', '本课程介绍企业如何利用数字化工具提升管理效率。', 1, 2, '张三', 1, 1, 800),
                                                                                                                                              (2, '新能源技术基础', '了解太阳能、风能等新能源的基本原理和应用。', 2, 3, '李四', 2, 1, 1200),
                                                                                                                                              (3, '平台使用指南', '超级管理员录制的平台功能介绍视频', 99, 1, '平台超级管理员', NULL, 1, 999);


-- =================================================================
-- 会议管理相关表 (Meeting Management)
-- =================================================================

-- 5. 会议表 (biz_meeting)
DROP TABLE IF EXISTS `biz_meeting`;
CREATE TABLE `biz_meeting` (
                               `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '会议ID',
                               `meeting_name` VARCHAR(255) NOT NULL COMMENT '会议名称',
                               `start_time` DATETIME NOT NULL COMMENT '会议开始时间',
                               `end_time` DATETIME NOT NULL COMMENT '会议结束时间',
                               `cover_image_url` VARCHAR(255) DEFAULT NULL COMMENT '会议封面URL',
                               `content` TEXT COMMENT '会议内容（富文本）',
                               `location` VARCHAR(255) DEFAULT NULL COMMENT '会议地点',
                               `organizer` VARCHAR(255) DEFAULT NULL COMMENT '主办单位',
                               `agenda` TEXT COMMENT '会议议程',
                               `speakers` TEXT COMMENT '嘉宾介绍',
                               `creator_id` BIGINT NOT NULL COMMENT '创建人ID',
                               `creator_name` VARCHAR(100) DEFAULT NULL COMMENT '创建人姓名',
                               `company_id` BIGINT DEFAULT NULL COMMENT '创建企业ID',
                               `status` TINYINT NOT NULL DEFAULT '0' COMMENT '审核状态 (0:待审核, 1:已发布, 2:审核未通过)',
                               `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                               PRIMARY KEY (`id`),
                               CONSTRAINT `fk_meeting_creator` FOREIGN KEY (`creator_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='会议表';

-- 测试数据 for biz_meeting
INSERT INTO `biz_meeting` (`id`, `meeting_name`, `start_time`, `end_time`, `location`, `organizer`, `creator_id`, `creator_name`, `company_id`, `status`) VALUES
                                                                                                                                                              (1, '2025全球人工智能开发者大会', '2025-09-01 09:00:00', '2025-09-03 17:00:00', '北京国家会议中心', '数智未来科技有限公司', 2, '张三', 1, 1),
                                                                                                                                                              (2, '可持续发展与能源创新论坛', '2025-10-15 14:00:00', '2025-10-15 18:00:00', '上海国际会议中心', '绿色能源集团', 3, '李四', 2, 1);


-- 6. 会议注册回执表 (biz_meeting_registration)
DROP TABLE IF EXISTS `biz_meeting_registration`;
CREATE TABLE `biz_meeting_registration` (
                                            `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '注册ID',
                                            `meeting_id` BIGINT NOT NULL COMMENT '会议ID',
                                            `user_id` BIGINT NOT NULL COMMENT '注册用户ID',
                                            `company` VARCHAR(100) NOT NULL COMMENT '单位',
                                            `name` VARCHAR(50) NOT NULL COMMENT '姓名',
                                            `gender` TINYINT NOT NULL DEFAULT '0' COMMENT '性别 (0:未知, 1:男, 2:女)',
                                            `phone_number` VARCHAR(20) NOT NULL COMMENT '手机号码',
                                            `email` VARCHAR(100) DEFAULT NULL COMMENT '电子邮箱',
                                            `arrival_method` VARCHAR(50) DEFAULT NULL COMMENT '到达方式',
                                            `arrival_train_no` VARCHAR(50) DEFAULT NULL COMMENT '到达车次/航班号',
                                            `arrival_time` DATETIME DEFAULT NULL COMMENT '到达时间',
                                            `registered_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
                                            PRIMARY KEY (`id`),
                                            UNIQUE KEY `uk_meeting_user` (`meeting_id`,`user_id`),
                                            CONSTRAINT `fk_reg_meeting` FOREIGN KEY (`meeting_id`) REFERENCES `biz_meeting` (`id`) ON DELETE CASCADE,
                                            CONSTRAINT `fk_reg_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='会议注册回执表';

-- 测试数据 for biz_meeting_registration
INSERT INTO `biz_meeting_registration` (`meeting_id`, `user_id`, `company`, `name`, `gender`, `phone_number`, `email`) VALUES
                                                                                                                           (1, 3, '绿色能源集团', '李四', 1, '13900139000', 'lisi@greenenergy.com'),
                                                                                                                           (2, 2, '数智未来科技有限公司', '张三', 1, '13800138000', 'zhangsan@futuretech.com');


-- =================================================================
-- 合作模块相关表 (Collaboration Module)
-- =================================================================

-- 7. 合作模块通用表 (biz_collaboration)
DROP TABLE IF EXISTS `biz_collaboration`;
CREATE TABLE `biz_collaboration` (
                                     `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '合作ID',
                                     `category` TINYINT NOT NULL COMMENT '合作类型 (1:会议研讨, 2:标准定制, 3:技术培训, 4:工具研发, 5:公益行动)',
                                     `title` VARCHAR(255) NOT NULL COMMENT '模块名称/标题',
                                     `summary` VARCHAR(500) DEFAULT NULL COMMENT '模块简介',
                                     `content` TEXT COMMENT '详细内容（富文本）',
                                     `external_link` VARCHAR(255) DEFAULT NULL COMMENT '外部链接',
                                     `creator_id` BIGINT DEFAULT NULL COMMENT '创建人ID',
                                     `company_id` BIGINT DEFAULT NULL COMMENT '创建企业ID',
                                     `status` TINYINT NOT NULL DEFAULT '1' COMMENT '状态 (0:禁用, 1:正常)',
                                     `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                     `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                     PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='合作模块通用表';

-- 测试数据 for biz_collaboration
INSERT INTO `biz_collaboration` (`category`, `title`, `summary`, `creator_id`, `company_id`) VALUES
                                                                                                 (2, 'AI伦理安全标准定制', '联合行业伙伴，共同探讨并制定人工智能领域的伦理安全标准。', 1, NULL),
                                                                                                 (3, '数字化转型系列技术培训', '为合作企业提供一系列关于数字化转型的在线和线下培训课程。', 2, 1),
                                                                                                 (5, '乡村儿童编程启蒙公益行动', '一个旨在为乡村儿童提供免费编程教育的公益项目。', 1, NULL);


-- =================================================================
-- 数据分析相关表 (Data Analysis)
-- =================================================================

-- 8. 用户活动日志表 (sys_user_activity_log)
DROP TABLE IF EXISTS `sys_user_activity_log`;
CREATE TABLE `sys_user_activity_log` (
                                         `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '日志ID',
                                         `user_id` BIGINT DEFAULT NULL COMMENT '用户ID (可空)',
                                         `action_type` VARCHAR(50) NOT NULL COMMENT '操作类型',
                                         `target_type` VARCHAR(50) DEFAULT NULL COMMENT '目标类型',
                                         `target_id` BIGINT DEFAULT NULL COMMENT '目标ID',
                                         `ip_address` VARCHAR(50) DEFAULT NULL COMMENT 'IP地址',
                                         `device_info` VARCHAR(255) DEFAULT NULL COMMENT '设备信息',
                                         `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
                                         PRIMARY KEY (`id`),
                                         KEY `idx_user_action` (`user_id`,`action_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户活动日志表';

-- 测试数据 for sys_user_activity_log
INSERT INTO `sys_user_activity_log` (`user_id`, `action_type`, `target_type`, `target_id`, `ip_address`) VALUES
                                                                                                             (2, 'login', 'user', 2, '192.168.1.10'),
                                                                                                             (3, 'view_news', 'news', 1, '123.45.67.89'),
                                                                                                             (2, 'view_course', 'course', 2, '192.168.1.10'),
                                                                                                             (3, 'register_meeting', 'meeting', 1, '123.45.67.89');