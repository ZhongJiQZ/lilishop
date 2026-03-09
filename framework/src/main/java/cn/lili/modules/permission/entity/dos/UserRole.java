package cn.lili.modules.permission.entity.dos;

import cn.lili.mybatis.BaseIdEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户角色
 *
 * @author Chopper
 * @since 2020/11/19 12:18
 */
@Data
@TableName("li_user_role")
@Schema(description = "用户角色")
@EqualsAndHashCode(callSuper = false)
public class UserRole extends BaseIdEntity {

    @Schema(description = "用户唯一id")
    private String userId;

    @Schema(description = "角色唯一id")
    private String roleId;

    public UserRole(String userId, String roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

    public UserRole() {

    }
}
