package cn.lili.modules.member.mapper;

import cn.lili.modules.member.entity.dos.MemberCommission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

/**
 * 会员佣金明细数据处理层
 *
 * @author lensing
 * @since 2026-04-03 14:10:16
 */
public interface MemberCommissionMapper extends BaseMapper<MemberCommission> {

    /**
     * 根据邀请人ID获取已结算佣金
     */
    @Select("SELECT IFNULL(SUM(commission), 0) FROM li_member_commission " +
            "WHERE commission_member_id = #{memberId} AND is_settled = 1 AND delete_flag = 0")
    BigDecimal getTotalCommissionByInviter(@Param("memberId") String memberId);
}