package cn.lili.modules.goods.entity.dto;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 分类查询参数
 *
 * @author paulG
 * @since 2020/12/1
 **/
@Data
public class CategorySearchParams {

    @Schema(description = "分类名称")
    private String name;

    @Schema(description = "父id")
    private String parentId;

    @Schema(description = "层级")
    private Integer level;

    @Schema(description = "排序值")
    private BigDecimal sortOrder;

    @Schema(description = "佣金比例")
    private BigDecimal commissionRate;

    @Schema(description = "父节点名称")
    private String parentTitle;

    @Schema(description = "是否禁用")
    private Boolean deleteFlag;

    public <T> QueryWrapper<T> queryWrapper() {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(name != null, "name", name);
        queryWrapper.like(parentTitle != null, "parent_title", parentTitle);
        queryWrapper.eq(parentId != null, "parent_id", parentId);
        queryWrapper.eq(level != null, "level", level);
        queryWrapper.eq(sortOrder != null, "sort_order", sortOrder);
        queryWrapper.eq(commissionRate != null, "commission_rate", commissionRate);
        queryWrapper.eq(deleteFlag != null, "delete_flag", deleteFlag);
        return queryWrapper;
    }
}
