package cn.lili.modules.store.entity.dos;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 运费模板子配置
 *
 * @author Chopper
 * @since 2020/11/17 4:27 下午
 */
@Data
@TableName("li_freight_template_child")
@Schema(description = "运费模板子配置")
@EqualsAndHashCode(callSuper = false)
public class FreightTemplateChild extends BaseEntity {

    private static final long serialVersionUID = -5043707833032504674L;

    @Schema(description = "店铺模板ID")
    private String freightTemplateId;

    @Schema(description = "首重/首件")
    private Double firstCompany;

    @Schema(description = "运费")
    private Double firstPrice;

    @Schema(description = "续重/续件")
    private Double continuedCompany;

    @Schema(description = "续费")
    private Double continuedPrice;

    @Schema(description = "地址，示例参数：上海,江苏,浙江")
    private String area;

    @Schema(description = "地区ID，示例参数：1,2,3,4")
    private String areaId;

}
