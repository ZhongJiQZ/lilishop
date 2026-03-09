package cn.lili.modules.goods.entity.dos;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品计量单位
 *
 * @author Bulbasaur
 * @since 2020/11/26 16:08
 */
@Data
@TableName("li_goods_unit")
@Schema(description = "商品计量单位")
@EqualsAndHashCode(callSuper = false)
public class GoodsUnit extends BaseEntity {

    @NotEmpty(message = "计量单位名称不能为空")
    @Size(max = 5, message = "计量单位长度最大为5")
    @Schema(description = "计量单位名称")
    private String name;
}
