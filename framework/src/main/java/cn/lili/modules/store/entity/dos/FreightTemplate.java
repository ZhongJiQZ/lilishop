package cn.lili.modules.store.entity.dos;

import cn.lili.modules.store.entity.enums.FreightTemplateEnum;
import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 运费模板
 *
 * @author Chopper
 * @since 2020/11/17 4:27 下午
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("li_freight_template")
@Schema(description = "运费模板")
public class FreightTemplate extends BaseEntity {

    @Schema(description = "店铺ID", hidden = true)
    private String storeId;

    @NotEmpty(message = "模板名称不能为空")
    @Schema(description = "模板名称")
    private String name;

    /**
     * @see FreightTemplateEnum
     */
    @NotEmpty(message = "计价方式不能为空")
    @Schema(description = "计价方式：按件、按重量", allowableValues = "WEIGHT,NUM,FREE")
    private String pricingMethod;


}
