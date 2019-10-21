-- ----------------------------
-- Table： t_bic_scenic
-- Desc: 景区基础信息表添加字段
-- Date: 2019年10月21日14:08:36
-- ----------------------------

alter table `t_bic_scenic` add  `title_qp` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '景区名称全拼';
alter table `t_bic_scenic` add  `title_jp` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '景区名称简拼';

