package cn.lili.modules.member.entity.dto;

import cn.lili.common.security.sensitive.Sensitive;
import cn.lili.common.security.sensitive.enums.SensitiveStrategy;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 店员dto
 *
 * @author wget
 * @title: Clerk
 * @projectName lilishop
 * @date 2021/12/28 7:39 下午
 */
@Data
@NoArgsConstructor
public class ClerkEditDTO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "店员id", hidden = true)
    private String id;

    @Schema(description = "会员密码")
    private String password;

    @Schema(description = "状态")
    private Boolean status;

    @Schema(description = "所属部门id")
    private String departmentId;

    @Schema(description = "是否是超级管理员 超级管理员/普通管理员")
    private Boolean isSuper = false;

    @Schema(description = "角色")
    private List<String> roles;


}
