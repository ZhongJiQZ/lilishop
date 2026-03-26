package cn.lili.event.impl;


import cn.lili.event.MemberRechargeEvent;
import cn.lili.modules.member.entity.dos.Member;
import cn.lili.modules.member.entity.enums.CoinTypeEnum;
import cn.lili.modules.member.service.MemberService;
import cn.lili.modules.order.order.entity.enums.PayStatusEnum;
import cn.lili.modules.order.order.service.OrderService;
import cn.lili.modules.system.entity.dos.Setting;
import cn.lili.modules.system.entity.dto.CoinSetting;
import cn.lili.modules.system.entity.enums.SettingEnum;
import cn.lili.modules.system.service.SettingService;
import cn.lili.modules.wallet.entity.dos.Recharge;
import cn.lili.modules.wallet.service.RechargeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 会员平台币
 *
 * @author lensing
 * @since 2026-03-25 11:20
 */
@Service
public class MemberCoinExecute implements MemberRechargeEvent
{

    /**
     * 配置
     */
    @Autowired
    private SettingService settingService;
    /**
     * 会员
     */
    @Autowired
    private MemberService memberService;
    /**
     * 会员
     */
    @Autowired
    private RechargeService rechargeService;
    /**
     * 订单
     */
    @Autowired
    private OrderService orderService;

//    /**
//     * 会员注册赠送平台币
//     *
//     * @param member 会员
//     */
//    @Override
//    public void memberRegister(Member member) {
//        //获取平台币设置
//        CoinSetting coinSetting = getCoinSetting();
//        //赠送会员平台币
//        memberService.updateMemberCoin(coinSetting.getRegister().longValue(), CoinTypeEnum.INCREASE.name(), member.getId(), "会员注册，赠送平台币" + coinSetting.getRegister() + "分");
//    }

//    /**
//     * 会员评价赠送平台币
//     *
//     * @param memberEvaluation 会员评价
//     */
//    @Override
//    public void goodsComment(MemberEvaluation memberEvaluation) {
//        //获取平台币设置
//        CoinSetting coinSetting = getCoinSetting();
//        //赠送会员平台币
//        memberService.updateMemberCoin(coinSetting.getComment().longValue(), CoinTypeEnum.INCREASE.name(), memberEvaluation.getMemberId(), "会员评价，赠送平台币" + coinSetting.getComment() + "分");
//    }
//
//    /**
//     * 非平台币订单订单完成后赠送平台币
//     *
//     * @param orderMessage 订单消息
//     */
//    @Override
//    public void orderChange(OrderMessage orderMessage) {
//
//        switch (orderMessage.getNewStatus()) {
//            case CANCELLED: {
//                Order order = orderService.getBySn(orderMessage.getOrderSn());
//                Long coin = order.getPriceDetailDTO().getPayCoin();
//                if (coin <= 0) {
//                    return;
//                }
//                //如果未付款，则不去要退回相关代码执行
//                if (order.getPayStatus().equals(PayStatusEnum.UNPAID.name())) {
//                    return;
//                }
//                String content = "订单取消，平台币返还：" + coin + "分";
//                //赠送会员平台币
//                memberService.updateMemberCoin(coin, CoinTypeEnum.INCREASE.name(), order.getMemberId(), content);
//                break;
//            }
//            case COMPLETED: {
//                Order order = orderService.getBySn(orderMessage.getOrderSn());
//                //如果是平台币订单 则直接返回
//                if (CharSequenceUtil.isNotEmpty(order.getOrderPromotionType())
//                        && order.getOrderPromotionType().equals(OrderPromotionTypeEnum.POINTS.name())) {
//                    return;
//                }
//                //获取平台币设置
//                CoinSetting coinSetting = getCoinSetting();
//                if (coinSetting.getConsumer() == 0) {
//                    return;
//                }
//                //计算赠送平台币数量
//                Double coin = CurrencyUtil.mul(coinSetting.getConsumer(), order.getFlowPrice(), 0);
//                //赠送会员平台币
//                memberService.updateMemberCoin(coin.longValue(), CoinTypeEnum.INCREASE.name(), order.getMemberId(), "会员下单，赠送平台币" + coin + "分");
//                break;
//            }
//
//            default:
//                break;
//        }
//    }
//
//
//    /**
//     * 提交售后后扣除平台币
//     *
//     * @param afterSale 售后
//     */
//    @Override
//    public void afterSaleStatusChange(AfterSale afterSale) {
//        if (afterSale.getServiceStatus().equals(AfterSaleStatusEnum.COMPLETE.name())) {
//            Order order = orderService.getBySn(afterSale.getOrderSn());
//            //获取平台币设置
//            CoinSetting coinSetting = getCoinSetting();
//            if (coinSetting.getConsumer() == 0 || !OrderStatusEnum.COMPLETED.name().equals(order.getOrderStatus())) {
//                return;
//            }
//            //计算扣除平台币数量
//            Double coin = CurrencyUtil.mul(coinSetting.getConsumer(), afterSale.getActualRefundPrice(), 0);
//            //扣除会员平台币
//            memberService.updateMemberCoin(coin.longValue(), CoinTypeEnum.REDUCE.name(), afterSale.getMemberId(), "会员退款，回退消费赠送平台币" + coin + "分");
//
//        }
//    }

    /**
     * 获取平台币设置
     *
     * @return 平台币设置
     */
    private CoinSetting getCoinSetting() {
        Setting setting = settingService.get(SettingEnum.COIN_SETTING.name());
        return new Gson().fromJson(setting.getSettingValue(), CoinSetting.class);
    }

    @Override
    public void memberRecharge(Member member) {
        //判断是否首次充值
        if (isFirstPaidRecharge(member.getId())) {
            //更新为VIP会员
            member.setIsVip(true);
            memberService.updateById(member);
            //获取平台币设置
            CoinSetting coinSetting = getCoinSetting();
            //赠送50平台币
            memberService.updateMemberCoin(coinSetting.getRegister().longValue(), CoinTypeEnum.INCREASE.name(), member.getId(), "会员首次充值，赠送平台币" + coinSetting.getRecharge() + "币");
        }
    }

    /**
     * 判断用户是否首次成功充值
     * @param memberId 会员ID
     * @return true=首次充值 false=非首次充值
     */
    private boolean isFirstPaidRecharge(String memberId) {
        long paidCount = rechargeService.count(new LambdaQueryWrapper<Recharge>()
                .eq(Recharge::getMemberId, memberId)
                .eq(Recharge::getPayStatus, PayStatusEnum.PAID.name())
        );
        // 已支付订单数量 ≤ 1 代表首次充值（包含当前刚支付的这笔）
        return paidCount <= 1;
    }
}
