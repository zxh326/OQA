/*
 Navicat Premium Data Transfer

 Source Server         : local
 Source Server Type    : MySQL
 Source Server Version : 50724
 Source Host           : localhost:3306
 Source Schema         : oqa

 Target Server Type    : MySQL
 Target Server Version : 50724
 File Encoding         : 65001

 Date: 27/12/2018 22:20:28
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for gmessage
-- ----------------------------
DROP TABLE IF EXISTS `gmessage`;
CREATE TABLE `gmessage`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fromId` int(255) NULL DEFAULT NULL,
  `groupId` int(11) NULL DEFAULT NULL,
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `guser`(`fromId`) USING BTREE,
  INDEX `group`(`groupId`) USING BTREE,
  CONSTRAINT `group` FOREIGN KEY (`groupId`) REFERENCES `ogroup` (`groupId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `guser` FOREIGN KEY (`fromId`) REFERENCES `user` (`userId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of gmessage
-- ----------------------------
INSERT INTO `gmessage` VALUES (1, 4, 1, '123');
INSERT INTO `gmessage` VALUES (2, 5, 1, '456');
INSERT INTO `gmessage` VALUES (3, 4, 1, '123123');
INSERT INTO `gmessage` VALUES (4, 4, 1, '消息记录保存');
INSERT INTO `gmessage` VALUES (5, 4, 1, '000');
INSERT INTO `gmessage` VALUES (6, 4, 3, '908908');
INSERT INTO `gmessage` VALUES (7, 4, 1, 'hahhaha');

-- ----------------------------
-- Table structure for group_users
-- ----------------------------
DROP TABLE IF EXISTS `group_users`;
CREATE TABLE `group_users`  (
  `groupId` int(11) NOT NULL,
  `UserId` int(11) NOT NULL,
  PRIMARY KEY (`groupId`, `UserId`) USING BTREE,
  INDEX `2`(`UserId`) USING BTREE,
  CONSTRAINT `1` FOREIGN KEY (`groupId`) REFERENCES `ogroup` (`groupId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `2` FOREIGN KEY (`UserId`) REFERENCES `user` (`userId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of group_users
-- ----------------------------
INSERT INTO `group_users` VALUES (1, 4);
INSERT INTO `group_users` VALUES (3, 4);
INSERT INTO `group_users` VALUES (1, 5);
INSERT INTO `group_users` VALUES (3, 6);

-- ----------------------------
-- Table structure for ogroup
-- ----------------------------
DROP TABLE IF EXISTS `ogroup`;
CREATE TABLE `ogroup`  (
  `groupId` int(11) NOT NULL AUTO_INCREMENT,
  `groupName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `createTime` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`groupId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ogroup
-- ----------------------------
INSERT INTO `ogroup` VALUES (1, 'test', '2018-12-11 20:29:15');
INSERT INTO `ogroup` VALUES (2, 'test', '2018-12-11 21:28:10');
INSERT INTO `ogroup` VALUES (3, 'test', '2018-12-20 19:10:03');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `userId` int(11) NOT NULL AUTO_INCREMENT,
  `loginId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `userRole` int(11) NOT NULL COMMENT '1,学生，2，教师\r\n',
  `userPass` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `avatarUrl` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`userId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (4, 'zzde', 0, '43lbsnb4m3h1cubegd7l5lgvq2rg6cns', '/static/img/stu.jpg');
INSERT INTO `user` VALUES (5, '123', 0, '12', '/static/img/stu.jpg');
INSERT INTO `user` VALUES (6, 't1', 1, '43lbsnb4m3h1cubegd7l5lgvq2rg6cns', '/static/img/teacher.jpg');

-- ----------------------------
-- Table structure for userprofile
-- ----------------------------
DROP TABLE IF EXISTS `userprofile`;
CREATE TABLE `userprofile`  (
  `userId` int(11) NOT NULL,
  `userName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `userDepartment` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`userId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of userprofile
-- ----------------------------
INSERT INTO `userprofile` VALUES (4, 'zzzz', 'qwe');
INSERT INTO `userprofile` VALUES (6, '张三', '无');

SET FOREIGN_KEY_CHECKS = 1;
