package cn.lili.modules.member.entity.vo;

import cn.lili.common.utils.BeanUtil;
import cn.lili.modules.member.entity.dos.MemberEvaluation;
import cn.lili.mybatis.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 会员评价VO
 *
 * @author Bulbasaur
 * @since 2020/11/30 15:00
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class MemberEvaluationVO extends MemberEvaluation {

    private static final long serialVersionUID = 6696978796248845481L;

    @Schema(description = "评论图片")
    private List<String> evaluationImages;

    @Schema(description = "回复评论图片")
    private List<String> replyEvaluationImages;

    public MemberEvaluationVO(MemberEvaluation memberEvaluation) {
        BeanUtil.copyProperties(memberEvaluation, this);
    }
}
