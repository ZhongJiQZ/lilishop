package cn.lili.controller.member;

import cn.lili.common.enums.ResultCode;
import cn.lili.common.enums.ResultUtil;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.security.AuthUser;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.im.entity.dos.ImTalk;
import cn.lili.modules.im.service.ImTalkService;
import cn.lili.modules.permission.entity.dos.AdminUser;
import cn.lili.modules.permission.service.AdminUserService;
import cn.lili.modules.store.entity.dos.Store;
import cn.lili.modules.store.service.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端,会员接口
 *
 * @author Bulbasaur
 * @since 2020-02-25 14:10:16
 */
@RestController
@Tag(name = "管理端,会员接口")
@RequestMapping("/manager/member/user")
public class MemberIMManagerController {
    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private ImTalkService talkService;
    @Autowired
    private StoreService storeService;

    @GetMapping("")
    @Operation(summary = "获取当前登录管理员接口")
    public ResultMessage<AdminUser> getAdminInfo() {
        AuthUser tokenUser = UserContext.getCurrentUser();
        if (tokenUser != null) {
            AdminUser adminUser = adminUserService.getById(tokenUser.getId());
            return ResultUtil.data(adminUser);
        }
        throw new ServiceException(ResultCode.USER_NOT_LOGIN);
    }

    @GetMapping("/store")
    @Operation(summary = "获取当前管理员代理店铺接口")
    public ResultMessage<Store> getAdminStoreInfo(String talkId) {
        ImTalk talk = talkService.getById(talkId);
        Store store = null;
        if (Boolean.TRUE.equals(talk.getStoreFlag1())) {
            store = storeService.getById(talk.getUserId1());
            return ResultUtil.data(store);
        } else if (Boolean.TRUE.equals(talk.getStoreFlag2())) {
            store = storeService.getById(talk.getUserId2());
            return ResultUtil.data(store);
        }
        throw new ServiceException(ResultCode.USER_NOT_LOGIN);
    }
}
