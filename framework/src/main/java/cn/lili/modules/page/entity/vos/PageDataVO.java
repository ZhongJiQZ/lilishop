package cn.lili.modules.page.entity.vos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 楼层装修数据VO
 *
 * @author Bulbasaur
 * @since 2020/12/10 17:42
 */
@Data
public class PageDataVO {

    @Schema(description = "页面数据")
    private String pageData;
}
