package cn.lili.modules.wallet.serviceimpl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.text.CharSequenceUtil;
import cn.lili.common.enums.ResultCode;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.properties.RocketmqCustomProperties;
import cn.lili.common.security.AuthUser;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.utils.SnowFlake;
import cn.lili.common.vo.PageVO;
import cn.lili.modules.member.entity.dos.Member;
import cn.lili.modules.member.service.MemberService;
import cn.lili.modules.order.order.entity.enums.PayStatusEnum;
import cn.lili.modules.order.trade.entity.vo.RechargeQueryVO;
import cn.lili.modules.payment.entity.enums.PaymentMethodEnum;
import cn.lili.modules.system.entity.dos.Setting;
import cn.lili.modules.system.entity.dto.CoinSetting;
import cn.lili.modules.system.entity.enums.SettingEnum;
import cn.lili.modules.system.service.SettingService;
import cn.lili.modules.wallet.entity.dos.Recharge;
import cn.lili.modules.wallet.entity.dto.MemberWalletUpdateDTO;
import cn.lili.modules.wallet.entity.enums.DepositServiceTypeEnum;
import cn.lili.modules.wallet.mapper.RechargeMapper;
import cn.lili.modules.wallet.service.MemberWalletService;
import cn.lili.modules.wallet.service.RechargeService;
import cn.lili.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 * 预存款业务层实现
 *
 * @author pikachu
 * @since 2020-02-25 14:10:16
 */
@Service
public class RechargeServiceImpl extends ServiceImpl<RechargeMapper, Recharge> implements RechargeService {

    /**
     * 会员预存款
     */
    @Autowired
    @Lazy
    private MemberWalletService memberWalletService;

    @Autowired
    private MemberService memberService;
    /**
     * 配置
     */
    @Autowired
    private SettingService settingService;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    /**
     * RocketMQ 配置
     */
    @Autowired
    private RocketmqCustomProperties rocketmqCustomProperties;

    @Override
    public Recharge recharge(Double price) {

        if (price == null || price <= 0 || price > 1000000) {
            throw new ServiceException(ResultCode.RECHARGE_PRICE_ERROR);
        }

        //获取当前登录的会员
        AuthUser authUser = UserContext.getCurrentUser();
        //构建sn
        String sn = "Y" + SnowFlake.getId();
        //整合充值订单数据
        Recharge recharge = new Recharge(sn, authUser.getId(), authUser.getUsername(), price);
        recharge.setRechargeType(0);
        //添加预存款充值账单
        this.save(recharge);
        //返回预存款
        return recharge;
    }

    @Override
    public Recharge rechargeMember(Double price) {
        if (price == null || price <= 0 || price > 1000000) {
            throw new ServiceException(ResultCode.RECHARGE_PRICE_ERROR);
        }

        //获取当前登录的会员
        AuthUser authUser = UserContext.getCurrentUser();
        //构建sn
        String sn = "Y" + SnowFlake.getId();
        //整合充值订单数据
        Recharge recharge = new Recharge(sn, authUser.getId(), authUser.getUsername(), price);
        recharge.setRechargeType(1);
        //添加预存款充值账单
        this.save(recharge);
        //返回预存款
        return recharge;
    }

    @Override
    public IPage<Recharge> rechargePage(PageVO page, RechargeQueryVO rechargeQueryVO) {
        //构建查询条件
        QueryWrapper<Recharge> queryWrapper = new QueryWrapper<>();
        //会员名称
        queryWrapper.like(!CharSequenceUtil.isEmpty(rechargeQueryVO.getMemberName()), "member_name", rechargeQueryVO.getMemberName());
        //充值订单号
        queryWrapper.eq(!CharSequenceUtil.isEmpty(rechargeQueryVO.getRechargeSn()), "recharge_sn", rechargeQueryVO.getRechargeSn());
        //会员id
        queryWrapper.eq(!CharSequenceUtil.isEmpty(rechargeQueryVO.getMemberId()), "member_id", rechargeQueryVO.getMemberId());
        //支付时间 开始时间和结束时间
        if (!CharSequenceUtil.isEmpty(rechargeQueryVO.getStartDate()) && !CharSequenceUtil.isEmpty(rechargeQueryVO.getEndDate())) {
            Date start = cn.hutool.core.date.DateUtil.parse(rechargeQueryVO.getStartDate());
            Date end = cn.hutool.core.date.DateUtil.parse(rechargeQueryVO.getEndDate());
            queryWrapper.between("pay_time", start, end);
        }
        queryWrapper.orderByDesc("create_time");
        //查询返回数据
        return this.page(PageUtil.initPage(page), queryWrapper);
    }

