package cn.lili.modules.member.entity.dos;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 会员消息
 *
 * @author Chopper
 * @since 2020-02-25 14:10:16
 */
@Data
@TableName("li_member_notice_senter")
@Schema(description = "会员消息")
@EqualsAndHashCode(callSuper = false)
public class MemberNoticeSenter extends BaseEntity {
    /**
     * 标题
     */
    @Schema(description = "标题")
    private String title;
    /**
     * 消息内容
     */
    @Schema(description = "消息内容")
    private String content;
    /**
     * 会员id
     */
    @Schema(description = "会员id")
    private String memberIds;
    /**
     * 发送类型
     */
    @Schema(description = "发送类型,ALL 全站，SELECT 指定会员")
    private String sendType;

}