-- ----------------------------
-- Table： t_bic_entertainment
-- Desc: 农家乐表添加字段
-- Date: 2020年2月11日14:18:14
-- ----------------------------

alter table `t_bic_entertainment` add  `guest_room_num` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客房数';
alter table `t_bic_entertainment` add  `guest_bed_num` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客房床位数';
alter table `t_bic_entertainment` add  `restaurant_num` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '餐厅数';
alter table `t_bic_entertainment` add  `restaurant_permit` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '餐厅可容多少人共同用餐';
alter table `t_bic_entertainment` add  `reception_permit` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '可接待人数';

