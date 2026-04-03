package cn.lili.modules.member.service;

import cn.lili.modules.member.entity.dos.MemberCommission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;

/**
 * 会员佣金明细业务层
 *
 * @author lensing
 * @since 2026-04-03 14:10:16
 */
public interface MemberCommissionService extends IService<MemberCommission> {

    /**
     * 根据邀请人ID获取总已结算佣金
     * @param currentMemberId 邀请人ID
     * @return
     */
    BigDecimal getTotalCommissionByInviter(String currentMemberId);
}