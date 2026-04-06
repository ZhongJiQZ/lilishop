package cn.lili.controller.store;

import cn.hutool.core.text.CharSequenceUtil;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.security.AuthUser;
import cn.lili.common.security.context.UserContext;
import cn.lili.modules.member.entity.dos.Clerk;
import cn.lili.modules.member.entity.dos.Member;
import cn.lili.modules.member.service.ClerkService;
import cn.lili.modules.member.service.MemberService;
import cn.lili.modules.store.entity.dos.Store;
import cn.lili.modules.store.service.StoreService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 买家端「商户工作台」当前店铺解析：兼容 MEMBER 体系下
 * 仅手机号店主、微信登录、店员等多种账号形态。
 */
@Component
public class BuyerMerchantStoreSupport {

    @Autowired
    private ClerkService clerkService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private StoreService storeService;

    /**
     * 解析当前登录用户对应的店铺 ID（用于订单、商品等商户接口）。
     */
    public String requireCurrentStoreId() {
        AuthUser user = Objects.requireNonNull(UserContext.getCurrentUser());
        if (CharSequenceUtil.isNotEmpty(user.getStoreId())) {
            return user.getStoreId();
        }
        String memberId = user.getId();
        Clerk clerk = clerkService.getClerkByMemberId(memberId);
        if (clerk != null && Boolean.TRUE.equals(clerk.getStatus())
                && CharSequenceUtil.isNotEmpty(clerk.getStoreId())) {
            return clerk.getStoreId();
        }
        Member member = memberService.getById(memberId);
        if (member != null && Boolean.TRUE.equals(member.getHaveStore())
                && CharSequenceUtil.isNotEmpty(member.getStoreId())) {
            return member.getStoreId();
        }
        Store store = storeService.getOne(
                new LambdaQueryWrapper<Store>().eq(Store::getMemberId, memberId), false);
        if (store == null) {
            throw new ServiceException("当前账号未绑定店铺");
        }
        return store.getId();
    }
}
