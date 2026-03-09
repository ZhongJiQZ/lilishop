package cn.lili.modules.system.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 秒杀活动设置
 *
 * @author Bulbasaur
 * @since 2021/5/24 3:27 下午
 */
@Data
public class SeckillSetting implements Serializable {

    @Schema(description = "开启几点场 例如：6,8,12")
    @NotNull(message = "活动时间段不能为空")
    private String hours;

    @Schema(description = "秒杀规则")
    @NotNull(message = "秒杀规则不能为空")
    private String seckillRule;
}
