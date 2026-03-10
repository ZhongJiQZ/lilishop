package cn.lili.modules.circle.entity.dos;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 圈子帖子关联商品（帖子带货）
 *
 * @author lensing
 * @since 2026-03-09 15:18:56
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("li_circle_post_goods")
@Schema(description = "圈子帖子关联商品")
public class CirclePostGoods extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "帖子ID")
    private Long postId;

    @Schema(description = "商品ID")
    private Long goodsId;

    @Schema(description = "具体SKU ID（可选）")
    private Long skuId;

    @Schema(description = "展示排序")
    private Integer sort = 0;
}