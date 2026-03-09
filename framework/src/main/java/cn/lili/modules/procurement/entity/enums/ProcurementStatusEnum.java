package cn.lili.modules.procurement.entity.enums;

/**
 * 采购单状态枚举
 * 定义采购单在提交、入库、完成等生命周期阶段
 * @author Bulbasaur
 * @since 2025-12-18
 */
public enum ProcurementStatusEnum {
    DRAFT("待提交"),
    SUBMITTED("已提交"),
    PENDING_INBOUND("待入库"),
    PARTIAL_INBOUND("部分入库"),
    CLOSED("已关闭"),
    COMPLETED("已完成"),
    REJECTED("已拒绝");

    private final String description;

    ProcurementStatusEnum(String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }
}
