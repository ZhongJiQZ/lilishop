package cn.lili.modules.member.entity.dos;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 角色权限绑定关系
 *
 * @author Chopper
 * @since 2020/11/19 12:18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("li_store_menu_role")
@Schema(description = "店铺角色权限")
public class StoreMenuRole extends BaseEntity {

    private static final long serialVersionUID = -4680260092546996026L;

    @Schema(description = "角色id")
    private String roleId;

    @Schema(description = "菜单")
    private String menuId;

    @Schema(description = "店铺id")
    private String storeId;

    @Schema(description = "是否拥有操作数据权限，为否则只有查看权限")
    private Boolean isSuper;

}