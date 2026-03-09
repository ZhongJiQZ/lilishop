package cn.lili.modules.permission.entity.dos;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 角色
 *
 * @author Chopper
 * @since 2020/11/19 11:57
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("li_role")
@Schema(description = "角色")
public class Role extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "角色名")
    private String name;

    @Schema(description = "是否为注册默认角色")
    private Boolean defaultRole;

    @Schema(description = "备注")
    private String description;

}
