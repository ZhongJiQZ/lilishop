package cn.lili.modules.permission.entity.dos;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 部门
 *
 * @author Chopper
 * @since 2020/11/19 11:57
 */
@Data
@TableName("li_department")
@Schema(description = "部门")
@EqualsAndHashCode(callSuper = true)
public class Department extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "部门名称")
    @NotNull(message = "部门名称不能为空")
    private String title;

    @Schema(description = "父id")
    private String parentId;

    @Schema(description = "排序值")
    private Double sortOrder;
}