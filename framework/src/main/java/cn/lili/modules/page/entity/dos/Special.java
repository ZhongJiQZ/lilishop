package cn.lili.modules.page.entity.dos;

import cn.lili.common.enums.ClientTypeEnum;
import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 专题活动
 *
 * @author Bulbasaur
 * @since 2020/12/10 17:42
 */
@Data
@TableName("li_special")
@Schema(description = "专题活动")
@EqualsAndHashCode(callSuper = false)
public class Special extends BaseEntity {

    @Schema(description = "专题活动名称")
    private String specialName;

    /**
     * @see ClientTypeEnum
     */
    @Schema(description = "楼层对应连接端类型", allowableValues = "PC,H5,WECHAT_MP,APP")
    private String clientType;

    @Schema(description = "页面ID")
    private String pageDataId;
}
