package cn.lili.modules.permission.entity.dto;

import cn.lili.mybatis.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

/**
 * 管理员入库dto
 *
 * @author Chopper
 * @since 2020/11/16 19:55
 */
@Data
@Schema(description = "管理员入库dto")
@EqualsAndHashCode(callSuper = false)
public class AdminUserDTO extends BaseEntity {

    private static final long serialVersionUID = 1L;


    @Schema(description = "用户名")
    @Length(max = 20, message = "用户名长度不能超过20个字符")
    private String username;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "昵称")
    @Length(max = 10, message = "昵称长度不能超过10个字符")
    private String nickName;

    @Schema(description = "手机")
    @Length(max = 11, message = "手机号长度不能超过11")
    private String mobile;

    @Schema(description = "邮件")
    @Length(max = 100, message = "邮箱长度不能超过100")
    private String email;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "描述/详情/备注")
    private String description;

    @Schema(description = "所属部门id")
    private String departmentId;

    @Schema(description = "是否为超级管理员")
    private Boolean isSuper;
}
