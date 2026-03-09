package cn.lili.modules.goods.entity.dos;

import cn.lili.mybatis.BaseIdEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author paulG
 * @since 2022/5/20
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("li_wholesale")
@Schema(description = "批发商品")
public class Wholesale extends BaseIdEntity {

    private static final long serialVersionUID = -6389806138583086068L;

    @Schema(description = "商品ID")
    private String goodsId;
    @Schema(description = "SkuID")
    private String skuId;
    @Schema(description = "模版id")
    private String templateId;
    @Schema(description = "数量")
    private Integer num;
    @Schema(description = "金额")
    private Double price;
}
