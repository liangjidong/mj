/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50541
Source Host           : localhost:3306
Source Database       : mj

Target Server Type    : MYSQL
Target Server Version : 50541
File Encoding         : 65001

Date: 2015-06-04 01:37:28
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for group
-- ----------------------------
DROP TABLE IF EXISTS `group`;
CREATE TABLE `group` (
  `id` int(11) NOT NULL COMMENT '用户组id',
  `group_name` varchar(100) DEFAULT NULL COMMENT '用户组名称',
  `permission` int(11) DEFAULT NULL COMMENT '用户组权限（用相应数字对应相关权限）',
  `status` int(11) DEFAULT NULL COMMENT '用户组状态',
  `keepword1` varchar(100) DEFAULT NULL,
  `keepword2` varchar(100) DEFAULT NULL,
  `keepword3` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of group
-- ----------------------------

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` int(11) DEFAULT NULL COMMENT '信息类型',
  `detail` varchar(100) DEFAULT NULL COMMENT '信息详情',
  `if_feedback` int(11) DEFAULT NULL COMMENT '消息是否需要用户反馈（0--不需要，1--需要）',
  `feedback_place` varchar(100) DEFAULT NULL COMMENT '用户反馈到的位置',
  `related_service` varchar(100) DEFAULT NULL COMMENT '相关业务',
  `status` int(11) DEFAULT NULL COMMENT '信息状态',
  `keepword1` varchar(100) DEFAULT NULL COMMENT '保留关键字',
  `keepword2` varchar(100) DEFAULT NULL COMMENT '保留关键字',
  `keepword3` varchar(100) DEFAULT NULL COMMENT '保留关键字',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of message
-- ----------------------------

-- ----------------------------
-- Table structure for mjproduct
-- ----------------------------
DROP TABLE IF EXISTS `mjproduct`;
CREATE TABLE `mjproduct` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '产品id',
  `picture_id` int(11) DEFAULT NULL COMMENT '图片id',
  `video_id` int(11) DEFAULT NULL COMMENT '视频id',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `industry` varchar(100) DEFAULT NULL COMMENT '所属行业',
  `is_private` int(11) DEFAULT NULL COMMENT '是否私有（0--非私有，若为私有，该值为私有者的id）',
  `fetch_code` varchar(100) DEFAULT NULL COMMENT '产品提取码',
  `production_phase` int(11) DEFAULT NULL COMMENT '制作阶段（1--上传，3--审核，5--发布）',
  `status` int(11) DEFAULT NULL COMMENT '产品状态',
  `make_time` timestamp NULL DEFAULT NULL COMMENT '产品制作时间',
  `publish_time` timestamp NULL DEFAULT NULL COMMENT '发布时间',
  `description` varchar(100) DEFAULT NULL COMMENT '产品描述',
  `keepword1` varchar(100) DEFAULT NULL COMMENT '保留关键字',
  `keepword2` varchar(100) DEFAULT NULL COMMENT '保留关键字',
  `keepword3` varchar(100) DEFAULT NULL COMMENT '保留关键字',
  PRIMARY KEY (`id`),
  KEY `fk_mjproduct_picture_1` (`picture_id`),
  KEY `fk_mjproduct_video_1` (`video_id`),
  KEY `fk_mjproduct_user_1` (`user_id`),
  CONSTRAINT `fk_mjproduct_picture_1` FOREIGN KEY (`picture_id`) REFERENCES `picture` (`id`),
  CONSTRAINT `fk_mjproduct_user_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_mjproduct_video_1` FOREIGN KEY (`video_id`) REFERENCES `video` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of mjproduct
-- ----------------------------

-- ----------------------------
-- Table structure for picture
-- ----------------------------
DROP TABLE IF EXISTS `picture`;
CREATE TABLE `picture` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `video_id` int(11) DEFAULT NULL COMMENT '关联视频id',
  `belong_user_id` int(11) DEFAULT NULL COMMENT '图片归属用户id(若id=0则代表图片归属用户组；不为0则为归属用户id)',
  `belong_group_id` int(11) DEFAULT NULL COMMENT '图片归属用户id(若id=0则代表图片归属用户组；不为0则为归属用户id)',
  `name` varchar(100) DEFAULT NULL COMMENT '图片名',
  `size` varchar(100) DEFAULT NULL COMMENT '图片大小',
  `quality` int(11) DEFAULT NULL COMMENT '图片质量（是否可用）',
  `similitude` int(11) DEFAULT NULL COMMENT '图片相似性',
  `url` varchar(100) DEFAULT NULL COMMENT '图片路径',
  `type` varchar(100) DEFAULT NULL COMMENT '图片类型',
  `status` int(11) DEFAULT NULL COMMENT '图片状态',
  `keepword1` varchar(100) DEFAULT NULL COMMENT '保留关键字',
  `keepword2` varchar(100) DEFAULT NULL COMMENT '保留关键字',
  `keepword3` varchar(100) DEFAULT NULL COMMENT '保留关键字',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of picture
-- ----------------------------
INSERT INTO `picture` VALUES ('9', null, null, null, 'picture_9.jpg', '4.76K', null, null, 'upload/picture/picture_9.jpg', 'jpg', null, null, null, null);
INSERT INTO `picture` VALUES ('12', null, null, null, 'picture_12.jpg', '117.02K', null, null, 'upload/picture/picture_12.jpg', 'jpg', null, null, null, null);

-- ----------------------------
-- Table structure for picture_group_share
-- ----------------------------
DROP TABLE IF EXISTS `picture_group_share`;
CREATE TABLE `picture_group_share` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '图片用户组分享关系id',
  `picture_id` int(11) DEFAULT NULL COMMENT '图片id',
  `group_id` int(11) DEFAULT NULL COMMENT '用户组id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of picture_group_share
-- ----------------------------

-- ----------------------------
-- Table structure for picture_user_share
-- ----------------------------
DROP TABLE IF EXISTS `picture_user_share`;
CREATE TABLE `picture_user_share` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '图片用户分享关系id',
  `picture_id` int(11) DEFAULT NULL COMMENT '图片id',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of picture_user_share
-- ----------------------------

-- ----------------------------
-- Table structure for picture_video
-- ----------------------------
DROP TABLE IF EXISTS `picture_video`;
CREATE TABLE `picture_video` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `picture_id` int(11) DEFAULT NULL COMMENT '图片id',
  `video_id` int(11) DEFAULT NULL COMMENT '视频id',
  `status` int(11) DEFAULT NULL COMMENT '状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of picture_video
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group_id` int(11) DEFAULT NULL COMMENT '用户组id',
  `phone_number` varchar(100) DEFAULT NULL COMMENT '电话号码',
  `nick_name` varchar(100) DEFAULT NULL COMMENT '昵称',
  `name` varchar(100) DEFAULT NULL COMMENT '姓名',
  `adress` varchar(100) DEFAULT NULL COMMENT '地址',
  `password` varchar(100) DEFAULT NULL COMMENT '密码',
  `role` int(11) DEFAULT NULL COMMENT '角色（不同数字对应不同类型的用户，具体要看总共有多少种类型的用户）',
  `status` int(11) DEFAULT NULL COMMENT '状态',
  `payment_information` varchar(100) DEFAULT NULL COMMENT '用户支付相关信息',
  `qq_number` varchar(100) DEFAULT NULL COMMENT '用户qq号',
  `keepword1` varchar(100) DEFAULT NULL COMMENT '保留关键字',
  `keepword2` varchar(100) DEFAULT NULL COMMENT '保留关键字',
  `email` varchar(100) DEFAULT NULL COMMENT '用户邮箱',
  `keepword3` varchar(100) DEFAULT NULL COMMENT '保留关键字',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', null, null, 'addf', 'test2', null, 'e10adc3949ba59abbe56e057f20f883e', null, null, null, null, null, null, null, null);
INSERT INTO `user` VALUES ('2', null, null, 'update', 'test23', null, 'e10adc3949ba59abbe56e057f20f883e', null, null, null, null, null, null, null, null);
INSERT INTO `user` VALUES ('3', null, null, 'addf', 'test2', null, 'e10adc3949ba59abbe56e057f20f883e', null, null, null, null, null, null, null, null);
INSERT INTO `user` VALUES ('4', null, null, 'addf', 'test223', null, 'e10adc3949ba59abbe56e057f20f883e', null, null, null, null, null, null, null, null);
INSERT INTO `user` VALUES ('5', null, null, 'addf', 'test223', null, 'e10adc3949ba59abbe56e057f20f883e', null, null, null, null, null, null, null, null);
INSERT INTO `user` VALUES ('6', null, null, null, 'test', null, 'e10adc3949ba59abbe56e057f20f883e', null, null, null, null, null, null, null, null);

-- ----------------------------
-- Table structure for video
-- ----------------------------
DROP TABLE IF EXISTS `video`;
CREATE TABLE `video` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL COMMENT '视频名字',
  `size` varchar(100) DEFAULT NULL COMMENT '视频大小',
  `origin_url` varchar(100) DEFAULT NULL COMMENT '源视频路径',
  `final_url` varchar(100) DEFAULT NULL COMMENT '最终视频路径',
  `video_format` varchar(100) DEFAULT NULL COMMENT '视频格式',
  `audio_format` varchar(255) DEFAULT NULL COMMENT '音频格式',
  `upload_way` varchar(100) DEFAULT NULL COMMENT '上传途径',
  `code_rate` varchar(100) DEFAULT NULL COMMENT '码率',
  `duration` varchar(100) DEFAULT NULL COMMENT '视频时长',
  `status` int(11) DEFAULT NULL COMMENT '视频状态',
  `keepword1` varchar(100) DEFAULT NULL COMMENT '保留关键字',
  `keepword2` varchar(100) DEFAULT NULL COMMENT '保留关键字',
  `keepword3` varchar(100) DEFAULT NULL COMMENT '保留关键字',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of video
-- ----------------------------
INSERT INTO `video` VALUES ('1', null, '5.94M', null, null, null, 'avi', null, null, null, null, null, null, null);
