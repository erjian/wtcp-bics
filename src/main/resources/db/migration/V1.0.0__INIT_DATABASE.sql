/*
 Source Server Type    : MySQL
 Source Schema         : wtcp-bics
 Date: 25/02/2020 18:39:27
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for flyway_schema_history
-- ----------------------------
CREATE TABLE IF NOT EXISTS `flyway_schema_history` (
  `installed_rank` int(11) NOT NULL,
  `version` varchar(50) DEFAULT NULL,
  `description` varchar(200) NOT NULL,
  `type` varchar(20) NOT NULL,
  `script` varchar(1000) NOT NULL,
  `checksum` int(11) DEFAULT NULL,
  `installed_by` varchar(100) NOT NULL,
  `installed_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `execution_time` int(11) NOT NULL,
  `success` tinyint(1) NOT NULL,
  PRIMARY KEY (`installed_rank`),
  KEY `flyway_schema_history_s_idx` (`success`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for t_bic_audit_log
-- ----------------------------
CREATE TABLE IF NOT EXISTS `t_bic_audit_log` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `principal_id` varchar(32) NOT NULL COMMENT '关联信息ID',
  `pre_status` tinyint(1) DEFAULT NULL COMMENT '审核前状态（0：待审，1：通过，2：退回，9：上线）',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '审核状态（0：待审，1：通过，2：退回，9：上线）',
  `opinion` varchar(500) DEFAULT NULL COMMENT '审核意见',
  `created_user` varchar(100) DEFAULT NULL COMMENT '创建用户',
  `created_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `type` int(1) NOT NULL COMMENT '操作记录类型（0：审核记录，1：上线/下线记录）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='审核日志表';

-- ----------------------------
-- Table structure for t_bic_business
-- ----------------------------
CREATE TABLE IF NOT EXISTS `t_bic_business` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `principal_id` varchar(32) NOT NULL COMMENT '关联主键',
  `ticket` varchar(200) DEFAULT NULL COMMENT '门票',
  `coupon` varchar(500) DEFAULT NULL COMMENT '优惠政策',
  `phone` varchar(19) DEFAULT NULL COMMENT '联系电话',
  `reserve` varchar(100) DEFAULT NULL COMMENT '预定页面',
  `ticket_notice` varchar(1000) DEFAULT NULL COMMENT '门票说明',
  `business_notice` varchar(1000) DEFAULT NULL COMMENT '营业说明',
  `traffic_notice` varchar(1000) DEFAULT NULL COMMENT '交通说明',
  `hint_notice` varchar(1000) DEFAULT NULL COMMENT '提示说明',
  `extend_notice` varchar(1000) DEFAULT NULL COMMENT '扩展说明',
  `service_facility` varchar(1000) DEFAULT NULL COMMENT '服务及设施',
  `created_user` varchar(100) DEFAULT NULL COMMENT '创建用户',
  `created_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_user` varchar(100) DEFAULT NULL COMMENT '更新用户',
  `updated_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='营业信息表';

-- ----------------------------
-- Table structure for t_bic_cate_relation
-- ----------------------------
CREATE TABLE IF NOT EXISTS `t_bic_cate_relation` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `cate_id` varchar(32) DEFAULT NULL COMMENT '美食ID',
  `catering_id` varchar(32) DEFAULT NULL COMMENT '餐饮服务ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='餐饮企业关联美食表';

-- ----------------------------
-- Table structure for t_bic_contact
-- ----------------------------
CREATE TABLE IF NOT EXISTS `t_bic_contact` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `principal_id` varchar(32) NOT NULL COMMENT '关联信息ID',
  `phone` varchar(19) NOT NULL COMMENT '办公室电话',
  `fax` varchar(20) DEFAULT NULL COMMENT '办公室传真',
  `website` varchar(100) DEFAULT NULL COMMENT '官网地址',
  `mail` varchar(100) DEFAULT NULL COMMENT '电子邮箱',
  `wechat` varchar(100) DEFAULT NULL COMMENT '官方微信',
  `micro_blog` varchar(100) DEFAULT NULL COMMENT '官方微博',
  `complaint_call` varchar(19) DEFAULT NULL COMMENT '投诉电话',
  `rescue_call` varchar(19) DEFAULT NULL COMMENT '救援电话',
  `created_user` varchar(100) DEFAULT NULL COMMENT '创建用户',
  `created_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_user` varchar(100) DEFAULT NULL COMMENT '更新用户',
  `updated_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通讯信息表';

-- ----------------------------
-- Table structure for t_bic_destination
-- ----------------------------
CREATE TABLE IF NOT EXISTS `t_bic_destination` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `status` tinyint(1) NOT NULL COMMENT '审核状态（0：待审，1：通过，9：上线）',
  `region_full_name` varchar(100) NOT NULL COMMENT '地区名称（格式示例：甘肃省,兰州市,城关区）',
  `region_full_code` varchar(100) NOT NULL COMMENT '地区编码（格式示例：62,6201,620102）',
  `region` varchar(20) NOT NULL COMMENT '所属区域',
  `introduce` varchar(1000) NOT NULL COMMENT '目的地介绍',
  `eat_introduce` varchar(1000) DEFAULT NULL COMMENT '美食介绍',
  `drink_introduce` varchar(1000) DEFAULT NULL COMMENT '饮品介绍',
  `play_introduce` varchar(1000) DEFAULT NULL COMMENT '游玩介绍',
  `tourism_introduce` varchar(1000) DEFAULT NULL COMMENT '旅游介绍',
  `shop_introduce` varchar(1000) DEFAULT NULL COMMENT '购物介绍',
  `entertainment_introduce` varchar(1000) DEFAULT NULL COMMENT '娱乐介绍',
  `weight` int(11) DEFAULT '0' COMMENT '权重',
  `dept_code` varchar(50) DEFAULT NULL COMMENT '组织机构编码',
  `created_user` varchar(100) DEFAULT NULL COMMENT '创建用户',
  `created_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_user` varchar(100) DEFAULT NULL COMMENT '更新用户',
  `updated_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='目的地基础信息表';

-- ----------------------------
-- Table structure for t_bic_destination_relation
-- ----------------------------
CREATE TABLE IF NOT EXISTS `t_bic_destination_relation` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `destination_id` varchar(32) NOT NULL COMMENT '目的地主键',
  `principal_id` varchar(32) NOT NULL COMMENT '关联信息ID',
  `created_user` varchar(100) DEFAULT NULL COMMENT '创建用户',
  `created_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建用户',
  `type` int(2) NOT NULL COMMENT '类型：例如：1.景区 2.周边 。。。(从字典表获取)',
  `dept_code` varchar(50) DEFAULT NULL COMMENT '组织机构编码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='目的地关联表';

-- ----------------------------
-- Table structure for t_bic_destination_tags
-- ----------------------------
CREATE TABLE IF NOT EXISTS `t_bic_destination_tags` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `principal_id` varchar(32) NOT NULL COMMENT '目的地ID',
  `tag_catagory` varchar(20) NOT NULL COMMENT '标签分类',
  `tag_name` varchar(20) NOT NULL COMMENT '标签名称',
  `dept_code` varchar(50) DEFAULT NULL COMMENT '组织机构编码',
  `created_user` varchar(100) DEFAULT NULL COMMENT '创建用户',
  `created_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_user` varchar(100) DEFAULT NULL COMMENT '更新用户',
  `updated_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='目的地标签表';

-- ----------------------------
-- Table structure for t_bic_drive_camp
-- ----------------------------
CREATE TABLE IF NOT EXISTS `t_bic_drive_camp` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `title` varchar(200) NOT NULL COMMENT '标题',
  `sub_title` varchar(200) DEFAULT NULL COMMENT '子标题',
  `code` varchar(50) DEFAULT NULL COMMENT '编码',
  `slogan` varchar(500) DEFAULT NULL COMMENT '广告语',
  `summary` varchar(1000) DEFAULT NULL COMMENT '摘要',
  `description` varchar(1000) DEFAULT NULL COMMENT '简介',
  `content` longtext COMMENT '内容',
  `weight` int(11) NOT NULL COMMENT '权重',
  `longitude` double(12,8) DEFAULT NULL COMMENT '经度',
  `latitude` double(12,8) DEFAULT NULL COMMENT '纬度',
  `region_full_name` varchar(100) DEFAULT NULL COMMENT '地区名称（格式示例：甘肃省,兰州市,城关区）',
  `region_full_code` varchar(100) DEFAULT NULL COMMENT '地区编码（格式示例：62,6201,620102）',
  `region` varchar(20) DEFAULT NULL COMMENT '所属区域',
  `address` varchar(200) DEFAULT NULL COMMENT '详细地址',
  `created_date` datetime DEFAULT NULL COMMENT '创建时间',
  `created_user` varchar(100) DEFAULT NULL COMMENT '创建用户',
  `updated_date` datetime DEFAULT NULL COMMENT '更新时间',
  `updated_user` varchar(100) DEFAULT NULL COMMENT '更新用户',
  `simple_spell` varchar(30) DEFAULT NULL COMMENT '拼音首字母',
  `full_spell` varchar(300) DEFAULT NULL COMMENT '全拼',
  `keyword` varchar(128) DEFAULT NULL COMMENT '关键字',
  `start_time` datetime DEFAULT NULL COMMENT '成立时间',
  `dept_code` varchar(50) DEFAULT NULL COMMENT '组织机构编码',
  `status` int(1) NOT NULL COMMENT '审核状态（0：待审，1：通过，2：退回，9：上线）',
  `traffic` varchar(500) DEFAULT NULL COMMENT '交通信息',
  `summer_time` varchar(20) DEFAULT NULL COMMENT '夏季营业时间',
  `winter_time` varchar(20) DEFAULT NULL COMMENT '冬季营业时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='自驾营地信息表';

-- ----------------------------
-- Table structure for t_bic_drive_camp_tags
-- ----------------------------
CREATE TABLE IF NOT EXISTS `t_bic_drive_camp_tags` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `principal_id` varchar(32) NOT NULL COMMENT '自驾营地ID',
  `tag_catagory` varchar(20) NOT NULL COMMENT '标签分类',
  `tag_name` varchar(20) NOT NULL COMMENT '标签名称',
  `created_user` varchar(100) DEFAULT NULL COMMENT '创建用户',
  `created_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_user` varchar(100) DEFAULT NULL COMMENT '更新用户',
  `updated_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='自驾营地标签表';

-- ----------------------------
-- Table structure for t_bic_enterprise
-- ----------------------------
CREATE TABLE IF NOT EXISTS `t_bic_enterprise` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `principal_id` varchar(32) NOT NULL COMMENT '关联信息ID',
  `name` varchar(100) NOT NULL COMMENT '企业名称',
  `type` int(11) NOT NULL COMMENT '企业类型（1：事业单位，2：有限责任公司，3：股份有限公司，4：私营企业非法人，5：个体工商户，6：国有企业）',
  `legal_person` varchar(50) NOT NULL COMMENT '法人',
  `general_manager` varchar(50) NOT NULL COMMENT '总经理',
  `business_license` varchar(20) DEFAULT NULL COMMENT '营业执照注册号',
  `organizational_code` varchar(20) DEFAULT NULL COMMENT '组织机构代码证',
  `info_filler` varchar(100) DEFAULT NULL COMMENT '信息填报人',
  `filler_phone` varchar(19) DEFAULT NULL COMMENT '填报人电话',
  `created_user` varchar(100) DEFAULT NULL COMMENT '创建用户',
  `created_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_user` varchar(100) DEFAULT NULL COMMENT '更新用户',
  `updated_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='企业信息表';

-- ----------------------------
-- Table structure for t_bic_entertainment
-- ----------------------------
CREATE TABLE IF NOT EXISTS `t_bic_entertainment` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `code` varchar(50) NOT NULL COMMENT '编码',
  `title` varchar(200) NOT NULL COMMENT '标题',
  `sub_title` varchar(200) DEFAULT NULL COMMENT '子标题',
  `slogan` varchar(500) DEFAULT NULL COMMENT '宣传语（二级标题）',
  `summary` varchar(1000) DEFAULT NULL COMMENT '摘要',
  `description` varchar(1000) DEFAULT NULL COMMENT '简介',
  `content` longtext COMMENT '内容',
  `weight` int(11) DEFAULT NULL,
  `longitude` double(12,8) DEFAULT NULL COMMENT '经度',
  `latitude` double(12,8) DEFAULT NULL COMMENT '纬度',
  `region_full_name` varchar(100) DEFAULT NULL COMMENT '地区名称（格式示例：甘肃省,兰州市,城关区）',
  `region_full_code` varchar(100) DEFAULT NULL COMMENT '地区编码（格式示例：62,6201,620102）',
  `region` varchar(20) DEFAULT NULL COMMENT '所属区域',
  `address` varchar(200) DEFAULT NULL COMMENT '详细地址',
  `status` int(1) NOT NULL DEFAULT '0' COMMENT '审核状态（0：待审，1：通过，2：退回，9：上线）',
  `scenic_id` varchar(32) DEFAULT NULL COMMENT '所属景区',
  `within_scenic` int(4) DEFAULT NULL COMMENT '是否在景区之内（1：是，0：否）',
  `within_park` int(4) DEFAULT '0' COMMENT '是否有停车场（1：是，0：否）',
  `num` int(11) DEFAULT NULL COMMENT '数量（车位数）',
  `dept_code` varchar(50) NOT NULL COMMENT '组织机构编码',
  `created_user` varchar(100) DEFAULT NULL COMMENT '创建用户',
  `created_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_user` varchar(100) DEFAULT NULL COMMENT '更新用户',
  `updated_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `guest_room_num` varchar(8) DEFAULT NULL COMMENT '客房数',
  `guest_bed_num` varchar(8) DEFAULT NULL COMMENT '客房床位数',
  `restaurant_num` varchar(8) DEFAULT NULL COMMENT '餐厅数',
  `restaurant_permit` varchar(8) DEFAULT NULL COMMENT '餐厅可容多少人共同用餐',
  `reception_permit` varchar(8) DEFAULT NULL COMMENT '可接待人数',
  `type` varchar(50) DEFAULT NULL COMMENT '农家乐类型',
  `start_time` datetime DEFAULT NULL COMMENT '开业时间',
  `summer_time` varchar(20) DEFAULT NULL COMMENT '夏季营业时间',
  `winter_time` varchar(20) DEFAULT NULL COMMENT '冬季营业时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='休闲娱乐表';

-- ----------------------------
-- Table structure for t_bic_entertainment_tags
-- ----------------------------
CREATE TABLE IF NOT EXISTS `t_bic_entertainment_tags` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `principal_id` varchar(32) NOT NULL COMMENT '周边ID',
  `tag_catagory` varchar(20) NOT NULL COMMENT '标签分类',
  `tag_name` varchar(20) NOT NULL COMMENT '标签名称',
  `created_user` varchar(100) DEFAULT NULL COMMENT '创建用户',
  `created_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_user` varchar(100) DEFAULT NULL COMMENT '更新用户',
  `updated_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='休闲娱乐标签表';

-- ----------------------------
-- Table structure for t_bic_extend
-- ----------------------------
CREATE TABLE IF NOT EXISTS `t_bic_extend` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `principal_id` varchar(32) NOT NULL COMMENT '关联信息ID',
  `code` varchar(50) NOT NULL COMMENT '编码',
  `title` varchar(200) NOT NULL COMMENT '标题',
  `sub_title` varchar(200) NOT NULL COMMENT '子标题',
  `slogan` varchar(500) DEFAULT NULL COMMENT '宣传语（二级标题）',
  `summary` varchar(1000) DEFAULT NULL COMMENT '摘要',
  `description` varchar(1000) DEFAULT NULL COMMENT '简介',
  `content` longtext COMMENT '内容',
  `weight` int(11) DEFAULT '0' COMMENT '权重',
  `type` varchar(50) NOT NULL COMMENT '类型（从字典获取）',
  `status` int(1) NOT NULL DEFAULT '0' COMMENT '审核状态（0：待审，1：通过，2：退回，9：上线）',
  `price` float(8,2) DEFAULT NULL COMMENT '价格',
  `created_user` varchar(100) DEFAULT NULL COMMENT '创建用户',
  `created_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_user` varchar(100) DEFAULT NULL COMMENT '更新用户',
  `updated_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `dept_code` varchar(50) NOT NULL COMMENT '组织机构编码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='扩展表';

-- ----------------------------
-- Table structure for t_bic_extend_tags
-- ----------------------------
CREATE TABLE IF NOT EXISTS `t_bic_extend_tags` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `principal_id` varchar(32) NOT NULL COMMENT '扩展ID',
  `tag_catagory` varchar(20) NOT NULL COMMENT '标签分类',
  `tag_name` varchar(20) NOT NULL COMMENT '标签名称',
  `created_user` varchar(100) DEFAULT NULL COMMENT '创建用户',
  `created_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_user` varchar(100) DEFAULT NULL COMMENT '更新用户',
  `updated_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='扩展标签表';

-- ----------------------------
-- Table structure for t_bic_material
-- ----------------------------
CREATE TABLE IF NOT EXISTS `t_bic_material` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `principal_id` varchar(32) NOT NULL COMMENT '关联信息ID',
  `material_id` varchar(32) DEFAULT NULL COMMENT '素材ID',
  `file_type` varchar(10) NOT NULL COMMENT '素材类型（image：图片，audio：音频，video：视频， file: 文档）',
  `file_url` varchar(500) NOT NULL COMMENT '路径(相对路径)',
  `file_identify` varchar(50) DEFAULT NULL COMMENT '素材标识（1：标题图片，2：亮点图片，3：标题且亮点图片）',
  `created_user` varchar(100) DEFAULT NULL COMMENT '创建用户',
  `created_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_user` varchar(100) DEFAULT NULL COMMENT '更新用户',
  `updated_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `cover_image_url` varchar(500) DEFAULT NULL COMMENT '视频封面图地址',
  `file_name` varchar(200) DEFAULT NULL COMMENT '素材名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='素材表';

-- ----------------------------
-- Table structure for t_bic_periphery
-- ----------------------------
CREATE TABLE IF NOT EXISTS `t_bic_periphery` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `category` varchar(32) NOT NULL COMMENT '类别（1：特色小吃，2：餐饮服务，3：小吃街，4：购物场所，5：特产）',
  `code` varchar(50) NOT NULL COMMENT '编码',
  `title` varchar(200) NOT NULL COMMENT '标题',
  `sub_title` varchar(200) NOT NULL COMMENT '子标题',
  `slogan` varchar(500) DEFAULT NULL COMMENT '宣传语（二级标题）',
  `summary` varchar(1000) DEFAULT NULL COMMENT '摘要',
  `description` varchar(1000) DEFAULT NULL COMMENT '简介',
  `content` longtext COMMENT '内容',
  `weight` int(11) DEFAULT '0' COMMENT '权重',
  `longitude` double(12,8) DEFAULT NULL COMMENT '经度',
  `latitude` double(12,8) DEFAULT NULL COMMENT '纬度',
  `region_full_name` varchar(100) NOT NULL COMMENT '地区名称（格式示例：甘肃省,兰州市,城关区）',
  `region_full_code` varchar(100) NOT NULL COMMENT '地区编码（格式示例：62,6201,620102）',
  `region` varchar(100) NOT NULL COMMENT '所属区域',
  `address` varchar(200) DEFAULT NULL COMMENT '详细地址',
  `phone` varchar(19) DEFAULT NULL COMMENT '联系电话',
  `trading_area` varchar(100) DEFAULT NULL COMMENT '商圈（万达商圈，兰州中心，...）',
  `type` varchar(20) DEFAULT NULL COMMENT '类型（中餐厅，川菜，...）',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '审核状态（0：待审，1：通过，9：上线）',
  `score` float(3,2) DEFAULT NULL COMMENT '评分',
  `per_consumption` float(6,2) DEFAULT NULL COMMENT '人均消费',
  `dept_code` varchar(50) NOT NULL COMMENT '组织机构编码',
  `created_user` varchar(100) DEFAULT NULL COMMENT '创建用户',
  `created_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_user` varchar(100) DEFAULT NULL COMMENT '更新用户',
  `updated_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `park_score` float(3,2) DEFAULT NULL COMMENT '停车方便评分',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='周边信息表';

-- ----------------------------
-- Table structure for t_bic_periphery_tags
-- ----------------------------
CREATE TABLE IF NOT EXISTS `t_bic_periphery_tags` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `principal_id` varchar(32) NOT NULL COMMENT '周边ID',
  `tag_catagory` varchar(20) NOT NULL COMMENT '标签分类',
  `tag_name` varchar(20) NOT NULL COMMENT '标签名称',
  `created_user` varchar(100) DEFAULT NULL COMMENT '创建用户',
  `created_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_user` varchar(100) DEFAULT NULL COMMENT '更新用户',
  `updated_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='周边标签表';

-- ----------------------------
-- Table structure for t_bic_poi
-- ----------------------------
CREATE TABLE IF NOT EXISTS `t_bic_poi` (
  `id` varchar(32) NOT NULL DEFAULT '' COMMENT '主键',
  `parent_id` varchar(32) DEFAULT '0' COMMENT '父ID（第一级景点时值为0）',
  `principal_id` varchar(32) DEFAULT NULL COMMENT '关联信息ID',
  `code` varchar(50) NOT NULL COMMENT '编码',
  `title` varchar(200) NOT NULL COMMENT '标题',
  `sub_title` varchar(200) DEFAULT NULL COMMENT '子标题',
  `slogan` varchar(500) DEFAULT NULL COMMENT '宣传语（二级标题）',
  `summary` varchar(1000) DEFAULT NULL COMMENT '摘要',
  `description` varchar(1000) DEFAULT NULL COMMENT '简介',
  `content` longtext COMMENT '内容',
  `weight` float(3,2) DEFAULT '0.00' COMMENT '权重',
  `longitude` double(12,8) NOT NULL COMMENT '经度',
  `latitude` double(12,8) NOT NULL COMMENT '纬度',
  `region_full_name` varchar(100) DEFAULT NULL COMMENT '地区名称（格式示例：甘肃省,兰州市,城关区）',
  `region_full_code` varchar(100) DEFAULT NULL COMMENT '地区编码（格式示例：62,6201,620102）',
  `region` varchar(20) DEFAULT NULL COMMENT '所属区域',
  `address` varchar(200) DEFAULT NULL COMMENT '详细地址',
  `status` int(1) unsigned zerofill DEFAULT '0' COMMENT '审核状态（0：待审，1：通过，9：上线）',
  `dept_code` varchar(50) NOT NULL COMMENT '组织机构编码',
  `created_user` varchar(100) DEFAULT NULL COMMENT '创建用户',
  `created_date` datetime NOT NULL COMMENT '创建时间',
  `updated_user` varchar(100) DEFAULT NULL COMMENT '更新用户',
  `updated_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `type` varchar(20) NOT NULL COMMENT '类型（1：厕所，2：停车场，3：出入口，4：游客中心，5：景点）',
  `num` int(11) DEFAULT NULL COMMENT '数量',
  `woman_pit` int(11) DEFAULT NULL COMMENT '女坑位数',
  `man_pit` int(11) DEFAULT NULL COMMENT '男坑位数',
  `inside_scenic` varchar(2) DEFAULT NULL COMMENT '是否在景区内',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='POI表';

-- ----------------------------
-- Table structure for t_bic_poi_tags
-- ----------------------------
CREATE TABLE IF NOT EXISTS `t_bic_poi_tags` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `principal_id` varchar(32) NOT NULL COMMENT '景点ID',
  `tag_catagory` varchar(20) NOT NULL COMMENT '标签分类',
  `tag_name` varchar(20) NOT NULL COMMENT '标签名称',
  `created_user` varchar(100) DEFAULT NULL COMMENT '创建用户',
  `created_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_user` varchar(100) DEFAULT NULL COMMENT '更新用户',
  `updated_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='景点标签表';

-- ----------------------------
-- Table structure for t_bic_rental_car
-- ----------------------------
CREATE TABLE IF NOT EXISTS `t_bic_rental_car` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `code` varchar(50) NOT NULL,
  `created_date` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_user` varchar(100) NOT NULL COMMENT '创建人',
  `updated_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `updated_user` varchar(100) DEFAULT NULL COMMENT '更新人',
  `title` varchar(200) NOT NULL COMMENT '租车公司名称',
  `sub_title` varchar(200) DEFAULT NULL COMMENT '简称',
  `title_qp` varchar(800) DEFAULT NULL COMMENT '名称全拼',
  `title_jp` varchar(200) DEFAULT NULL COMMENT '名称简拼',
  `region_full_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci DEFAULT NULL COMMENT '所在地区编码',
  `region_full_name` varchar(500) DEFAULT NULL COMMENT '所在地区名称',
  `address` varchar(500) DEFAULT NULL COMMENT '详细地址',
  `region` varchar(100) DEFAULT NULL COMMENT '所属区域',
  `longitude` double(12,8) DEFAULT NULL COMMENT '经度',
  `latitude` double(12,8) DEFAULT NULL COMMENT '纬度',
  `establish_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '成立时间',
  `chartered_bus_type` varchar(50) DEFAULT NULL COMMENT '包车类型',
  `regular_bus_type` varchar(50) DEFAULT NULL COMMENT '班车类型',
  `keyword` varchar(500) DEFAULT NULL COMMENT '关键字',
  `slogan` varchar(500) DEFAULT NULL COMMENT '宣传语',
  `summary` varchar(1000) DEFAULT NULL COMMENT '简介',
  `content` longtext COMMENT '内容',
  `weight` int(11) DEFAULT NULL COMMENT '权重',
  `status` tinyint(1) NOT NULL COMMENT '审核状态（0：待审，1：通过，9：上线）',
  `dept_code` varchar(50) DEFAULT NULL COMMENT '组织机构编码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='租车管理表';

-- ----------------------------
-- Table structure for t_bic_rental_car_tags
-- ----------------------------
CREATE TABLE IF NOT EXISTS `t_bic_rental_car_tags` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `principal_id` varchar(32) NOT NULL COMMENT '租车ID',
  `tag_catagory` varchar(20) NOT NULL COMMENT '标签分类',
  `tag_name` varchar(20) NOT NULL COMMENT '标签名称',
  `created_user` varchar(100) DEFAULT NULL COMMENT '创建用户',
  `created_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_user` varchar(100) DEFAULT NULL COMMENT '更新用户',
  `updated_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='租车标签表';

-- ----------------------------
-- Table structure for t_bic_resource_config
-- ----------------------------
CREATE TABLE IF NOT EXISTS `t_bic_resource_config` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `code` varchar(255) NOT NULL COMMENT '资源编码',
  `name` varchar(255) NOT NULL COMMENT '资源名称',
  `parent_code` varchar(255) NOT NULL COMMENT '上级资源编码',
  `query_way` varchar(2) NOT NULL COMMENT '查询方式（1 单表查询，0 多表查询）',
  `table_name` varchar(100) DEFAULT NULL COMMENT '表名，如 t_bic_scenic',
  `column_name` varchar(100) DEFAULT NULL COMMENT '类型字段名，如 category',
  `created_user` varchar(100) DEFAULT NULL COMMENT '创建用户',
  `created_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_user` varchar(100) DEFAULT NULL COMMENT '更新用户',
  `updated_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资源配置表';

-- ----------------------------
-- Table structure for t_bic_scenic
-- ----------------------------
CREATE TABLE IF NOT EXISTS `t_bic_scenic` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `code` varchar(50) NOT NULL COMMENT '编码',
  `title` varchar(200) NOT NULL COMMENT '标题',
  `sub_title` varchar(200) DEFAULT NULL COMMENT '子标题',
  `slogan` varchar(500) DEFAULT NULL COMMENT '宣传语（二级标题）',
  `summary` varchar(1000) DEFAULT NULL COMMENT '摘要',
  `description` varchar(1000) DEFAULT NULL COMMENT '简介',
  `content` longtext COMMENT '内容',
  `weight` int(11) DEFAULT '0' COMMENT '权重',
  `longitude` double(12,8) DEFAULT NULL COMMENT '经度',
  `latitude` double(12,8) DEFAULT NULL COMMENT '纬度',
  `region_full_name` varchar(100) DEFAULT NULL COMMENT '地区名称（格式示例：甘肃省,兰州市,城关区）',
  `region_full_code` varchar(100) DEFAULT NULL COMMENT '地区编码（格式示例：62,6201,620102）',
  `region` varchar(20) DEFAULT NULL COMMENT '所属区域',
  `address` varchar(200) DEFAULT NULL COMMENT '详细地址',
  `level` varchar(50) DEFAULT NULL COMMENT '级别（0：非A级，1：1A级，2：2A级，3：3A级，4：4A级，5：5A级）',
  `area` double(20,2) DEFAULT NULL COMMENT '面积',
  `category` varchar(50) NOT NULL COMMENT '类别（1：普通景区，2：乡村游）',
  `panoramic_url` varchar(100) DEFAULT NULL COMMENT '全景地址',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '审核状态（0：待审，1：通过，9：上线）',
  `playtime` float(3,1) DEFAULT NULL COMMENT '游玩时间',
  `score` float(3,2) DEFAULT NULL COMMENT '评分',
  `dept_code` varchar(50) NOT NULL COMMENT '组织机构编码',
  `created_user` varchar(100) DEFAULT NULL COMMENT '创建用户',
  `created_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_user` varchar(100) DEFAULT NULL COMMENT '更新用户',
  `updated_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `title_qp` varchar(800) DEFAULT NULL COMMENT '景区名称全拼',
  `title_jp` varchar(200) DEFAULT NULL COMMENT '景区名称简拼',
  `start_time` datetime DEFAULT NULL COMMENT '开业时间',
  `summer_time` varchar(20) DEFAULT NULL COMMENT '夏季营业时间',
  `winter_time` varchar(20) DEFAULT NULL COMMENT '冬季营业时间',
  `rank` tinyint(5) DEFAULT NULL COMMENT '排名',
  `light_title` varchar(200) DEFAULT NULL COMMENT '亮点标题',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='景区表';

-- ----------------------------
-- Table structure for t_bic_scenic_tags
-- ----------------------------
CREATE TABLE IF NOT EXISTS `t_bic_scenic_tags` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `principal_id` varchar(32) NOT NULL COMMENT '景区ID',
  `tag_catagory` varchar(20) NOT NULL COMMENT '标签分类',
  `tag_name` varchar(20) NOT NULL COMMENT '标签名称',
  `created_user` varchar(100) DEFAULT NULL COMMENT '创建用户',
  `created_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_user` varchar(100) DEFAULT NULL COMMENT '更新用户',
  `updated_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='景区标签表';

-- ----------------------------
-- Table structure for t_bic_traffic_agent
-- ----------------------------
CREATE TABLE IF NOT EXISTS `t_bic_traffic_agent` (
  `id` varchar(32) CHARACTER SET utf32 NOT NULL COMMENT '主键',
  `title` varchar(200) NOT NULL COMMENT '标题',
  `code` varchar(50) DEFAULT NULL COMMENT '编码',
  `summary` varchar(1000) DEFAULT NULL COMMENT '摘要',
  `description` varchar(1000) DEFAULT NULL COMMENT '简介',
  `content` longtext COMMENT '内容',
  `weight` int(11) NOT NULL COMMENT '权重',
  `longitude` double(12,8) NOT NULL COMMENT '经度',
  `latitude` double(12,8) NOT NULL COMMENT '纬度',
  `region_full_name` varchar(100) NOT NULL COMMENT '地区名称（格式示例：甘肃省,兰州市,城关区）',
  `region_full_code` varchar(100) NOT NULL COMMENT '地区编码（格式示例：62,6201,620102）',
  `region` varchar(20) NOT NULL COMMENT '所属区域',
  `address` varchar(200) NOT NULL COMMENT '详细地址',
  `created_date` datetime DEFAULT NULL COMMENT '创建时间',
  `created_user` varchar(100) DEFAULT NULL COMMENT '创建用户',
  `updated_date` datetime DEFAULT NULL COMMENT '更新时间',
  `updated_user` varchar(100) DEFAULT NULL COMMENT '更新用户',
  `status` int(1) NOT NULL COMMENT '审核状态（0：待审，1：通过，2：退回，9：上线）',
  `type` varchar(50) NOT NULL DEFAULT '' COMMENT '交通类型',
  `simple_spell` varchar(100) DEFAULT NULL COMMENT '拼音首字母',
  `full_spell` varchar(300) DEFAULT NULL COMMENT '全拼',
  `gould_id` varchar(50) DEFAULT NULL,
  `phone` varchar(100) DEFAULT NULL,
  `dept_code` varchar(50) DEFAULT NULL COMMENT '组织机构编码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='交通枢纽信息表';

-- ----------------------------
-- Table structure for t_bic_travel_agent
-- ----------------------------
CREATE TABLE IF NOT EXISTS `t_bic_travel_agent` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `title` varchar(200) NOT NULL COMMENT '标题',
  `sub_title` varchar(200) DEFAULT NULL COMMENT '子标题',
  `code` varchar(50) DEFAULT NULL COMMENT '编码',
  `slogan` varchar(500) DEFAULT NULL COMMENT '广告语',
  `summary` varchar(1000) DEFAULT NULL COMMENT '摘要',
  `description` varchar(1000) DEFAULT NULL COMMENT '简介',
  `content` longtext COMMENT '内容',
  `weight` int(11) NOT NULL COMMENT '权重',
  `longitude` double(12,8) DEFAULT NULL COMMENT '经度',
  `latitude` double(12,8) DEFAULT NULL COMMENT '纬度',
  `region_full_name` varchar(100) DEFAULT NULL COMMENT '地区名称（格式示例：甘肃省,兰州市,城关区）',
  `region_full_code` varchar(100) DEFAULT NULL COMMENT '地区编码（格式示例：62,6201,620102）',
  `region` varchar(20) DEFAULT NULL COMMENT '所属区域',
  `address` varchar(200) DEFAULT NULL COMMENT '详细地址',
  `created_date` datetime DEFAULT NULL COMMENT '创建时间',
  `created_user` varchar(100) DEFAULT NULL COMMENT '创建用户',
  `updated_date` datetime DEFAULT NULL COMMENT '更新时间',
  `updated_user` varchar(100) DEFAULT NULL COMMENT '更新用户',
  `simple_spell` varchar(30) DEFAULT NULL COMMENT '拼音首字母',
  `full_spell` varchar(300) DEFAULT NULL COMMENT '全拼',
  `keyword` varchar(128) DEFAULT NULL COMMENT '关键字',
  `business_scope` varchar(500) DEFAULT NULL COMMENT '经营范围',
  `business_type` varchar(300) DEFAULT NULL COMMENT '主营业务类型',
  `destination_city` varchar(300) DEFAULT NULL COMMENT '目的地城市',
  `from_city` varchar(300) DEFAULT NULL COMMENT '客源地城市',
  `level` varchar(50) DEFAULT NULL COMMENT '级别',
  `level_time` datetime DEFAULT NULL COMMENT '等级评定时间',
  `start_time` datetime DEFAULT NULL COMMENT '成立时间',
  `team_type` varchar(100) DEFAULT NULL COMMENT '接团类型',
  `tour_type` varchar(300) DEFAULT NULL COMMENT '特种旅游类型',
  `dept_code` varchar(100) DEFAULT NULL COMMENT '组织机构编码',
  `status` int(1) NOT NULL COMMENT '审核状态（0：待审，1：通过，2：退回，9：上线）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='旅行社信息表';

-- ----------------------------
-- Table structure for t_bic_travel_agent_tags
-- ----------------------------
CREATE TABLE IF NOT EXISTS `t_bic_travel_agent_tags` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `principal_id` varchar(32) NOT NULL COMMENT '旅行社ID',
  `tag_catagory` varchar(20) NOT NULL COMMENT '标签分类',
  `tag_name` varchar(20) NOT NULL COMMENT '标签名称',
  `created_user` varchar(100) DEFAULT NULL COMMENT '创建用户',
  `created_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_user` varchar(100) DEFAULT NULL COMMENT '更新用户',
  `updated_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='旅行社标签表';


