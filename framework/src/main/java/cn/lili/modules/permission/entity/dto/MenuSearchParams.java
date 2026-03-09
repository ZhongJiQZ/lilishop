package cn.lili.modules.permission.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 权限搜索参数
 *
 * @author Chopper
 * @since 2020-11-19 16:46
 */
@Data
public class MenuSearchParams {

    @Schema(description = "菜单/权限名称")
    private String name;

    @Schema(description = "层级")
    private Integer level;

    @Schema(description = "菜单标题")
    private String title;

    @Schema(description = "赋权API地址,正则表达式")
    private String path;

    @Schema(description = "前端路由")
    private String frontRoute;

    @Schema(description = "图标")
    private String icon;

}
