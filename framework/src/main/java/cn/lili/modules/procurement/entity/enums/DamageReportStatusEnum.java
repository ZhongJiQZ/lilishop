package cn.lili.modules.procurement.entity.enums;

/**
 * 报损单状态枚举，定义报损单生命周期各阶段
 * @author Bulbasaur
 * @since 2025-12-18
 */
public enum DamageReportStatusEnum {
    DRAFT("待提交"),
    SUBMITTED("待审核"),
    APPROVED("已通过"),
    REJECTED("已驳回"),
    CANCELLED("已作废"),
    COMPLETED("已完成");

    private final String description;

    DamageReportStatusEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
