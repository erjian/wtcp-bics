/**
 * 2019年10月09日 09:31:50
 */

CREATE TABLE IF NOT EXISTS `bic_scenic` (
`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
`code` varchar(50) NOT NULL COMMENT '编码',
`title` varchar(200) NOT NULL COMMENT '标题',
`sub_title` varchar(200) NOT NULL COMMENT '子标题',
`slogan` varchar(500) NULL COMMENT '宣传语（二级标题）',
`summary` varchar(1000) NULL COMMENT '摘要',
`description` varchar(1000) NULL COMMENT '简介',
`content` longtext NULL COMMENT '内容',
`weight` float(3,2) NULL DEFAULT 0 COMMENT '权重',
`longitude` double(12,8) NOT NULL COMMENT '经度',
`latitude` double(12,8) NOT NULL COMMENT '纬度',
`region_full_name` varchar(100) NOT NULL COMMENT '地区名称（格式示例：甘肃省,兰州市,城关区）',
`region_full_code` varchar(100) NOT NULL COMMENT '地区编码（格式示例：62,6201,620102）',
`region` varchar(20) NOT NULL COMMENT '所属区域',
`address` varchar(200) NOT NULL COMMENT '详细地址',
`level` int(11) NULL COMMENT '级别（0：非A级，1：1A级，2：2A级，3：3A级，4：4A级，5：5A级）',
`area` double(20,2) NULL COMMENT '面积',
`category` int(11) NOT NULL COMMENT '类别（1：普通景区，2：乡村游）',
`panoramic_url` varchar(100) NULL COMMENT '全景地址',
`status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '审核状态（0：待审，1：通过，9：上线）',
`playtime` float(3,1) NULL COMMENT '游玩时间',
`score` float(3,2) NULL COMMENT '评分',
`dept_code` varchar(50) NOT NULL COMMENT '组织机构编码',
`created_user` varchar(100) NULL COMMENT '创建用户',
`created_date` datetime NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
`updated_user` varchar(100) NULL COMMENT '更新用户',
`updated_date` datetime NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
PRIMARY KEY (`id`)
)
ENGINE = InnoDB
COMMENT = '景区表';

CREATE TABLE IF NOT EXISTS  `bic_scenic_spot` (
`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
`parent_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '父ID（第一级景点时值为0）',
`scenic_id` bigint(20) NOT NULL COMMENT '景区ID',
`code` varchar(50) NOT NULL COMMENT '编码',
`title` varchar(200) NOT NULL COMMENT '标题',
`sub_title` varchar(200) NOT NULL COMMENT '子标题',
`slogan` varchar(500) NULL COMMENT '宣传语（二级标题）',
`summary` varchar(1000) NULL COMMENT '摘要',
`description` varchar(1000) NULL COMMENT '简介',
`content` longtext NULL COMMENT '内容',
`weight` float(3,2) NULL DEFAULT 0 COMMENT '权重',
`longitude` double(12,8) NOT NULL COMMENT '经度',
`latitude` double(12,8) NOT NULL COMMENT '纬度',
`region_full_name` varchar(100) NOT NULL COMMENT '地区名称（格式示例：甘肃省,兰州市,城关区）',
`region_full_code` varchar(100) NOT NULL COMMENT '地区编码（格式示例：62,6201,620102）',
`region` varchar(20) NOT NULL COMMENT '所属区域',
`address` varchar(200) NOT NULL COMMENT '详细地址',
`status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '审核状态（0：待审，1：通过，9：上线）',
`dept_code` varchar(50) NOT NULL COMMENT '组织机构编码',
`created_user` varchar(100) NULL COMMENT '创建用户',
`created_date` datetime NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
`updated_user` varchar(100) NULL COMMENT '更新用户',
`updated_date` datetime NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
PRIMARY KEY (`id`)
)
ENGINE = InnoDB
COMMENT = '景点表';

CREATE TABLE IF NOT EXISTS  `bic_enterprise` (
`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
`principal_id` bigint(20) NOT NULL COMMENT '关联信息ID',
`name` varchar(100) NOT NULL COMMENT '企业名称',
`type` int(11) NOT NULL COMMENT '企业类型（1：事业单位，2：有限责任公司，3：股份有限公司，4：私营企业非法人，5：个体工商户，6：国有企业）',
`legal_person` varchar(50) NOT NULL COMMENT '法人',
`general_manager` varchar(50) NOT NULL COMMENT '总经理',
`business_license` varchar(20) NULL COMMENT '营业执照注册号',
`organizational_code` varchar(20) NULL COMMENT '组织机构代码证',
`info_filler` varchar(100) NULL COMMENT '信息填报人',
`filler_phone` varchar(19) NULL COMMENT '填报人电话',
`created_user` varchar(100) NULL COMMENT '创建用户',
`created_date` datetime NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
`updated_user` varchar(100) NULL COMMENT '更新用户',
`updated_date` datetime NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
PRIMARY KEY (`id`)
)
ENGINE = InnoDB
COMMENT = '企业信息表';

