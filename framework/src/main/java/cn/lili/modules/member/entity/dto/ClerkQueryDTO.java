package cn.lili.modules.member.entity.dto;

import cn.lili.mybatis.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

/**
 * 店员查询
 *
 * @author Chopper
 * @since 2020/11/16 19:55
 */
@Data
@Schema(description = "店员查询")
@EqualsAndHashCode(callSuper = false)
public class ClerkQueryDTO extends BaseEntity {

    private static final long serialVersionUID = 1L;


    @Schema(description = "店员名称")
    @Length(max = 20, message = "用户名长度不能超过20个字符")
    private String clerkName;

    @Schema(description = "手机")
    @Length(max = 11, message = "手机号长度不能超过11")
    private String mobile;

    @Schema(description = "所属部门id")
    private String departmentId;

    @Schema(description = "是否为超级管理员")
    private Boolean isSuper;

    @Schema(description = "状态")
    private Boolean status;

    @Schema(description = "店铺id", hidden = true)
    private String storeId;
}
