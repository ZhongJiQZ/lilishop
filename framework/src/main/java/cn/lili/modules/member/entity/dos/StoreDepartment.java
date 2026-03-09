package cn.lili.modules.member.entity.dos;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 部门
 *
 * @author Chopper
 * @since 2020/11/19 11:57
 */
@Data
@TableName("li_store_department")
@Schema(description = "店铺部门")
@EqualsAndHashCode(callSuper = false)
public class StoreDepartment extends BaseEntity {

    private static final long serialVersionUID = 1L;
    @Schema(description = "店铺id", hidden = true)
    private String storeId;

    @Schema(description = "部门名称")
    @NotEmpty(message = "部门名称不能为空")
    private String title;

    @Schema(description = "父id")
    @NotEmpty(message = "父id不能为空")
    private String parentId;

    @Schema(description = "排序值")
    private Double sortOrder;
}