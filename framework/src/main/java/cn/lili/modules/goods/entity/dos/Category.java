package cn.lili.modules.goods.entity.dos;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品分类
 *
 * @author pikachu
 * @since 2020-02-18 15:18:56
 */
@Data
@TableName("li_category")
@Schema(description = "商品分类")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Category extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @NotEmpty(message = "分类名称不能为空")
    @Size(max = 20)
    @Schema(description = "分类名称")
    private String name;

    @NotEmpty(message = "请选择父分类")
    @Schema(description = "父id, 根节点为0")
    private String parentId;

    @NotNull(message = "层级不能为空")
    @Min(value = 0, message = "层级需要大于0")
    @Max(value = 3, message = "层级最大为3")
    @Schema(description = "层级, 从0开始")
    private Integer level;

    @NotNull(message = "排序值不能为空")
    @Max(value = 999, message = "排序值最大999")
    @Schema(description = "排序值")
    private BigDecimal sortOrder;

    @Schema(description = "佣金比例")
    private Double commissionRate;

    @Schema(description = "分类图标")
    private String image;

    @Schema(description = "是否支持频道")
    private Boolean supportChannel;

    public Category(String id, String createBy, Date createTime, String updateBy, Date updateTime, Boolean deleteFlag, String name, String parentId, Integer level, BigDecimal sortOrder, Double commissionRate, String image, Boolean supportChannel) {
        super(id, createBy, createTime, updateBy, updateTime, deleteFlag);
        this.name = name;
        this.parentId = parentId;
        this.level = level;
        this.sortOrder = sortOrder;
        this.commissionRate = commissionRate;
        this.image = image;
        this.supportChannel = supportChannel;
    }

    public Category(String id, String name, String parentId, Integer level, BigDecimal sortOrder, Double commissionRate, String image, Boolean supportChannel) {
        this.name = name;
        this.parentId = parentId;
        this.level = level;
        this.sortOrder = sortOrder;
        this.commissionRate = commissionRate;
        this.image = image;
        this.supportChannel = supportChannel;
    }
}