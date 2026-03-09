package cn.lili.modules.member.entity.dos;

import cn.lili.mybatis.BaseIdEntity;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 会员签到
 *
 * @author pikachu
 * @since 2020-02-25 14:10:16
 */
@Data
@TableName("li_member_sign")
@Schema(description = "会员签到")
@EqualsAndHashCode(callSuper = false)
public class MemberSign extends BaseIdEntity {

    private static final long serialVersionUID = 1L;

    @CreatedDate
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间", hidden = true)
    private Date createTime;

    @Schema(description = "会员用户名")
    private String memberName;

    @Schema(description = "会员用户ID")
    private String memberId;

    @Schema(description = "连续签到天数")
    private Integer signDay;


    @Schema(description = "签到日 为数字 从现在减去19700101 的日期")
    private Integer day;

}
