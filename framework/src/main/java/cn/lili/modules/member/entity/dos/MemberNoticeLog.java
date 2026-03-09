package cn.lili.modules.member.entity.dos;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 会员消息
 *
 * @author Chopper
 * @since 2020-02-25 14:10:16
 */
@Data
@TableName("li_member_notice_log")
@Schema(description = "会员消息")
@EqualsAndHashCode(callSuper = false)
public class MemberNoticeLog extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "消息内容")
    private String content;

    @Schema(description = "会员id")
    private String memberIds;

    @Schema(description = "管理员id")
    private String adminId;

    @Schema(description = "管理员名称")
    private String adminName;

    @Schema(description = "发送时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date sendTime;

    @Schema(description = "发送类型,0全站，1指定会员")
    private Integer sendType;


}