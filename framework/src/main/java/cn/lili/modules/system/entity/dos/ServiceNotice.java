package cn.lili.modules.system.entity.dos;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 服务订阅消息
 *
 * @author Chopper
 * @since 2020-02-25 14:10:16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("li_service_notice")
@Schema(description = "服务订阅消息")
public class ServiceNotice extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "商家id，为-1时，代表是平台发布的消息")
    private String storeId;

    @Schema(description = "banner图")
    private String bannerImage;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "副标题")
    private String subTitle;

    @Schema(description = "点击跳转（此内容与站内信内容只能有一个生效）")
    private String toUrl;

    @Schema(description = "站内信内容(富文本框编辑，可以上传图片的html)")
    private String content;

}