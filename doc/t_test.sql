/*
 Navicat Premium Data Transfer

 Source Server         : 公司mysql-新
 Source Server Type    : MySQL
 Source Server Version : 50739
 Source Host           : 192.168.0.44:3306
 Source Schema         : t_test

 Target Server Type    : MySQL
 Target Server Version : 50739
 File Encoding         : 65001

 Date: 18/04/2023 11:36:32
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', '张三');
INSERT INTO `sys_user` VALUES ('2', '李四');
INSERT INTO `sys_user` VALUES ('3', '王五');
INSERT INTO `sys_user` VALUES ('4', '赵六');

SET FOREIGN_KEY_CHECKS = 1;
