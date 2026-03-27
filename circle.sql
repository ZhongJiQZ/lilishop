-- 1. 圈子帖子主表
CREATE TABLE li_circle_post
(
    id            BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    user_id       BIGINT  NOT NULL COMMENT '发帖人ID',
    user_type     VARCHAR(50) NOT NULL DEFAULT NULL COMMENT '发帖人类型：STORE=商家 MEMBER=普通用户',
    content       TEXT COMMENT '帖子文字内容',
    images        JSON COMMENT '图片列表 JSON 数组，例如 ["url1","url2"]',
    goods_ids     JSON COMMENT '关联商品ID列表（可选）',
    like_count    INT UNSIGNED DEFAULT 0 COMMENT '点赞数',
    comment_count INT UNSIGNED DEFAULT 0 COMMENT '评论数',
    view_count    INT UNSIGNED DEFAULT 0 COMMENT '浏览数',
    status        TINYINT          DEFAULT 1 COMMENT '状态：1=正常 0=删除/隐藏',

    -- 公共字段
    create_by     VARCHAR(255)     DEFAULT NULL COMMENT '创建者',
    create_time   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间',
    delete_flag   BIT(1)           DEFAULT b'0' COMMENT '删除标志（0未删 1已删）',
    update_by     VARCHAR(255)     DEFAULT NULL COMMENT '更新者',
    update_time   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '更新时间',

    INDEX         idx_user_id (user_id),
    INDEX         idx_create_time (create_time DESC),
    INDEX         idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='圈子帖子表';


-- 2. 帖子点赞记录表
CREATE TABLE li_circle_post_like
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    post_id     BIGINT NOT NULL,
    user_id     BIGINT NOT NULL,
    create_time DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6),

