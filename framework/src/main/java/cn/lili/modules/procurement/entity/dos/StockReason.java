package cn.lili.modules.procurement.entity.dos;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 出入库原因实体
 * 用于记录店铺的自定义出入库原因
 * @author Bulbasaur
 * @since 2025-12-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("li_stock_reason")
public class StockReason extends BaseEntity {

    @Schema(description = "出入库原因")
    private String reason;

    @Schema(description = "类别")
    private String category;
}