CREATE TABLE IF NOT EXISTS  `bic_contact` (
`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
`principal_id` bigint(20) NOT NULL COMMENT '关联信息ID',
`phone` varchar(19) NOT NULL COMMENT '办公室电话',
`fax` varchar(20) NULL COMMENT '办公室传真',
`website` varchar(100) NULL COMMENT '官网地址',
`mail` varchar(100) NULL COMMENT '电子邮箱',
`wechat` varchar(100) NULL COMMENT '官方微信',
`micro_blog` varchar(100) NULL COMMENT '官方微博',
`complaint_call` varchar(19) NULL COMMENT '投诉电话',
`rescue_call` varchar(19) NULL COMMENT '救援电话',
`created_user` varchar(100) NULL COMMENT '创建用户',
`created_date` datetime NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
`updated_user` varchar(100) NULL COMMENT '更新用户',
`updated_date` datetime NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
PRIMARY KEY (`id`)
)
ENGINE = InnoDB
COMMENT = '通讯信息表';

CREATE TABLE IF NOT EXISTS  `bic_business` (
`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
`principal_id` bigint(20) NOT NULL COMMENT '关联主键',
`ticket` varchar(200) NULL COMMENT '门票',
`coupon` varchar(500) NULL COMMENT '优惠政策',
`phone` varchar(19) NULL COMMENT '联系电话',
`reserve` varchar(100) NULL COMMENT '预定页面',
`ticket_notice` varchar(1000) NULL COMMENT '门票说明',
`business_notice` varchar(1000) NULL COMMENT '营业说明',
`traffic_notice` varchar(1000) NULL COMMENT '交通说明',
`hint_notice` varchar(1000) NULL COMMENT '提示说明',
`extend_notice` varchar(1000) NULL COMMENT '扩展说明',
`service_facility` varchar(1000) NULL COMMENT '服务及设施',
`created_user` varchar(100) NULL COMMENT '创建用户',
`created_date` datetime NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
`updated_user` varchar(100) NULL COMMENT '更新用户',
`updated_date` datetime NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
PRIMARY KEY (`id`)
)
ENGINE = InnoDB
COMMENT = '营业信息表';

CREATE TABLE IF NOT EXISTS  `bic_scenic_tags` (
`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
`principal_id` bigint(20) NOT NULL COMMENT '景区ID',
`tag_catagory` varchar(20) NOT NULL COMMENT '标签分类',
`tag_name` varchar(20) NOT NULL COMMENT '标签名称',
`created_user` varchar(100) NULL COMMENT '创建用户',
`created_date` datetime NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
`updated_user` varchar(100) NULL COMMENT '更新用户',
`updated_date` datetime NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
PRIMARY KEY (`id`)
)
ENGINE = InnoDB
COMMENT = '景区标签表';

CREATE TABLE IF NOT EXISTS  `bic_scenic_spot_tags` (
`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
`principal_id` bigint(20) NOT NULL COMMENT '景点ID',
`tag_catagory` varchar(20) NOT NULL COMMENT '标签分类',
`tag_name` varchar(20) NOT NULL COMMENT '标签名称',
`created_user` varchar(100) NULL COMMENT '创建用户',
`created_date` datetime NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
`updated_user` varchar(100) NULL COMMENT '更新用户',
`updated_date` datetime NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
PRIMARY KEY (`id`)
)
ENGINE = InnoDB
COMMENT = '景点标签表';

