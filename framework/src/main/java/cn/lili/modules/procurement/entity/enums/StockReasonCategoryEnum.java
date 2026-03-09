package cn.lili.modules.procurement.entity.enums;

/**
 * 出入库原因类别枚举
 * 标记入库、出库、报损等场景
 * @author Bulbasaur
 * @since 2025-12-18
 */
public enum StockReasonCategoryEnum {
    INBOUND("入库"),
    OUTBOUND("出库"),
    DAMAGE("报损");

    private final String description;

    StockReasonCategoryEnum(String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }
}
