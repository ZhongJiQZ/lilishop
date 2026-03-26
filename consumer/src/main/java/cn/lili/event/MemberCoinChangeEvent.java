package cn.lili.event;

import cn.lili.modules.member.entity.dto.MemberCoinMessage;

/**
 * 会员平台币改变消息
 *
 * @author lensing
 * @since 2026/03/25 7:13 下午
 */
public interface MemberCoinChangeEvent {

    /**
     * 会员平台币改变消息
     *
     * @param memberCoinMessage 会员平台币消息
     */
    void memberCoinChange(MemberCoinMessage memberCoinMessage);
}
