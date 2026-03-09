package cn.lili.modules.promotion.entity.vos.kanjia;

import cn.lili.modules.promotion.entity.dos.KanjiaActivity;
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
@Schema(description = "砍价活动VO")
@EqualsAndHashCode(callSuper = false)
public class KanjiaActivityVO extends KanjiaActivity {

    @Schema(description = "是否可以砍价")
    private Boolean help;

    @Schema(description = "是否已发起砍价")
    private Boolean launch;

    @Schema(description = "是否可购买")
    private Boolean pass;

    public KanjiaActivityVO() {
        this.setHelp(false);
        this.setLaunch(false);
        this.setPass(false);
    }

}