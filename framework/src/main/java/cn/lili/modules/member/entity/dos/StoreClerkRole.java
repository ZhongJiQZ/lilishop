package cn.lili.modules.member.entity.dos;

import cn.lili.mybatis.BaseIdEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 用户角色
 *
 * @author Chopper
 * @since 2020/11/19 12:18
 */
@Data
@TableName("li_clerk_role")
@Schema(description = "用户角色")
@Builder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class StoreClerkRole extends BaseIdEntity {

    @Schema(description = "店员唯一id")
    private String clerkId;

    @Schema(description = "角色唯一id")
    private String roleId;

    public StoreClerkRole(String clerkId, String roleId) {
        this.clerkId = clerkId;
        this.roleId = roleId;
    }

}

