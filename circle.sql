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