package cn.lili.modules.member.entity.dos;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 会员等级
 *
 * @author Bulbasaur
 * @since 2021/5/14 5:43 下午
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("li_member_grade")
@Schema(description = "会员等级")
public class MemberGrade extends BaseEntity {

    @NotNull
    @Schema(description = "等级名称")
    private String gradeName;

    @NotNull
    @Schema(description = "等级图片 1029*498")
    private String gradeImage;

    @NotNull
    @Schema(description = "会员等级")
    private Integer level;

    @Schema(description = "累计实付")
    private Double payPrice;

    @Schema(description = "累计购买次数")
    private Integer count;
}
