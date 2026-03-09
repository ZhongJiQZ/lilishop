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
@TableName("li_store_role")
@Schema(description = "店铺角色")
@EqualsAndHashCode(callSuper = false)
public class StoreRole extends BaseEntity {

    @Schema(description = "角色名")
    @NotEmpty(message = "角色名称必填")
    private String name;

    @Schema(description = "店铺id", hidden = true)
    private String storeId;

    @Schema(description = "是否为注册默认角色")
    private Boolean defaultRole = false;

    @Schema(description = "备注")
    private String description;
}