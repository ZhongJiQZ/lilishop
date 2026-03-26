package cn.lili.modules.member.aop.interceptor;

import cn.lili.modules.member.entity.dos.Member;
import cn.lili.modules.member.entity.dos.MemberCoinsHistory;
import cn.lili.modules.member.entity.enums.CoinTypeEnum;
import cn.lili.modules.member.service.MemberCoinsHistoryService;
import cn.lili.modules.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 平台币操作切面
 *
 * @author lensing
 * @since 2026/03/25 7:22 下午
 */
@Slf4j
@Aspect
@Component
public class CoinLogInterceptor {

    @Autowired
    private MemberCoinsHistoryService memberCoinsHistoryService;

    @Autowired
    private MemberService memberService;

    @After("@annotation(cn.lili.modules.member.aop.annotation.CoinLogPoint)")
    public void doAfter(JoinPoint pjp) {
        //参数
        Object[] obj = pjp.getArgs();
        try {
            //变动平台币
            Long coin = 0L;
            if (obj[0] != null) {
                coin = Long.valueOf(obj[0].toString());
            }
            //变动类型
            String type = CoinTypeEnum.INCREASE.name();
            if (obj[1] != null) {
                type = obj[1].toString();
            }
            // 会员ID
            String memberId = "";
            if (obj[2] != null) {
                memberId = obj[2].toString();
            }
            // 变动平台币为0，则直接返回
            if (coin == 0) {
                return;
            }

            //根据会员id查询会员信息
            Member member = memberService.getById(memberId);
            if (member != null) {
                MemberCoinsHistory memberCoinsHistory = new MemberCoinsHistory();
                memberCoinsHistory.setMemberId(member.getId());
                memberCoinsHistory.setMemberName(member.getUsername());
                memberCoinsHistory.setCoinType(type);

                memberCoinsHistory.setVariableCoin(coin);
                if (type.equals(CoinTypeEnum.INCREASE.name())) {
                    memberCoinsHistory.setBeforeCoin(member.getCoin() - coin);
                } else {
                    memberCoinsHistory.setBeforeCoin(member.getCoin() + coin);
                }

                memberCoinsHistory.setCoin(member.getCoin());
                memberCoinsHistory.setContent(obj[3] == null ? "" : obj[3].toString());
                memberCoinsHistory.setCreateBy("系统");
                memberCoinsHistoryService.save(memberCoinsHistory);
            }
        } catch (Exception e) {
            log.error("平台币操作错误", e);
        }


    }

}
