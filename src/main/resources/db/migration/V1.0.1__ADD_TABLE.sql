SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_dynamic_form_extend
-- ----------------------------
CREATE TABLE if NOT EXISTS `t_dynamic_form_extend`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `created_user` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建用户',
  `created_date` datetime(0) NOT NULL COMMENT '创建时间',
  `updated_user` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新用户',
  `updated_date` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `principal_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '关联信息ID',
  `extend_field` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展字段',
  `extend_value` varchar(2048) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展内容',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '动态表单内容表 ' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;