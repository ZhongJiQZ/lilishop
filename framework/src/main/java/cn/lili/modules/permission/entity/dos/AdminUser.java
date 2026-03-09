package cn.lili.modules.permission.entity.dos;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;


/**
 * 管理员类
 *
 * @author Chopper
 * @since 2020/11/19 11:42
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("li_admin_user")
@Schema(description = "管理员")
public class AdminUser extends BaseEntity {

    private static final long serialVersionUID = 2918352800205024873L;

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

    @Schema(description = "用户头像")
    private String avatar = "https://i.loli.net/2020/11/19/LyN6JF7zZRskdIe.png";

    @Schema(description = "是否是超级管理员 超级管理员/普通管理员")
    private Boolean isSuper = false;

    @Schema(description = "状态 默认true正常 false禁用")
    private Boolean status = true;

    @Schema(description = "描述/详情/备注")
    private String description;

    @Schema(description = "所属部门id")
    private String departmentId;
    /**
     * 冗余字段
     */
    @Schema(description = "角色id集合")
    private String roleIds;

}
