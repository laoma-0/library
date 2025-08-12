/*
 Navicat MySQL Dump SQL

 Source Server         : MySQL
 Source Server Type    : MySQL
 Source Server Version : 90001 (9.0.1)
 Source Host           : localhost:3306
 Source Schema         : library_management

 Target Server Type    : MySQL
 Target Server Version : 90001 (9.0.1)
 File Encoding         : 65001

 Date: 09/06/2025 18:34:58
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for books
-- ----------------------------
DROP TABLE IF EXISTS `books`;
CREATE TABLE `books`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '书名',
  `author` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '作者',
  `isbn` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'ISBN',
  `category_id` bigint NOT NULL COMMENT '分类ID',
  `publisher` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '出版社',
  `publish_date` date NULL DEFAULT NULL COMMENT '出版日期',
  `price` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '价格',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '图书简介',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态：0-已借出，1-可借阅',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `isbn`(`isbn` ASC) USING BTREE,
  INDEX `category_id`(`category_id` ASC) USING BTREE,
  CONSTRAINT `books_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 55 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of books
-- ----------------------------
INSERT INTO `books` VALUES (2, 'Python数据分析实战', 'Sebastian Raschka', '9787111583080', 1, '机械工业出版社', '2018-03-01', 90.00, '从实战角度讲解Python数据分析的方法和技巧。', 0, '2025-05-28 12:33:20', '2025-06-07 01:30:16');
INSERT INTO `books` VALUES (3, '数据结构与算法分析', 'Mark Allen Weiss', '9787111610478', 1, '机械工业出版社', '2018-10-01', 89.00, '全面介绍数据结构和算法分析的经典教材。', 1, '2025-05-28 12:33:20', '2025-06-06 07:12:52');
INSERT INTO `books` VALUES (4, '深入理解计算机系统', 'Randal E. Bryant', '9787111544746', 1, '机械工业出版社', '2016-05-01', 129.00, '从程序员的视角详细阐述计算机系统的本质概念。', 0, '2025-05-28 12:33:20', '2025-06-06 07:31:42');
INSERT INTO `books` VALUES (5, '计算机网络', 'Andrew S. Tanenbaum', '9787111611215', 1, '机械工业出版社', '2018-10-01', 99.00, '经典的计算机网络教材，全面系统地介绍计算机网络的原理和技术。', 0, '2025-05-28 12:33:20', '2025-06-06 07:23:19');
INSERT INTO `books` VALUES (6, '红楼梦', '曹雪芹', '9787020002472', 2, '人民文学出版社', '1996-12-01', 59.00, '中国古典文学四大名著之一，以贾、史、王、薛四大家族的兴衰为背景。', 1, '2025-05-28 12:33:20', '2025-05-28 12:33:20');
INSERT INTO `books` VALUES (7, '百年孤独', '加西亚·马尔克斯', '9787020049521', 2, '人民文学出版社', '2008-06-01', 39.00, '魔幻现实主义文学的代表作，描写了布恩迪亚家族七代人的故事。', 0, '2025-05-28 12:33:20', '2025-06-06 06:28:40');
INSERT INTO `books` VALUES (8, '活着', '余华', '9787530208288', 2, '北京十月文艺出版社', '2012-08-01', 20.00, '讲述了在大时代背景下，主人公福贵的人生和家庭不断经受着苦难。', 1, '2025-05-28 12:33:20', '2025-06-06 07:20:51');
INSERT INTO `books` VALUES (9, '平凡的世界', '路遥', '9787020081101', 2, '人民文学出版社', '2009-03-01', 86.00, '以中国70年代中期到80年代中期10年间为背景，通过复杂的矛盾纠葛。', 1, '2025-05-28 12:33:20', '2025-05-28 12:33:20');
INSERT INTO `books` VALUES (10, '三体', '刘慈欣', '9787536479041', 2, '四川科学技术出版社', '2013-05-01', 38.00, '中国科幻文学的里程碑之作，讲述了地球人类文明和三体文明的信息交流。', 0, '2025-05-28 12:33:20', '2025-06-06 06:51:29');
INSERT INTO `books` VALUES (11, '社会心理学', '戴维·迈尔斯', '9787111572213', 3, '机械工业出版社', '2017-08-01', 129.00, '全球畅销的社会心理学教材，系统介绍了社会心理学的基本概念、理论和研究方法。', 1, '2025-05-28 12:33:20', '2025-05-28 12:33:20');
INSERT INTO `books` VALUES (12, '乌合之众：大众心理研究', '古斯塔夫·勒庞', '9787544715827', 3, '译林出版社', '2010-12-01', 22.00, '解析群体心理的经典名著，深入探讨了群体心理的特点和影响。', 1, '2025-05-28 12:33:20', '2025-06-06 06:48:13');
INSERT INTO `books` VALUES (13, '梦的解析', '西格蒙德·弗洛伊德', '9787544715834', 3, '译林出版社', '2010-12-01', 22.00, '弗洛伊德的代表作，也是精神分析的奠基作。', 1, '2025-05-28 12:33:20', '2025-05-29 17:28:49');
INSERT INTO `books` VALUES (14, '思考，快与慢', '丹尼尔·卡尼曼', '9787508625709', 3, '中信出版社', '2012-07-01', 69.00, '诺贝尔奖得主丹尼尔·卡尼曼的代表作，讲述了对大脑思考速度的看法。', 1, '2025-05-28 12:33:20', '2025-05-28 12:33:20');
INSERT INTO `books` VALUES (15, '资本论', '卡尔·马克思', '9787010087730', 3, '人民出版社', '2009-12-01', 82.00, '马克思主义的重要经典著作之一，以剩余价值为中心。', 1, '2025-05-28 12:33:20', '2025-05-28 12:33:20');
INSERT INTO `books` VALUES (16, '时间简史', '史蒂芬·霍金', '9787535745546', 4, '湖南科学技术出版社', '2002-08-01', 38.00, '全球科学著作的里程碑，介绍了宇宙起源和人类命运。', 1, '2025-05-28 12:33:20', '2025-06-05 14:45:11');
INSERT INTO `books` VALUES (17, '物种起源', '查尔斯·达尔文', '9787100047268', 4, '商务印书馆', '2005-10-01', 42.00, '达尔文论述生物进化的重要著作，提出了生物进化论学说。', 1, '2025-05-28 12:33:20', '2025-05-28 12:33:20');
INSERT INTO `books` VALUES (18, '自然哲学的数学原理', '艾萨克·牛顿', '9787535753688', 4, '湖南科学技术出版社', '2007-04-01', 68.00, '经典力学的第一部经典著作，也是人类掌握的第一个完整的科学的宇宙论和科学理论体系。', 1, '2025-05-28 12:33:20', '2025-05-28 12:33:20');
INSERT INTO `books` VALUES (19, '相对论', '阿尔伯特·爱因斯坦', '9787542843369', 4, '上海科技教育出版社', '2007-05-01', 25.00, '爱因斯坦的相对论是现代物理学的重要基石。', 0, '2025-05-28 12:33:20', '2025-05-28 12:33:20');
INSERT INTO `books` VALUES (20, '化学原理', '彼得·阿特金斯', '9787030162951', 4, '科学出版社', '2006-01-01', 118.00, '全面介绍化学基本原理的教材，内容涵盖无机化学、有机化学和物理化学。', 1, '2025-05-28 12:33:20', '2025-05-28 12:33:20');
INSERT INTO `books` VALUES (21, '机械设计手册', '成大先', '9787111213395', 5, '机械工业出版社', '2008-01-01', 499.00, '机械设计领域的权威工具书，涵盖机械设计的各个方面。', 1, '2025-05-28 12:33:20', '2025-06-05 13:43:54');
INSERT INTO `books` VALUES (22, '电子电路基础', '董永贵', '9787111369371', 5, '机械工业出版社', '2012-01-01', 49.00, '介绍电子电路的基本原理和分析方法，包括模拟电路和数字电路。', 1, '2025-05-28 12:33:20', '2025-06-05 14:41:21');
INSERT INTO `books` VALUES (23, '建筑设计原理', '张文忠', '9787112080773', 5, '中国建筑工业出版社', '2006-05-01', 46.00, '系统阐述建筑设计的基本原理和方法，结合大量实例进行分析。', 1, '2025-05-28 12:33:20', '2025-05-28 12:33:20');
INSERT INTO `books` VALUES (24, '土木工程概论', '叶志明', '9787111238374', 5, '机械工业出版社', '2008-07-01', 35.00, '全面介绍土木工程的各个领域，包括建筑工程、道路桥梁工程等。', 1, '2025-05-28 12:33:20', '2025-05-28 12:33:20');
INSERT INTO `books` VALUES (25, '经济学原理', 'N·格里高利·曼昆', '9787301139992', 6, '北京大学出版社', '2009-04-01', 78.00, '经济学领域的经典教材，分为微观经济学和宏观经济学两部分。', 1, '2025-05-28 12:33:20', '2025-06-05 14:41:30');
INSERT INTO `books` VALUES (26, '管理学原理', '斯蒂芬·P·罗宾斯', '9787300128064', 6, '中国人民大学出版社', '2010-12-01', 69.00, '全球经典的管理学教材，系统介绍了管理的基本职能和方法。', 1, '2025-05-28 12:33:20', '2025-05-28 12:33:20');
INSERT INTO `books` VALUES (27, '市场营销原理', '菲利普·科特勒', '9787111421327', 6, '机械工业出版社', '2013-01-01', 99.00, '市场营销领域的权威教材，涵盖市场营销的理论、策略和实践。', 1, '2025-05-28 12:33:20', '2025-05-28 12:33:20');
INSERT INTO `books` VALUES (28, '财务管理', '荆新', '9787300162907', 6, '中国人民大学出版社', '2012-08-01', 49.00, '系统介绍财务管理的基本理论、方法和技能。', 1, '2025-05-28 12:33:20', '2025-06-05 14:41:35');
INSERT INTO `books` VALUES (29, '人力资源管理', '加里·德斯勒', '9787300162914', 6, '中国人民大学出版社', '2012-08-01', 78.00, '全面介绍人力资源管理的各个方面，包括招聘、培训、绩效管理等。', 1, '2025-05-28 12:33:20', '2025-05-28 12:33:20');
INSERT INTO `books` VALUES (30, '史记', '司马迁', '9787101009967', 7, '中华书局', '2006-06-01', 128.00, '中国第一部纪传体通史，记载了从上古传说到汉武帝时期的历史。', 1, '2025-05-28 12:33:20', '2025-05-28 12:33:20');
INSERT INTO `books` VALUES (31, '资治通鉴', '司马光', '9787101059573', 7, '中华书局', '2007-03-01', 298.00, '编年体通史巨著，记载了公元前403年到公元959年共16朝1362年的历史。', 1, '2025-05-28 12:33:20', '2025-06-05 14:41:39');
INSERT INTO `books` VALUES (32, '中国通史', '吕思勉', '9787101071537', 7, '中华书局', '2010-01-01', 49.00, '中国通史经典著作，分为上下两册，上册为中国文化史，下册为中国政治史。', 1, '2025-05-28 12:33:20', '2025-05-28 12:33:20');
INSERT INTO `books` VALUES (33, '全球通史', '斯塔夫里阿诺斯', '9787532735073', 7, '上海译文出版社', '2005-05-01', 88.00, '从全球的角度而不是某一国家或地区的角度来考察世界各地区人类文明的产生和发展。', 1, '2025-05-28 12:33:20', '2025-05-28 12:33:20');
INSERT INTO `books` VALUES (34, '地理学与生活', '阿瑟·格蒂斯', '9787111384480', 7, '机械工业出版社', '2012-06-01', 89.00, '一本地理学入门教材，全面介绍了地理学的基本概念、理论和方法。', 1, '2025-05-28 12:33:20', '2025-06-05 14:41:44');
INSERT INTO `books` VALUES (35, 'Java编程思想', 'Bruce Eckel', '9787111213821', 1, '机械工业出版社', '2007-06-01', 108.00, 'Java领域的经典著作，全面系统地讲解Java语言的编程思想和方法。', 1, '2025-05-28 12:33:20', '2025-05-28 12:33:20');
INSERT INTO `books` VALUES (36, 'Effective Java', 'Joshua Bloch', '9787111590804', 1, '机械工业出版社', '2018-01-01', 79.00, 'Java程序员必备书籍，介绍了在Java编程中应该遵循的最佳实践和设计模式。', 1, '2025-05-28 12:33:20', '2025-05-28 12:33:20');
INSERT INTO `books` VALUES (37, 'JavaScript高级程序设计', 'Matt Frisbie', '9787111604218', 1, '机械工业出版社', '2018-08-01', 119.00, '全面介绍JavaScript语言和Web浏览器编程的经典教材。', 0, '2025-05-28 12:33:20', '2025-06-05 14:41:46');
INSERT INTO `books` VALUES (38, 'CSS权威指南', 'Eric A. Meyer', '9787115377347', 1, '人民邮电出版社', '2015-01-01', 129.00, 'CSS领域的权威著作，详细讲解了CSS的各个方面。', 1, '2025-05-28 12:33:20', '2025-05-28 12:33:20');
INSERT INTO `books` VALUES (39, 'HTML5与CSS3权威指南', '谢成鸿', '9787121248057', 1, '电子工业出版社', '2014-11-01', 118.00, '全面介绍HTML5和CSS3的新特性和应用方法。', 1, '2025-05-28 12:33:20', '2025-05-28 12:33:20');
INSERT INTO `books` VALUES (40, '数据结构与算法（Python语言描述）', 'Michael T. Goodrich', '9787111573364', 1, '机械工业出版社', '2017-08-01', 89.00, '使用Python语言描述数据结构和算法的教材，适合Python开发者学习。', 0, '2025-05-28 12:33:20', '2025-05-28 12:33:20');
INSERT INTO `books` VALUES (41, '算法导论', 'Thomas H. Cormen', '9787111187776', 1, '机械工业出版社', '2006-09-01', 85.00, '算法领域的经典教材，全面介绍了算法的设计、分析和实现。', 1, '2025-05-28 12:33:20', '2025-05-28 12:33:20');
INSERT INTO `books` VALUES (42, '计算机组成原理', '唐朔飞', '9787040251692', 1, '高等教育出版社', '2008-12-01', 45.00, '计算机专业的核心课程教材，系统介绍了计算机的组成原理和体系结构。', 1, '2025-05-28 12:33:20', '2025-05-28 12:33:20');
INSERT INTO `books` VALUES (43, '操作系统概念', 'Abraham Silberschatz', '9787111505280', 1, '机械工业出版社', '2015-06-01', 99.00, '操作系统领域的经典教材，全面介绍了操作系统的基本概念、原理和设计。', 1, '2025-05-28 12:33:20', '2025-06-05 14:41:52');
INSERT INTO `books` VALUES (44, '编译原理', 'Alfred V. Aho', '9787111228528', 1, '机械工业出版社', '2009-04-01', 79.00, '编译原理领域的经典教材，系统介绍了编译程序的构造原理和方法。', 1, '2025-05-28 12:33:20', '2025-05-28 12:33:20');
INSERT INTO `books` VALUES (45, '设计模式：可复用面向对象软件的基础', 'Erich Gamma', '9787111213319', 1, '机械工业出版社', '2007-06-01', 59.00, '设计模式领域的经典著作，介绍了23种经典的设计模式。', 1, '2025-05-28 12:33:20', '2025-05-28 12:33:20');
INSERT INTO `books` VALUES (46, '重构：改善既有代码的设计', 'Martin Fowler', '9787111217270', 1, '机械工业出版社', '2007-08-01', 59.00, '讲述如何识别和实施重构，如何重构以及何时重构的经典著作。', 1, '2025-05-28 12:33:20', '2025-06-05 14:41:58');

-- ----------------------------
-- Table structure for borrow_records
-- ----------------------------
DROP TABLE IF EXISTS `borrow_records`;
CREATE TABLE `borrow_records`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '借阅用户ID',
  `book_id` bigint NOT NULL COMMENT '图书ID',
  `borrow_date` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '借阅日期',
  `return_date` datetime NULL DEFAULT NULL COMMENT '归还日期',
  `status` tinyint NULL DEFAULT 0 COMMENT '状态：0-未归还，1-已归还',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  INDEX `book_id`(`book_id` ASC) USING BTREE,
  CONSTRAINT `borrow_records_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `borrow_records_ibfk_2` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1012 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of borrow_records
-- ----------------------------
INSERT INTO `borrow_records` VALUES (1003, 4, 3, '2023-06-07 08:00:00', '2025-06-11 08:00:00', 1, '2025-06-01 15:57:47');
INSERT INTO `borrow_records` VALUES (1009, 53, 2, '2025-06-10 08:00:00', NULL, 0, '2025-06-06 06:34:17');
INSERT INTO `borrow_records` VALUES (1010, 3, 5, '2025-06-10 08:00:00', NULL, 0, '2025-06-06 07:23:19');
INSERT INTO `borrow_records` VALUES (1011, 4, 4, '2025-06-03 08:00:00', NULL, 0, '2025-06-06 07:31:42');

-- ----------------------------
-- Table structure for categories
-- ----------------------------
DROP TABLE IF EXISTS `categories`;
CREATE TABLE `categories`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分类名称',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父分类ID',
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分类描述',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name`(`name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of categories
-- ----------------------------
INSERT INTO `categories` VALUES (1, '计算机科学', 0, '计算机技术相关书籍', '2025-05-28 12:33:11');
INSERT INTO `categories` VALUES (2, '文学艺术', 0, '文学、艺术、诗歌等', '2025-05-28 12:33:11');
INSERT INTO `categories` VALUES (3, '社会科学', 0, '社会学、心理学等', '2025-05-28 12:33:11');
INSERT INTO `categories` VALUES (4, '自然科学', 0, '物理、化学、生物等', '2025-05-28 12:33:11');
INSERT INTO `categories` VALUES (5, '工程技术', 0, '机械、电子、建筑等', '2025-05-28 12:33:11');
INSERT INTO `categories` VALUES (6, '经济管理', 0, '经济学、管理学等', '2025-05-28 12:33:11');
INSERT INTO `categories` VALUES (7, '历史地理', 0, '历史、地理、考古等', '2025-05-28 12:33:11');

-- ----------------------------
-- Table structure for system_config
-- ----------------------------
DROP TABLE IF EXISTS `system_config`;
CREATE TABLE `system_config`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `config_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '配置键',
  `config_value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '配置值',
  `config_type` tinyint NULL DEFAULT 0 COMMENT '配置类型：0-文本，1-数字，2-布尔，3-JSON',
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '配置描述',
  `is_system` tinyint NULL DEFAULT 0 COMMENT '是否系统内置：0-否，1-是',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `config_key`(`config_key` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of system_config
-- ----------------------------
INSERT INTO `system_config` VALUES (1, 'library_name', '智慧图书馆', 0, '图书馆名称', 1, '2025-05-29 22:16:19', '2025-05-29 22:16:19');
INSERT INTO `system_config` VALUES (2, 'library_address', '北京市海淀区中关村南大街5号', 0, '图书馆地址', 1, '2025-05-29 22:16:19', '2025-05-29 22:16:19');
INSERT INTO `system_config` VALUES (3, 'library_phone', '010-12345678', 0, '图书馆联系电话', 1, '2025-05-29 22:16:19', '2025-05-29 22:16:19');
INSERT INTO `system_config` VALUES (4, 'library_email', 'library@example.com', 0, '图书馆邮箱', 1, '2025-05-29 22:16:19', '2025-05-29 22:16:19');
INSERT INTO `system_config` VALUES (5, 'library_opening_hours', '周一至周五: 9:00-21:00\n周六至周日: 10:00-18:00', 0, '图书馆开放时间', 1, '2025-05-29 22:16:19', '2025-05-29 22:16:19');
INSERT INTO `system_config` VALUES (6, 'max_borrow_count', '5', 1, '用户最大借阅数量', 1, '2025-05-29 22:16:19', '2025-05-29 22:16:19');
INSERT INTO `system_config` VALUES (7, 'max_borrow_days', '30', 1, '最大借阅天数', 1, '2025-05-29 22:16:19', '2025-05-29 22:16:19');
INSERT INTO `system_config` VALUES (8, 'overdue_fine_per_day', '1.00', 1, '逾期每天罚款金额(元)', 1, '2025-05-29 22:16:19', '2025-05-29 22:16:19');
INSERT INTO `system_config` VALUES (9, 'allow_renew', 'true', 2, '是否允许续借', 1, '2025-05-29 22:16:19', '2025-05-29 22:16:19');
INSERT INTO `system_config` VALUES (10, 'max_renew_times', '1', 1, '最大续借次数', 1, '2025-05-29 22:16:19', '2025-05-29 22:16:19');
INSERT INTO `system_config` VALUES (11, 'allow_online_reservation', 'true', 2, '是否允许在线预约', 1, '2025-05-29 22:16:19', '2025-05-29 22:16:19');
INSERT INTO `system_config` VALUES (12, 'reservation_valid_days', '3', 1, '预约保留天数', 1, '2025-05-29 22:16:19', '2025-05-29 22:16:19');
INSERT INTO `system_config` VALUES (13, 'enable_email_notification', 'true', 2, '是否启用邮件通知', 1, '2025-05-29 22:16:19', '2025-05-29 22:16:19');
INSERT INTO `system_config` VALUES (14, 'enable_sms_notification', 'false', 2, '是否启用短信通知', 1, '2025-05-29 22:16:19', '2025-05-29 22:16:19');

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `role` tinyint NULL DEFAULT 2 COMMENT '角色：1-管理员，2-普通用户',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 77 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (2, 'admin', '123456', '超级管理员', '139001390www', 'user1@example.com', 1, '2025-05-28 12:33:05', '2025-05-30 01:25:18', 1);
INSERT INTO `users` VALUES (3, 'user1', '$2y$10$oPmTj3Dq2wXfJvQdNpQz0u.7aWl8sYq1lZ8vBqKcQvjO7fJzJ9W', '王富贵', '13800138000', 'admin@example.com', 1, '2025-05-30 01:24:17', '2025-06-01 16:03:07', 2);
INSERT INTO `users` VALUES (4, 'user', '1234567', '小孩', '188888888888', 'user3@example.com', 1, '2025-05-29 14:23:17', '2025-06-01 16:03:20', 2);
INSERT INTO `users` VALUES (44, 'user01', '$2y$10$oPmTj3Dq2wXfJvQdNpQz0u.7aWl8sYq1lZ8vBqKcQvjO7fJzJ9W', '雷士', '13900139001', 'user01@example.com', 1, '2025-05-30 01:24:17', '2025-05-30 01:24:17', 2);
INSERT INTO `users` VALUES (45, 'user02', '$2y$10$oPmTj3Dq2wXfJvQdNpQz0u.7aWl8sYq1lZ8vBqKcQvjO7fJzJ9W', '李四', '13900139002', 'user02@example.com', 1, '2025-05-30 01:24:17', '2025-05-30 01:24:17', 2);
INSERT INTO `users` VALUES (46, 'user03', '$2y$10$oPmTj3Dq2wXfJvQdNpQz0u.7aWl8sYq1lZ8vBqKcQvjO7fJzJ9W', '王五', '13900139003', 'user03@example.com', 1, '2025-05-30 01:24:17', '2025-05-30 01:24:17', 2);
INSERT INTO `users` VALUES (47, 'user04', '$2y$10$oPmTj3Dq2wXfJvQdNpQz0u.7aWl8sYq1lZ8vBqKcQvjO7fJzJ9W', '赵六', '13900139004', 'user04@example.com', 1, '2025-05-30 01:24:17', '2025-05-30 01:24:17', 2);
INSERT INTO `users` VALUES (48, 'user05', '$2y$10$oPmTj3Dq2wXfJvQdNpQz0u.7aWl8sYq1lZ8vBqKcQvjO7fJzJ9W', '钱七', '13900139005', 'user05@example.com', 1, '2025-05-30 01:24:17', '2025-05-30 01:24:17', 2);
INSERT INTO `users` VALUES (49, 'user06', '$2y$10$oPmTj3Dq2wXfJvQdNpQz0u.7aWl8sYq1lZ8vBqKcQvjO7fJzJ9W', '孙八', '13900139006', 'user06@example.com', 1, '2025-05-30 01:24:17', '2025-05-30 01:24:17', 2);
INSERT INTO `users` VALUES (50, 'user07', '$2y$10$oPmTj3Dq2wXfJvQdNpQz0u.7aWl8sYq1lZ8vBqKcQvjO7fJzJ9W', '周九', '13900139007', 'user07@example.com', 1, '2025-05-30 01:24:17', '2025-05-30 01:24:17', 2);
INSERT INTO `users` VALUES (51, 'user08', '$2y$10$oPmTj3Dq2wXfJvQdNpQz0u.7aWl8sYq1lZ8vBqKcQvjO7fJzJ9W', '吴十', '13900139008', 'user08@example.com', 1, '2025-05-30 01:24:17', '2025-05-30 01:24:17', 2);
INSERT INTO `users` VALUES (52, 'user09', '$2y$10$oPmTj3Dq2wXfJvQdNpQz0u.7aWl8sYq1lZ8vBqKcQvjO7fJzJ9W', '郑十一', '13900139009', 'user09@example.com', 1, '2025-05-30 01:24:17', '2025-05-30 01:24:17', 2);
INSERT INTO `users` VALUES (53, 'user10', '$2y$10$oPmTj3Dq2wXfJvQdNpQz0u.7aWl8sYq1lZ8vBqKcQvjO7fJzJ9W', '王十二', '13900139010', 'user10@example.com', 1, '2025-05-30 01:24:17', '2025-05-30 01:24:17', 2);
INSERT INTO `users` VALUES (54, 'user11', '$2y$10$oPmTj3Dq2wXfJvQdNpQz0u.7aWl8sYq1lZ8vBqKcQvjO7fJzJ9W', '陈十三', '13900139011', 'user11@example.com', 1, '2025-05-30 01:24:17', '2025-05-30 01:24:17', 2);
INSERT INTO `users` VALUES (55, 'user12', '$2y$10$oPmTj3Dq2wXfJvQdNpQz0u.7aWl8sYq1lZ8vBqKcQvjO7fJzJ9W', '杨十四', '13900139012', 'user12@example.com', 1, '2025-05-30 01:24:17', '2025-05-30 01:24:17', 2);
INSERT INTO `users` VALUES (56, 'user13', '$2y$10$oPmTj3Dq2wXfJvQdNpQz0u.7aWl8sYq1lZ8vBqKcQvjO7fJzJ9W', '黄十五', '13900139013', 'user13@example.com', 1, '2025-05-30 01:24:17', '2025-05-30 01:24:17', 2);
INSERT INTO `users` VALUES (57, 'user14', '$2y$10$oPmTj3Dq2wXfJvQdNpQz0u.7aWl8sYq1lZ8vBqKcQvjO7fJzJ9W', '周十六', '13900139014', 'user14@example.com', 1, '2025-05-30 01:24:17', '2025-05-30 01:24:17', 2);
INSERT INTO `users` VALUES (59, 'user16', '$2y$10$oPmTj3Dq2wXfJvQdNpQz0u.7aWl8sYq1lZ8vBqKcQvjO7fJzJ9W', '郑十八', '13900139016', 'user16@example.com', 0, '2025-05-30 01:24:17', '2025-06-07 09:50:53', 2);
INSERT INTO `users` VALUES (60, 'disabled_user', '$2y$10$oPmTj3Dq2wXfJvQdNpQz0u.7aWl8sYq1lZ8vBqKcQvjO7fJzJ9W', '禁用用户', '13900139017', 'disabled@example.com', 0, '2025-05-30 01:24:17', '2025-05-30 01:24:17', 2);
INSERT INTO `users` VALUES (76, 'sssss', 'ssssss', 'sssss', 'ds', '7777@qq.com', 1, '2025-06-07 09:51:51', NULL, 2);

SET FOREIGN_KEY_CHECKS = 1;
