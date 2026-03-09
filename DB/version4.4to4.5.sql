SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `li_category_parameter`;
CREATE TABLE `li_category_parameter` (
  `id` bigint NOT NULL COMMENT 'ID',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '创建者',
  `create_time` datetime(6) DEFAULT NULL COMMENT '创建时间',
  `delete_flag` bit(1) DEFAULT NULL COMMENT '删除标志 true/false 删除/未删除',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(6) DEFAULT NULL COMMENT '更新时间',
  `category_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '分类ID',
  `parameter_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '参数ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_li_category_parameter_category_parameter` (`category_id`,`parameter_id`) USING BTREE,
  KEY `idx_li_category_parameter_category_id` (`category_id`) USING BTREE,
  KEY `idx_li_category_parameter_parameter_id` (`parameter_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='分类-参数关联';

INSERT IGNORE INTO `li_category_parameter` (`id`, `category_id`, `parameter_id`)
SELECT `id`, `category_id`, `id`
FROM `li_parameters`
WHERE `category_id` IS NOT NULL;

ALTER TABLE `li_parameters` DROP COLUMN `category_id`;

/* 添加参数组管理菜单 */
INSERT INTO `lilishop`.`li_menu` (`id`, `create_by`, `create_time`, `delete_flag`, `update_by`, `update_time`, `description`, `front_route`, `icon`, `level`, `name`, `parent_id`, `path`, `sort_order`, `title`, `front_component`, `permission`) VALUES (2002568857217798145, 'admin', '2025-12-21 10:36:36', b'0', 'admin', '2025-12-21 13:27:39', NULL, 'goods/goods-manage/parameter', NULL, 2, 'goods-parameter-group', '1367044657296441344', 'goods-parameter-group', 4.00, '参数列表', NULL, '');

