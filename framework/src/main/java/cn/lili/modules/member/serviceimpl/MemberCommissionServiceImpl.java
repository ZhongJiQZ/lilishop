package cn.lili.modules.member.serviceimpl;


import cn.lili.modules.member.entity.dos.MemberCommission;
import cn.lili.modules.member.mapper.MemberCommissionMapper;
import cn.lili.modules.member.service.MemberCommissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * 会员佣金明细业务层实现
 *
 * @author lensing
 * @since 2026-04-03 14:10:16
 */
@Service
public class MemberCommissionServiceImpl extends ServiceImpl<MemberCommissionMapper, MemberCommission> implements MemberCommissionService {

    /**
     * 根据邀请人ID获取【已结算】总佣金
     */
    @Override
    public BigDecimal getTotalCommissionByInviter(String memberId) {
        BigDecimal total = this.baseMapper.getTotalCommissionByInviter(memberId);
        return total == null ? BigDecimal.ZERO : total;
    }
}