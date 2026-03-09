package cn.lili.modules.statistics.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.Objects;

/**
 * 流量数据展示VO
 *
 * @author Chopper
 * @since 2020-06-19 17:50
 */
@Data
public class PlatformViewVO {

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @Schema(description = "展示时间")
    private Date date;

    @Schema(description = "pv数量")
    private Long pvNum;

    @Schema(description = "uv数量")
    private Long uvNum;

    @Schema(description = "店铺id")
    private String storeId = "-1";


    public PlatformViewVO() {
        //初始化参数
        pvNum = 0L;
        uvNum = 0L;
    }

    public Long getPvNum() {
        if (Objects.isNull(pvNum)) {
            return 0L;
        }
        return pvNum;
    }

    public Long getUvNum() {
        if (Objects.isNull(uvNum)) {
            return 0L;
        }
        return uvNum;
    }

    public PlatformViewVO(Date date) {
        //初始化参数
        pvNum = 0L;
        uvNum = 0L;
        this.date = date;
    }

}
