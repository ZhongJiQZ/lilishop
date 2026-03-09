package cn.lili.modules.system.entity.vo;

import cn.lili.mybatis.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 物流公司设置
 *
 * @author Chopper
 * @since 2020/11/17 8:01 下午
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "物流公司VO")
public class StoreLogisticsVO extends BaseEntity {

    @Schema(description = "物流公司ID")
    private String logisticsId;

    @Schema(description = "物流公司名称")
    private String name;

    @Schema(description = "已选择")
    private String selected;

    @Schema(description = "是否使用电子面单")
    private Boolean faceSheetFlag;
}