CREATE TABLE IF NOT EXISTS  `bic_poi` (
`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
`principal_id` bigint(20) NOT NULL COMMENT '关联信息ID',
`type` int(11) NOT NULL COMMENT '类型（1：厕所，2：停车场，3：出入口，4：游客中心）',
`code` varchar(50) NOT NULL COMMENT '编码',
`title` varchar(200) NOT NULL COMMENT '标题',
`sub_title` varchar(200) NOT NULL COMMENT '子标题',
`slogan` varchar(500) NULL COMMENT '宣传语（二级标题）',
`summary` varchar(1000) NULL COMMENT '摘要',
`description` varchar(1000) NULL COMMENT '简介',
`content` longtext NULL COMMENT '内容',
`weight` float(3,2) NULL DEFAULT 0 COMMENT '权重',
`longitude` double(12,8) NOT NULL COMMENT '经度',
`latitude` double(12,8) NOT NULL COMMENT '纬度',
`region_full_name` varchar(100) NOT NULL COMMENT '地区名称（格式示例：甘肃省,兰州市,城关区）',
`region_full_code` varchar(100) NOT NULL COMMENT '地区编码（格式示例：62,6201,620102）',
`region` varchar(20) NOT NULL COMMENT '所属区域',
`address` varchar(200) NOT NULL COMMENT '详细地址',
`num` int(11) NULL DEFAULT 0 COMMENT '数量（厕所坑位，停车场车位）',
`status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '审核状态（0：待审，1：通过，9：上线）',
`dept_code` varchar(50) NOT NULL COMMENT '组织机构编码',
`created_user` varchar(100) NULL COMMENT '创建用户',
`created_date` datetime NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
`updated_user` varchar(100) NULL COMMENT '更新用户',
`updated_date` datetime NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
PRIMARY KEY (`id`)
)
ENGINE = InnoDB
COMMENT = 'poi表';

CREATE TABLE IF NOT EXISTS  `bic_entertainment` (
`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
`type` int(11) NOT NULL COMMENT '类型（1：农家乐）',
`code` varchar(50) NOT NULL COMMENT '编码',
`title` varchar(200) NOT NULL COMMENT '标题',
`sub_title` varchar(200) NOT NULL COMMENT '子标题',
`slogan` varchar(500) NULL COMMENT '宣传语（二级标题）',
`summary` varchar(1000) NULL COMMENT '摘要',
`description` varchar(1000) NULL COMMENT '简介',
`content` longtext NULL COMMENT '内容',
`weight` float(3,2) NULL DEFAULT 0 COMMENT '权重',
`longitude` double(12,8) NOT NULL COMMENT '经度',
`latitude` double(12,8) NOT NULL COMMENT '纬度',
`region_full_name` varchar(100) NOT NULL COMMENT '地区名称（格式示例：甘肃省,兰州市,城关区）',
`region_full_code` varchar(100) NOT NULL COMMENT '地区编码（格式示例：62,6201,620102）',
`region` varchar(20) NOT NULL COMMENT '所属区域',
`address` varchar(200) NOT NULL COMMENT '详细地址',
`status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '审核状态（0：待审，1：通过，9：上线）',
`scenic_id` bigint NULL COMMENT '所属景区',
`within_scenic` tinyint NOT NULL COMMENT '是否在景区之内（1：是，0：否）',
`within_park` tinyint NOT NULL DEFAULT 0 COMMENT '是否有停车场（1：是，0：否）',
`num` int NULL COMMENT '数量（车位数）',
`dept_code` varchar(50) NOT NULL COMMENT '组织机构编码',
`created_user` varchar(100) NULL COMMENT '创建用户',
`created_date` datetime NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
`updated_user` varchar(100) NULL COMMENT '更新用户',
`updated_date` datetime NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
PRIMARY KEY (`id`)
)
ENGINE = InnoDB
COMMENT = '休闲娱乐表';

CREATE TABLE IF NOT EXISTS  `bic_destination_tags` (
`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
`principal_id` bigint(20) NOT NULL COMMENT '目的地ID',
`tag_catagory` varchar(20) NOT NULL COMMENT '标签分类',
`tag_name` varchar(20) NOT NULL COMMENT '标签名称',
`created_user` varchar(100) NULL COMMENT '创建用户',
`created_date` datetime NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
`updated_user` varchar(100) NULL COMMENT '更新用户',
`updated_date` datetime NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
PRIMARY KEY (`id`)
)
ENGINE = InnoDB
COMMENT = '目的地标签表';

CREATE TABLE IF NOT EXISTS  `bic_material` (
`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
`principal_id` bigint(20) NOT NULL COMMENT '关联信息ID',
`material_id` varchar(32) NOT NULL COMMENT '素材ID',
`file_type` varchar(10) NOT NULL COMMENT '素材类型（image：图片，audio：音频，video：视频， file: 文档）',
`file_url` varchar(200) NOT NULL COMMENT '路径(相对路径)',
`file_identify` tinyint(1) NULL COMMENT '素材标识（1：标题图片，2：亮点图片，3：标题且亮点图片）',
`created_user` varchar(100) NULL COMMENT '创建用户',
`created_date` datetime NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
`updated_user` varchar(100) NULL COMMENT '更新用户',
`updated_date` datetime NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
PRIMARY KEY (`id`)
)
ENGINE = InnoDB
COMMENT = '素材表';

