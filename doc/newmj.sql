/*
Navicat MySQL Data Transfer

Source Server         : 182
Source Server Version : 50544
Source Host           : localhost:3306
Source Database       : newmj

Target Server Type    : MYSQL
Target Server Version : 50544
File Encoding         : 65001

Date: 2015-10-29 10:21:55
*/
drop database if exists newmj;
create database newmj;
use newmj;
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for group
-- ----------------------------
DROP TABLE IF EXISTS `group`;
CREATE TABLE `group` (
  `id` int(11) NOT NULL COMMENT '鐢ㄦ埛缁刬d',
  `group_name` varchar(100) DEFAULT NULL COMMENT '鐢ㄦ埛缁勫悕绉',
  `permission` int(11) DEFAULT NULL COMMENT '鐢ㄦ埛缁勬潈闄愶紙鐢ㄧ浉搴旀暟瀛楀?搴旂浉鍏虫潈闄愶級',
  `status` int(11) DEFAULT NULL COMMENT '鐢ㄦ埛缁勭姸鎬',
  `keepword1` varchar(100) DEFAULT NULL,
  `keepword2` varchar(100) DEFAULT NULL,
  `keepword3` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for log_history
-- ----------------------------
DROP TABLE IF EXISTS `log_history`;
CREATE TABLE `log_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `operation_type` varchar(100) NOT NULL DEFAULT '',
  `operaton_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `operation_content` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=174 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` int(11) DEFAULT NULL COMMENT '淇℃伅绫诲瀷',
  `detail` varchar(100) DEFAULT NULL COMMENT '淇℃伅璇︽儏',
  `if_feedback` int(11) DEFAULT NULL COMMENT '娑堟伅鏄?惁闇??鐢ㄦ埛鍙嶉?锛?--涓嶉渶瑕侊紝1--闇??锛',
  `feedback_place` varchar(100) DEFAULT NULL COMMENT '鐢ㄦ埛鍙嶉?鍒扮殑浣嶇疆',
  `related_service` varchar(100) DEFAULT NULL COMMENT '鐩稿叧涓氬姟',
  `status` int(11) DEFAULT NULL COMMENT '淇℃伅鐘舵?',
  `keepword1` varchar(100) DEFAULT NULL COMMENT '淇濈暀鍏抽敭瀛',
  `keepword2` varchar(100) DEFAULT NULL COMMENT '淇濈暀鍏抽敭瀛',
  `keepword3` varchar(100) DEFAULT NULL COMMENT '淇濈暀鍏抽敭瀛',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for mjproduct
-- ----------------------------
DROP TABLE IF EXISTS `mjproduct`;
CREATE TABLE `mjproduct` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '浜у搧id',
  `picture_id` int(11) DEFAULT NULL COMMENT '鍥剧墖id',
  `video_id` int(11) DEFAULT NULL COMMENT '瑙嗛?id',
  `user_id` int(11) DEFAULT NULL COMMENT '鐢ㄦ埛id',
  `industry` varchar(100) CHARACTER SET latin1 DEFAULT NULL COMMENT '鎵?睘琛屼笟',
  `is_private` int(11) DEFAULT NULL COMMENT '鏄?惁绉佹湁锛?--闈炵?鏈夛紝鑻ヤ负绉佹湁锛岃?鍊间负绉佹湁鑰呯殑id锛',
  `fetch_code` varchar(100) CHARACTER SET latin1 DEFAULT NULL COMMENT '浜у搧鎻愬彇鐮',
  `production_phase` int(11) DEFAULT NULL COMMENT '鍒朵綔闃舵?锛?--涓婁紶锛?--瀹℃牳锛?--鍙戝竷锛',
  `status` int(11) DEFAULT '1' COMMENT '0:trained  1: not trained',
  `make_time` timestamp NULL DEFAULT NULL COMMENT '浜у搧鍒朵綔鏃堕棿',
  `publish_time` timestamp NULL DEFAULT NULL COMMENT '鍙戝竷鏃堕棿',
  `description` varchar(100) CHARACTER SET latin1 DEFAULT NULL COMMENT '浜у搧鎻忚堪',
  `keepword1` varchar(100) CHARACTER SET latin1 DEFAULT NULL COMMENT '淇濈暀鍏抽敭瀛',
  `keepword2` varchar(100) CHARACTER SET latin1 DEFAULT NULL COMMENT '淇濈暀鍏抽敭瀛',
  `keepword3` varchar(100) CHARACTER SET latin1 DEFAULT NULL COMMENT '淇濈暀鍏抽敭瀛',
  `title` varchar(50) CHARACTER SET latin1 DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `clicktimes` int(11) DEFAULT '0' COMMENT 'the video of product clicked',
  `threed_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_mjproduct_picture_1` (`picture_id`),
  KEY `fk_mjproduct_video_1` (`video_id`),
  KEY `fk_mjproduct_user_1` (`user_id`),
  CONSTRAINT `fk_mjproduct_user_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_mjproduct_video_1` FOREIGN KEY (`video_id`) REFERENCES `video` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=152 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for picture
-- ----------------------------
DROP TABLE IF EXISTS `picture`;
CREATE TABLE `picture` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL COMMENT '鎵?睘鐢ㄦ埛id',
  `video_id` int(11) DEFAULT NULL COMMENT '鍏宠仈瑙嗛?id',
  `belong_user_id` int(11) DEFAULT NULL COMMENT '鍥剧墖褰掑睘鐢ㄦ埛id(鑻?d=0鍒欎唬琛ㄥ浘鐗囧綊灞炵敤鎴风粍锛涗笉涓?鍒欎负褰掑睘鐢ㄦ埛id)',
  `belong_group_id` int(11) DEFAULT NULL COMMENT '鍥剧墖褰掑睘鐢ㄦ埛id(鑻?d=0鍒欎唬琛ㄥ浘鐗囧綊灞炵敤鎴风粍锛涗笉涓?鍒欎负褰掑睘鐢ㄦ埛id)',
  `name` varchar(100) DEFAULT NULL COMMENT '鍥剧墖鍚',
  `size` varchar(100) DEFAULT NULL COMMENT '鍥剧墖澶у皬',
  `quality` int(11) DEFAULT NULL COMMENT '鍥剧墖璐ㄩ噺锛堟槸鍚﹀彲鐢?級',
  `similitude` int(11) DEFAULT NULL COMMENT '鍥剧墖鐩镐技鎬',
  `url` varchar(100) DEFAULT NULL COMMENT '鍥剧墖璺?緞',
  `type` varchar(100) DEFAULT NULL COMMENT '鍥剧墖绫诲瀷',
  `status` int(11) DEFAULT NULL COMMENT '鍥剧墖鐘舵?',
  `keepword1` varchar(100) DEFAULT NULL COMMENT '淇濈暀鍏抽敭瀛',
  `keepword2` varchar(100) DEFAULT NULL COMMENT '淇濈暀鍏抽敭瀛',
  `keepword3` varchar(100) DEFAULT NULL COMMENT '淇濈暀鍏抽敭瀛',
  `createtime` datetime DEFAULT NULL,
  `threed_id` int(11) DEFAULT NULL,
  `reality_type` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=826 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for picture_group_share
-- ----------------------------
DROP TABLE IF EXISTS `picture_group_share`;
CREATE TABLE `picture_group_share` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '鍥剧墖鐢ㄦ埛缁勫垎浜?叧绯籭d',
  `picture_id` int(11) DEFAULT NULL COMMENT '鍥剧墖id',
  `group_id` int(11) DEFAULT NULL COMMENT '鐢ㄦ埛缁刬d',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for picture_user_share
-- ----------------------------
DROP TABLE IF EXISTS `picture_user_share`;
CREATE TABLE `picture_user_share` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '鍥剧墖鐢ㄦ埛鍒嗕韩鍏崇郴id',
  `picture_id` int(11) DEFAULT NULL COMMENT '鍥剧墖id',
  `user_id` int(11) DEFAULT NULL COMMENT '鐢ㄦ埛id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for picture_video
-- ----------------------------
DROP TABLE IF EXISTS `picture_video`;
CREATE TABLE `picture_video` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `picture_id` int(11) DEFAULT NULL COMMENT '鍥剧墖id',
  `video_id` int(11) DEFAULT NULL COMMENT '瑙嗛?id',
  `status` int(11) DEFAULT NULL COMMENT '鐘舵?',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for three_d
-- ----------------------------
DROP TABLE IF EXISTS `three_d`;
CREATE TABLE `three_d` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `file_size` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `url` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group_id` int(11) DEFAULT NULL COMMENT '鐢ㄦ埛缁刬d',
  `head_img` varchar(255) DEFAULT NULL COMMENT '鐢ㄦ埛澶村儚璺?緞',
  `phone_number` varchar(100) DEFAULT NULL COMMENT '鐢佃瘽鍙风爜',
  `nick_name` varchar(100) DEFAULT NULL COMMENT '鏄电О',
  `name` varchar(100) DEFAULT NULL COMMENT '濮撳悕',
  `adress` varchar(100) DEFAULT NULL COMMENT '鍦板潃',
  `password` varchar(100) DEFAULT NULL COMMENT '瀵嗙爜',
  `role` int(11) DEFAULT NULL COMMENT '瑙掕壊锛堜笉鍚屾暟瀛楀?搴斾笉鍚岀被鍨嬬殑鐢ㄦ埛锛屽叿浣撹?鐪嬫?鍏辨湁澶氬皯绉嶇被鍨嬬殑鐢ㄦ埛锛',
  `status` int(11) DEFAULT NULL COMMENT '鐘舵?',
  `payment_information` varchar(100) DEFAULT NULL COMMENT '鐢ㄦ埛鏀?粯鐩稿叧淇℃伅',
  `qq_number` varchar(100) DEFAULT NULL COMMENT '鐢ㄦ埛qq鍙',
  `keepword1` varchar(100) DEFAULT NULL COMMENT '淇濈暀鍏抽敭瀛',
  `keepword2` varchar(100) DEFAULT NULL COMMENT '淇濈暀鍏抽敭瀛',
  `email` varchar(100) DEFAULT NULL COMMENT '鐢ㄦ埛閭??',
  `keepword3` varchar(100) DEFAULT NULL COMMENT '淇濈暀鍏抽敭瀛',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for video
-- ----------------------------
DROP TABLE IF EXISTS `video`;
CREATE TABLE `video` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL COMMENT '鎵?睘鐢ㄦ埛id',
  `name` varchar(100) CHARACTER SET gbk DEFAULT NULL COMMENT '瑙嗛?鍚嶅瓧',
  `size` varchar(100) CHARACTER SET gbk DEFAULT NULL COMMENT '瑙嗛?澶у皬',
  `origin_url` varchar(100) CHARACTER SET gbk DEFAULT NULL COMMENT '婧愯?棰戣矾寰',
  `final_url` varchar(100) CHARACTER SET gbk DEFAULT NULL COMMENT '鏈?粓瑙嗛?璺?緞',
  `video_format` varchar(100) CHARACTER SET gbk DEFAULT NULL COMMENT '瑙嗛?鏍煎紡',
  `audio_format` varchar(255) CHARACTER SET gbk DEFAULT NULL COMMENT '闊抽?鏍煎紡',
  `upload_way` varchar(100) CHARACTER SET gbk DEFAULT NULL COMMENT '涓婁紶閫斿緞',
  `code_rate` varchar(100) CHARACTER SET gbk DEFAULT NULL COMMENT '鐮佺巼',
  `duration` varchar(100) CHARACTER SET gbk DEFAULT NULL COMMENT '瑙嗛?鏃堕暱',
  `status` int(11) DEFAULT NULL COMMENT '瑙嗛?鐘舵?',
  `keepword1` varchar(100) DEFAULT NULL COMMENT '淇濈暀鍏抽敭瀛',
  `keepword2` varchar(100) DEFAULT NULL COMMENT '淇濈暀鍏抽敭瀛',
  `keepword3` varchar(100) DEFAULT NULL COMMENT '淇濈暀鍏抽敭瀛',
  `createtime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=311 DEFAULT CHARSET=latin1;
