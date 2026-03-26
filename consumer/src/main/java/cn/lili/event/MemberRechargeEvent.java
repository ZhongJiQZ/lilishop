package cn.lili.event;

import cn.lili.modules.member.entity.dos.Member;

/**
 * 会员充值消息
 *
 * @author lensing
 * @since 2026/03/25 7:13 下午
 */
public interface MemberRechargeEvent {

    /**
     * 会员充值
     *
     * @param member 会员
     */
    void memberRecharge(Member member);
}
