package cn.lili.modules.procurement.entity.dos;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 采购入库单实体
 * 记录入库单基础信息、数量统计与操作人信息
 * @author Bulbasaur
 * @since 2025-12-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("li_procurement_inbound")
public class ProcurementInbound extends BaseEntity {

    @Schema(description = "入库单号")
    private String inboundSn;

    @Schema(description = "店铺ID")
    private String storeId;

    @Schema(description = "采购订单ID")
    private String procurementOrderId;

    @Schema(description = "预计入库量")
    private Integer expectedQuantity;

    @Schema(description = "已确认入库量")
    private Integer confirmedQuantity;

    @Schema(description = "待确认入库量")
    private Integer pendingQuantity;

    @Schema(description = "合计入库成本")
    private Double totalCost;

    @Schema(description = "合计零售金额")
    private Double totalRetailAmount;

    @Schema(description = "入库时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date inboundTime;

    @Schema(description = "入库凭证")
    private String certificateImages;

    @Schema(description = "制单人ID")
    private String operatorId;

    @Schema(description = "制单人")
    private String operatorName;
}
