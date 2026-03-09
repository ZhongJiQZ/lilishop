package cn.lili.modules.statistics.entity.dos;

import cn.lili.mybatis.BaseIdEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 平台pv统计
 *
 * @author Chopper
 * @since 2020-06-19 17:50
 */
@Data
@TableName("li_s_platform_view_data")
@Schema(description = "平台pv统计")
@EqualsAndHashCode(callSuper = false)
public class PlatformViewData extends BaseIdEntity {


    @Schema(description = "pv数量")
    private Long pvNum;

    @Schema(description = "uv数量")
    private Long uvNum;


    @Schema(description = "统计日")
    private Date date;

    //默认是平台流量统计//

    @Schema(description = "店铺id")
    private String storeId = "-1";
}