    @Override
    public void paySuccess(String sn, String receivableNo, String paymentMethod) {
        //根据sn获取支付账单
        Recharge recharge = this.getOne(new QueryWrapper<Recharge>().eq("recharge_sn", sn));
        //如果支付账单不为空则进行一下逻辑
        if (recharge != null && !recharge.getPayStatus().equals(PayStatusEnum.PAID.name())) {
            //将此账单支付状态更改为已支付
            recharge.setPayStatus(PayStatusEnum.PAID.name());
            recharge.setReceivableNo(receivableNo);
            recharge.setPayTime(new DateTime());
            recharge.setRechargeWay(paymentMethod);
            //执行保存操作
            this.updateById(recharge);
            //增加预存款余额
            memberWalletService.increase(new MemberWalletUpdateDTO(recharge.getRechargeMoney(), recharge.getMemberId(), "会员余额充值，充值单号为：" + recharge.getRechargeSn(), DepositServiceTypeEnum.WALLET_RECHARGE.name()));
            //充值会员
            rechargeMember(recharge);
            //发送会员充值信息
//            applicationEventPublisher.publishEvent(new TransactionCommitSendMQEvent("new member recharge", rocketmqCustomProperties.getMemberTopic(),
//                    MemberTagsEnum.MEMBER_RECHARGE.name(), recharge));
        }
    }

    /**
     * 获取平台币设置
     *
     * @return 平台币设置
     */
    private CoinSetting getCoinSetting() {
        Setting setting = settingService.get(SettingEnum.COIN_SETTING.name());
        return new Gson().fromJson(setting.getSettingValue(), CoinSetting.class);
    }

    /**
     * 充值会员
     * @param recharge
     */
    public void rechargeMember(Recharge recharge) {
        if(recharge.getRechargeType() == 1){
            Member member = memberService.getById(recharge.getMemberId());
            //判断用户是否首次成功会员充值
            if (isFirstPaidRecharge(member.getId())) {
                //更新为VIP会员
                member.setIsVip(1);
                memberService.updateById(member);
                //获取平台币设置
                CoinSetting coinSetting = getCoinSetting();
                BigDecimal rechargeMoney = coinSetting.getRecharge();
                //赠送50平台币（余额）
//                memberService.updateMemberCoin(coinSetting.getRecharge(), CoinTypeEnum.INCREASE.name(), member.getId(), "会员首次VIP会员充值，赠送平台币" + coinSetting.getRecharge() + "币");
                //增加50预存款余额
                memberWalletService.increase(new MemberWalletUpdateDTO(rechargeMoney.doubleValue(), recharge.getMemberId(), "会员首次VIP会员充值，赠送平台币，充值单号为：" + recharge.getRechargeSn(), DepositServiceTypeEnum.WALLET_RECHARGE.name()));
            }
        }
    }

    /**
     * 判断用户是否首次成功会员充值
     * @param memberId 会员ID
     * @return true=首次会员充值 false=非首次会员充值
     */
    private boolean isFirstPaidRecharge(String memberId) {
        long paidCount = this.count(new LambdaQueryWrapper<Recharge>()
                .eq(Recharge::getMemberId, memberId)
                .eq(Recharge::getRechargeType, 1)
                .eq(Recharge::getPayStatus, PayStatusEnum.PAID.name())
        );
        // 已支付订单数量 ≤ 1 代表首次充值（包含当前刚支付的这笔）
        return paidCount <= 1;
    }

    @Override
    public Recharge getRecharge(String sn) {
        Recharge recharge = this.getOne(new QueryWrapper<Recharge>().eq("recharge_sn", sn));
        if (recharge != null) {
            return recharge;
        }
        throw new ServiceException(ResultCode.ORDER_NOT_EXIST);
    }


    @Override
    public void rechargeOrderCancel(String sn) {
        Recharge recharge = this.getOne(new QueryWrapper<Recharge>().eq("recharge_sn", sn));
        if (recharge != null) {
            recharge.setPayStatus(PayStatusEnum.CANCEL.name());
            this.updateById(recharge);
        }
    }

    @Override
    public Double getRecharge(Date[] dates, PaymentMethodEnum paymentMethodEnum) {
        LambdaQueryWrapper<Recharge> queryWrapper = new LambdaQueryWrapper<Recharge>();
        queryWrapper.eq(Recharge::getPayStatus, PayStatusEnum.PAID.name());
        queryWrapper.between(Recharge::getPayTime, dates[0], dates[1]);
        if(Objects.nonNull(paymentMethodEnum)){
            queryWrapper.eq(Recharge::getRechargeWay,paymentMethodEnum.name());
        }
        return this.baseMapper.getRecharge(queryWrapper);
    }

    @Override
    public boolean isOrderFinished(String rechargeSn) {
        Recharge recharge = this.getRecharge(rechargeSn);
        if (recharge == null) {
            return false;
        }
        // 已支付 → 回调不再处理
        return PayStatusEnum.PAID.name().equals(recharge.getPayStatus());
    }
}