CREATE TABLE IF NOT EXISTS  `bic_periphery` (
`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
`category` int(11) NOT NULL COMMENT '类别（1：特色小吃，2：餐饮服务，3：小吃街，4：购物场所，5：特产）',
`code` varchar(50) NOT NULL COMMENT '编码',
`title` varchar(200) NOT NULL COMMENT '标题',
`sub_title` varchar(200) NOT NULL COMMENT '子标题',
`slogan` varchar(500) NULL COMMENT '宣传语（二级标题）',
`summary` varchar(1000) NULL COMMENT '摘要',
`description` varchar(1000) NULL COMMENT '简介',
`content` longtext NULL COMMENT '内容',
`weight` float(3,2) NULL DEFAULT 0 COMMENT '权重',
`longitude` double(12,8) NOT NULL COMMENT '经度',
`latitude` double(12,8) NOT NULL COMMENT '纬度',
`region_full_name` varchar(100) NOT NULL COMMENT '地区名称（格式示例：甘肃省,兰州市,城关区）',
`region_full_code` varchar(100) NOT NULL COMMENT '地区编码（格式示例：62,6201,620102）',
`region` varchar(20) NOT NULL COMMENT '所属区域',
`address` varchar(200) NOT NULL COMMENT '详细地址',
`phone` varchar(19) NULL COMMENT '联系电话',
`trading_area` varchar(100) NULL COMMENT '商圈（万达商圈，兰州中心，...）',
`type` int(11) NULL COMMENT '类型（中餐厅，川菜，...）',
`status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '审核状态（0：待审，1：通过，9：上线）',
`score` float(3,2) NULL COMMENT '评分',
`per_consumption` float(6,2) NULL COMMENT '人均消费',
`dept_code` varchar(50) NOT NULL COMMENT '组织机构编码',
`created_user` varchar(100) NULL COMMENT '创建用户',
`created_date` datetime NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
`updated_user` varchar(100) NULL COMMENT '更新用户',
`updated_date` datetime NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
PRIMARY KEY (`id`)
)
ENGINE = InnoDB
COMMENT = '周边信息表';

CREATE TABLE IF NOT EXISTS  `bic_periphery_tags` (
`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
`principal_id` bigint(20) NOT NULL COMMENT '周边ID',
`tag_catagory` varchar(20) NOT NULL COMMENT '标签分类',
`tag_name` varchar(20) NOT NULL COMMENT '标签名称',
`created_user` varchar(100) NULL COMMENT '创建用户',
`created_date` datetime NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
`updated_user` varchar(100) NULL COMMENT '更新用户',
`updated_date` datetime NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
PRIMARY KEY (`id`)
)
ENGINE = InnoDB
COMMENT = '周边标签表';

CREATE TABLE IF NOT EXISTS  `bic_extend` (
`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
`principal_id` bigint(20) NOT NULL COMMENT '关联信息ID',
`code` varchar(50) NOT NULL COMMENT '编码',
`title` varchar(200) NOT NULL COMMENT '标题',
`sub_title` varchar(200) NOT NULL COMMENT '子标题',
`slogan` varchar(500) NULL COMMENT '宣传语（二级标题）',
`summary` varchar(1000) NULL COMMENT '摘要',
`description` varchar(1000) NULL COMMENT '简介',
`content` longtext NULL COMMENT '内容',
`weight` float(3,2) NULL DEFAULT 0 COMMENT '权重',
`type` int(11) NOT NULL COMMENT '类型（1：营地特色，2：营地设施,3：菜品，4：特色项目）',
`status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '审核状态（0：待审，1：通过，9：上线）',
`price` float(8,2) NULL COMMENT '价格',
`created_user` varchar(100) NULL COMMENT '创建用户',
`created_date` datetime NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
`updated_user` varchar(100) NULL COMMENT '更新用户',
`updated_date` datetime NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
PRIMARY KEY (`id`)
)
ENGINE = InnoDB
COMMENT = '扩展表';

CREATE TABLE IF NOT EXISTS  `bic_dictionary` (
`id` bigint(20) NOT NULL AUTO_INCREMENT,
`parent_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '父ID（第一级时为0）',
`name` varchar(100) NOT NULL COMMENT '名称',
`code` varchar(100) NOT NULL COMMENT '编码',
`weight` float(3,2) NULL DEFAULT 0 COMMENT '权重',
`remark` varchar(500) NULL COMMENT '备注信息',
`created_user` varchar(100) NULL COMMENT '创建用户',
`created_date` datetime NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
`updated_user` varchar(100) NULL COMMENT '更新用户',
`updated_date` datetime NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
PRIMARY KEY (`id`)
)
ENGINE = InnoDB
COMMENT = '字典表';

