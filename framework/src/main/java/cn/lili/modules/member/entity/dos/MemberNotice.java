package cn.lili.modules.member.entity.dos;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 会员站内信
 *
 * @author Chopper
 * @since 2020-02-25 14:10:16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("li_member_notice")
@Schema(description = "会员站内信")
public class MemberNotice extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "会员id")
    private String memberId;

    @Schema(description = "是否已读")
    private Boolean isRead;

    @Schema(description = "阅读时间")
    private Long receiveTime;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "站内信内容")
    private String content;

}