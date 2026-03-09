package cn.lili.modules.procurement.entity.dos;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 盘点单实体，记录店铺在某一时刻的库存快照
 * @author Bulbasaur
 * @since 2025-12-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("li_inventory_count")
public class InventoryCount extends BaseEntity {

    @Schema(description = "盘点单号")
    private String sn;

    @Schema(description = "店铺ID")
    private String storeId;

    @Schema(description = "制单人ID")
    private String makerId;

    @Schema(description = "制单人名称")
    private String makerName;

    @Schema(description = "商品总数")
    private Integer itemTotal;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "盘点时间")
    private Date countTime;
}