-- 公共字段（点赞记录一般不需要 update_by/update_time，可选保留）
    create_by   VARCHAR(255) DEFAULT NULL COMMENT '创建者',
    delete_flag BIT(1)       DEFAULT b'0' COMMENT '删除标志（0未删 1已删）',
    update_time DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '更新时间',

    UNIQUE KEY uk_post_user (post_id, user_id),
    INDEX       idx_post_id (post_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='圈子帖子点赞记录';


-- 3. 帖子评论表
CREATE TABLE li_circle_post_comment
(
    id            BIGINT PRIMARY KEY AUTO_INCREMENT,
    post_id       BIGINT  NOT NULL COMMENT '帖子ID',
    user_id       BIGINT  NOT NULL COMMENT '评论人ID',
    user_type     VARCHAR(50) NOT NULL DEFAULT NULL COMMENT '评论人类型：STORE=商家 MEMBER=普通用户',
    content       TEXT    NOT NULL COMMENT '评论内容',
    parent_id     BIGINT           DEFAULT 0 COMMENT '父评论ID，0表示一级评论',
    reply_user_id BIGINT           DEFAULT 0 COMMENT '被回复的用户ID',
    like_count    INT UNSIGNED DEFAULT 0 COMMENT '点赞数',
    status        TINYINT          DEFAULT 1 COMMENT '状态：1=正常 0=删除',

-- 公共字段
    create_by     VARCHAR(255)     DEFAULT NULL COMMENT '创建者',
    create_time   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间',
    delete_flag   BIT(1)           DEFAULT b'0' COMMENT '删除标志（0未删 1已删）',
    update_by     VARCHAR(255)     DEFAULT NULL COMMENT '更新者',
    update_time   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '更新时间',

    INDEX         idx_post_id (post_id),
    INDEX         idx_parent_id (parent_id),
    INDEX         idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='圈子帖子评论表';


-- 4. 帖子浏览记录表
CREATE TABLE li_circle_post_view_record
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    post_id     BIGINT NOT NULL,
    user_id     BIGINT       DEFAULT 0 COMMENT '浏览用户ID，0=游客',
    ip          VARCHAR(45)  DEFAULT '' COMMENT 'IP地址',

-- 公共字段（浏览记录一般只记录创建时间）
    create_by   VARCHAR(255) DEFAULT NULL COMMENT '创建者',
    create_time DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间',
    delete_flag BIT(1)       DEFAULT b'0' COMMENT '删除标志（0未删 1已删）',

    UNIQUE KEY uk_post_user_ip (post_id, user_id, ip(20)),
    INDEX       idx_post_id (post_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='圈子帖子浏览记录';


-- 5. 帖子关联商品表
CREATE TABLE li_circle_post_goods
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    post_id     BIGINT NOT NULL,
    goods_id    BIGINT NOT NULL,
    sku_id      BIGINT       DEFAULT 0 COMMENT '具体SKU（可选）',
    sort        INT          DEFAULT 0 COMMENT '排序',

-- 公共字段（关联表一般不需要 update_by/update_time，可选保留）
    create_by   VARCHAR(255) DEFAULT NULL COMMENT '创建者',
    create_time DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间',
    delete_flag BIT(1)       DEFAULT b'0' COMMENT '删除标志（0未删 1已删）',
    update_time DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '更新时间',

    UNIQUE KEY uk_post_goods (post_id, goods_id),
    INDEX       idx_post_id (post_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='圈子帖子关联商品';

--- 更新表
DROP TABLE IF EXISTS `li_member_coins_history`;
CREATE TABLE `li_member_coins_history`  (
    `id` bigint NOT NULL COMMENT 'ID',
    `create_by` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '创建者',
    `create_time` datetime(6) NULL DEFAULT NULL COMMENT '创建时间',
    `before_coin` bigint NULL DEFAULT NULL COMMENT '消费之前平台币',
    `content` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '内容',
    `member_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '会员ID',
    `member_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '会员名称',
    `coin` bigint NULL DEFAULT NULL COMMENT '当前平台币',
    `coin_type` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '消费平台币类型',
    `variable_coin` bigint NULL DEFAULT NULL COMMENT '消费平台币',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

DROP TABLE IF EXISTS `li_circle_post_follow`;
CREATE TABLE `li_circle_post_follow` (
    `id` varchar(64) NOT NULL COMMENT '主键ID',
    `member_id` varchar(64) NOT NULL COMMENT '关注者ID（当前用户）',
    `followed_member_id` varchar(64) NOT NULL COMMENT '被关注者ID（作者）',
    `create_time` datetime DEFAULT NULL COMMENT '关注时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_member_followed` (`member_id`,`followed_member_id`) USING BTREE COMMENT '唯一关注关系，防止重复关注',
    KEY `idx_followed_member_id` (`followed_member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='圈子-用户关注表';

ALTER TABLE li_circle_post
    ADD COLUMN `need_follow` tinyint(1) DEFAULT 0 COMMENT '是否需要关注作者才能查看 0=不需要 1=需要'

ALTER TABLE li_member
    ADD COLUMN coin bigint DEFAULT NULL COMMENT '平台币',
    ADD COLUMN total_coin bigint DEFAULT NULL COMMENT '平台币总数量',
    ADD COLUMN is_vip bit(1) DEFAULT b'0' COMMENT '是否是VIP会员 0：普通用户 1：会员用户',
    ADD COLUMN invite_code varchar(255) DEFAULT NULL COMMENT '【邀请码】每个会员唯一',
    ADD COLUMN invite_status bit(1) DEFAULT b'0' COMMENT '邀请码状态 0=未填写 1=已填写',
    ADD COLUMN inviter_id bigint DEFAULT NULL COMMENT '邀请人ID',
    ADD COLUMN inviter_code varchar(255) NULL COMMENT '邀请人Code' AFTER `inviter_id`;
    ADD COLUMN inviter_name varchar(255) DEFAULT NULL COMMENT '邀请人名称';

ALTER TABLE li_member ADD UNIQUE INDEX uk_invite_code (invite_code);

--- 新增表

CREATE TABLE `li_im_chat_gift` (
    `id` varchar(64) NOT NULL,
    `gift_name` varchar(100) NOT NULL COMMENT '礼物名称',
    `gift_image` varchar(500) NOT NULL COMMENT '礼物图标',
    `coin_price` decimal(10, 2) NOT NULL DEFAULT 0 COMMENT '价格（平台币）',
    `sort` int DEFAULT 0 COMMENT '排序',
    `status` tinyint(1) DEFAULT 1 COMMENT '状态 0=禁用 1=启用',
    `create_time` datetime DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='聊天-礼物配置表';

CREATE TABLE `li_im_chat_reward` (
    `id` varchar(64) NOT NULL COMMENT '主键ID',
    `gift_id` varchar(64) NOT NULL COMMENT '礼物ID',
    `gift_name` varchar(100) NOT NULL COMMENT '礼物名称',
    `gift_image` varchar(500) NOT NULL COMMENT '礼物图片',
    `coin_price` decimal(10, 2) NOT NULL COMMENT '礼物单价（平台币）',
    `num` int NOT NULL DEFAULT 1 COMMENT '礼物数量',
    `total_coin` decimal(10, 2) NOT NULL COMMENT '本次打赏总消耗平台币',
    `from_member_id` varchar(64) NOT NULL COMMENT '打赏人ID（用户）',
    `from_member_name` varchar(255) DEFAULT NULL COMMENT '打赏人名称',
    `from_member_avatar` varchar(500) DEFAULT NULL COMMENT '打赏人头像',
    `to_member_id` varchar(64) NOT NULL COMMENT '被打赏人ID（试穿员）',
    `to_member_name` varchar(255) DEFAULT NULL COMMENT '被打赏人名称',
    `to_member_avatar` varchar(500) DEFAULT NULL COMMENT '被打赏人头像',
    `create_time` datetime DEFAULT NULL COMMENT '打赏时间',
    PRIMARY KEY (`id`),
    KEY `idx_from_member_id` (`from_member_id`),
    KEY `idx_to_member_id` (`to_member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='聊天-打赏记录表';

CREATE TABLE `li_im_member_income` (
    `id` varchar(64) NOT NULL COMMENT '主键ID',
    `member_id` varchar(64) NOT NULL COMMENT '试穿员会员ID',
    `member_name` varchar(255) DEFAULT NULL COMMENT '试穿员名称',
    `total_income` decimal(10, 2) DEFAULT 0 COMMENT '总收益（平台币）',
    `today_income` decimal(10, 2) DEFAULT 0 COMMENT '今日收益（平台币）',
    `update_time` datetime DEFAULT NULL COMMENT '最后更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_member_id` (`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会员-试穿员收益表';