package cn.lili.modules.statistics.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品统计查询参数
 *
 * @author Chopper
 * @since 2020/11/17 7:34 下午
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class GoodsStatisticsQueryParam extends StatisticsQueryParam {

    @Schema(description = "查询类型：按数量（NUM）、按金额（PRICE）")
    private String type;

}
