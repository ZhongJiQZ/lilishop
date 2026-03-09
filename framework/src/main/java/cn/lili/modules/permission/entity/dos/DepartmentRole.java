package cn.lili.modules.permission.entity.dos;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


/**
 * 角色部门绑定关系
 *
 * @author Chopper
 * @since 2020/11/19 12:18
 */
@Data
@TableName("li_department_role")
@Schema(description = "角色部门")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class DepartmentRole extends BaseEntity {


    private static final long serialVersionUID = 2342812932116647050L;

    @Schema(description = "角色id")
    private String roleId;

    @Schema(description = "部门id")
    private String departmentId;

}