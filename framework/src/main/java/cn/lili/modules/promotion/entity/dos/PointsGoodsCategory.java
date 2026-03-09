package cn.lili.modules.promotion.entity.dos;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 积分商品分类
 *
 * @author paulG
 * @since 2020-03-19 10:44 上午
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("li_points_goods_category")
@Schema(description = "积分商品分类")
@AllArgsConstructor
@NoArgsConstructor
public class PointsGoodsCategory extends BaseEntity {

    private static final long serialVersionUID = 4689246801280318515L;

    @NotEmpty(message = "分类名称不能为空")
    @Schema(description = "分类名称")
    private String name;

    @Schema(description = "父id, 根节点为0")
    private String parentId;

    @Schema(description = "层级, 从0开始")
    private Integer level;

    @Schema(description = "排序值")
    private BigDecimal sortOrder;

}
