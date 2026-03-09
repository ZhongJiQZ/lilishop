package cn.lili.modules.procurement.entity.params;

import cn.lili.common.vo.PageVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 采购单查询参数
 * 支持按单号、状态、时间区间与商品名称过滤
 * @author Bulbasaur
 * @since 2025-12-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ProcurementOrderSearchParams extends PageVO {

    @Schema(description = "商品名称")
    private String goodsName;

    @Schema(description = "单据编号")
    private String orderSn;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "生成开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startCreateTime;

    @Schema(description = "生成结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endCreateTime;

    @Schema(description = "审核开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startAuditTime;

    @Schema(description = "审核结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endAuditTime;
}
