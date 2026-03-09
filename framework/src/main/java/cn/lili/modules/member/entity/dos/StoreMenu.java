package cn.lili.modules.member.entity.dos;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 菜单权限
 *
 * @author Chopper
 * @since 2020/11/19 12:12
 */
@Data
@TableName("li_store_menu")
@Schema(description = "店铺菜单权限")
@EqualsAndHashCode(callSuper = false)
public class StoreMenu extends BaseEntity {

    private static final long serialVersionUID = 7050744476203495207L;


    @Schema(description = "菜单标题")
    private String title;

    @Schema(description = "路由名称")
    private String name;

    @Schema(description = "路径")
    private String path;

    @Schema(description = "菜单层级")
    private Integer level;

    @Schema(description = "前端目录文件")
    private String frontRoute;

    @Schema(description = "父id")
    private String parentId = "0";

    @Schema(description = "排序值")
    private Double sortOrder;

    @Schema(description = "权限URL，*号模糊匹配，逗号分割")
    private String permission;


}