-- ----------------------------
-- Table： t_bic_scenic
-- Desc: 景区基础信息表添加字段
-- Date: 2019年10月21日14:08:36
-- ----------------------------

alter table `t_bic_poi` add  `inside_scenic` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否在景区内';

