package cn.lili.modules.goods.entity.dos;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

/**
 * 店铺商品分类
 *
 * @author pikachu
 * @since 2020-02-18 15:18:56
 */
@Data
@TableName("li_store_goods_label")
@Schema(description = "店铺商品分类")
@EqualsAndHashCode(callSuper = false)
public class StoreGoodsLabel extends BaseEntity {

    private static final long serialVersionUID = -5292518678940634419L;

    @Schema(description = "店铺ID")
    private String storeId;

    @NotEmpty(message = "店铺商品分类名称不能为空")
    @Length(max = 20, message = "店铺商品分类名称太长")
    @Schema(description = "店铺商品分类名称")
    private String labelName;


    @NotNull(message = "店铺商品分类排序不能为空")
    @Max(value = 99999, message = "排序值太大")
    @Schema(description = "店铺商品分类排序")
    private BigDecimal sortOrder;

    @NotEmpty(message = "父节点不能为空，需设定根节点或者某节点的子节点")
    @Schema(description = "父id, 根节点为0")
    private String parentId;

    @Schema(description = "层级, 从0开始")
    private Integer level;


}
