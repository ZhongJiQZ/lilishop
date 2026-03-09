package cn.lili.modules.procurement.entity.dos;

import cn.lili.mybatis.BaseIdEntity;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 采购单明细实体
 * 记录采购的SKU、数量、价格与税额等明细项
 * @author Bulbasaur
 * @since 2025-12-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("li_procurement_order_item")
public class ProcurementOrderItem extends BaseIdEntity {

    @CreatedDate
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @Schema(description = "采购订单ID")
    private String procurementOrderId;

    @Schema(description = "商品ID")
    private String goodsId;

    @Schema(description = "SKU ID")
    private String skuId;

    @Schema(description = "商品名称")
    private String goodsName;

    @Schema(description = "零售价")
    private Double retailPrice;

    @Schema(description = "采购数量")
    private Integer quantity;

    @Schema(description = "税率(百分比整数)")
    private Integer taxRate;

    @Schema(description = "含税单价")
    private Double unitPriceWithTax;

    @Schema(description = "不含税单价")
    private Double unitPriceWithoutTax;

    @Schema(description = "不含税小计")
    private Double subtotalWithoutTax;

    @Schema(description = "含税小计")
    private Double subtotalWithTax;

    @Schema(description = "已入库数量")
    private Integer receivedQuantity;
}
