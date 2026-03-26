package cn.lili.modules.member.entity.enums;

/**
 * 平台币类型枚举
 *
 * @author lensing
 * @since 2026/3/25 10:44
 */

public enum CoinTypeEnum {
    /**
     * 增加
     */
    INCREASE("增加"),
    /**
     * 减少
     */
    REDUCE("减少");

    private String description;

    public String description() {
        return description;
    }

    CoinTypeEnum(String description) {
        this.description = description;
    }
}
