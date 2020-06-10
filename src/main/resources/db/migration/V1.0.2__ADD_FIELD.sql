-- ----------------------------
-- Table： t_bic_poi、t_bic_traffic_agent、t_bic_periphery
-- Desc: 各个表添加相应的字段
-- Date: 2020年4月1日15:44:08
-- ----------------------------

alter table `t_bic_poi` add  `phone` varchar(19) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系电话';
alter table `t_bic_periphery` add  `summer_time` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '夏季营业时间';
alter table `t_bic_periphery` add  `winter_time` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '冬季营业时间';
alter table `t_bic_periphery` add  `traffic_notice` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '交通说明';

