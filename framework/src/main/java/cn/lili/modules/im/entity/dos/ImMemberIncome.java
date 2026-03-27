package cn.lili.modules.im.entity.dos;


import cn.lili.mybatis.BaseTenantEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author lensing
 */
@Data
@TableName("li_im_member_income")
@Schema(description = "会员收益表表（试穿员）")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ImMemberIncome extends BaseTenantEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "试穿员会员ID")
    private String memberId;

    @Schema(description = "试穿员名称")
    private String memberName;

    @Schema(description = "总收益（平台币）")
    private BigDecimal totalIncome;

    @Schema(description = "今日收益（平台币）")
    private BigDecimal todayIncome;
}