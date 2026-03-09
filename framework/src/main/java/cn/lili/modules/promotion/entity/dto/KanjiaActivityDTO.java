package cn.lili.modules.promotion.entity.dto;


import cn.lili.modules.promotion.entity.dos.KanjiaActivityLog;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 砍价活动参与实体类
 *
 * @author qiuqiu
 * @date 2020-7-1 10:44 上午
 */
@Data
@Schema(description = "砍价活动参与记录对象")
@EqualsAndHashCode(callSuper = false)
public class KanjiaActivityDTO extends KanjiaActivityLog {

    @Schema(description = "砍价商品Id")
    private String kanjiaActivityGoodsId;

}