CREATE TABLE IF NOT EXISTS  `bic_audit_log` (
`id` bigint(20) NOT NULL COMMENT '主键',
`principal_id` bigint(20) NOT NULL COMMENT '关联信息ID',
`pre_status` tinyint(1) NULL COMMENT '审核前状态（0：待审，1：通过，9：上线）',
`status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '审核状态（0：待审，1：通过，9：上线）',
`opinion` varchar(500) NULL COMMENT '审核意见',
`created_user` varchar(100) NULL COMMENT '创建用户',
`created_date` datetime NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
PRIMARY KEY (`id`)
)
ENGINE = InnoDB
COMMENT = '日志表';

CREATE TABLE IF NOT EXISTS  `bic_extend_tags` (
`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
`principal_id` bigint(20) NOT NULL COMMENT '扩展ID',
`tag_catagory` varchar(20) NOT NULL COMMENT '标签分类',
`tag_name` varchar(20) NOT NULL COMMENT '标签名称',
`created_user` varchar(100) NULL COMMENT '创建用户',
`created_date` datetime NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
`updated_user` varchar(100) NULL COMMENT '更新用户',
`updated_date` datetime NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
PRIMARY KEY (`id`)
)
ENGINE = InnoDB
COMMENT = '扩展标签表';

CREATE TABLE IF NOT EXISTS  `bic_destination` (
`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
`status` tinyint(1) NOT NULL COMMENT '审核状态（0：待审，1：通过，9：上线）',
`region_full_name` varchar(100) NOT NULL COMMENT '地区名称（格式示例：甘肃省,兰州市,城关区）',
`region_full_code` varchar(100) NOT NULL COMMENT '地区编码（格式示例：62,6201,620102）',
`region` varchar(20) NOT NULL COMMENT '所属区域',
`introduce` varchar(1000) NOT NULL COMMENT '目的地介绍',
`eat_introduce` varchar(1000) NULL COMMENT '美食介绍',
`drink_introduce` varchar(1000) NULL COMMENT '饮品介绍',
`play_introduce` varchar(1000) NULL COMMENT '游玩介绍',
`tourism_introduce` varchar(1000) NULL COMMENT '旅游介绍',
`shop_introduce` varchar(1000) NULL COMMENT '购物介绍',
`entertainment_introduce` varchar(1000) NULL COMMENT '娱乐介绍',
`weight` float(3,2) NULL DEFAULT 0 COMMENT '权重',
`created_user` varchar(100) NULL COMMENT '创建用户',
`created_date` datetime NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
`updated_user` varchar(100) NULL COMMENT '更新用户',
`updated_date` datetime NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
PRIMARY KEY (`id`)
)
COMMENT = '目的地表';

CREATE TABLE IF NOT EXISTS  `bic_destination_relation` (
`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
`destination_id` bigint(20) NOT NULL COMMENT '目的地主键',
`principal_id` bigint(20) NOT NULL COMMENT '关联信息ID',
`created_user` varchar(100) NULL COMMENT '创建用户',
`created_date` datetime NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建用户',
PRIMARY KEY (`id`)
)
COMMENT = '目的地关联表';

CREATE TABLE IF NOT EXISTS  `bic_entertainment_tags` (
`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
`principal_id` bigint(20) NOT NULL COMMENT '周边ID',
`tag_catagory` varchar(20) NOT NULL COMMENT '标签分类',
`tag_name` varchar(20) NOT NULL COMMENT '标签名称',
`created_user` varchar(100) NULL COMMENT '创建用户',
`created_date` datetime NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
`updated_user` varchar(100) NULL COMMENT '更新用户',
`updated_date` datetime NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
PRIMARY KEY (`id`)
)
ENGINE = InnoDB
COMMENT = '休闲娱乐标签表';
