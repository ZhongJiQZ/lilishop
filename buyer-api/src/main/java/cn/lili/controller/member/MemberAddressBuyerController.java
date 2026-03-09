package cn.lili.controller.member;

import cn.lili.common.enums.ResultUtil;
import cn.lili.common.security.OperationalJudgment;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.vo.PageVO;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.member.entity.dos.MemberAddress;
import cn.lili.modules.member.service.MemberAddressService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Objects;


/**
 * 买家端,会员地址接口
 *
 * @author Bulbasaur
 * @since 2020/11/16 10:07 下午
 */
@RestController
@Tag(name = "买家端,会员地址接口")
@RequestMapping("/buyer/member/address")
public class MemberAddressBuyerController {

    /**
     * 会员收件地址
     */
    @Autowired
    private MemberAddressService memberAddressService;

    @Operation(summary = "获取会员收件地址分页列表")
    @GetMapping
    public ResultMessage<IPage<MemberAddress>> page(PageVO page) {
        return ResultUtil.data(memberAddressService.getAddressByMember(page, UserContext.getCurrentUser().getId()));
    }

    @Operation(summary = "根据ID获取会员收件地址")
    @Parameter(name = "id", description = "会员地址ID")
    @GetMapping("/get/{id}")
    public ResultMessage<MemberAddress> getShippingAddress(@PathVariable String id) {
        return ResultUtil.data(memberAddressService.getMemberAddress(id));
    }

    @Operation(summary = "获取当前会员默认收件地址")
    @GetMapping("/get/default")
    public ResultMessage<MemberAddress> getDefaultShippingAddress() {
        return ResultUtil.data(memberAddressService.getDefaultMemberAddress());
    }

    @Operation(summary = "新增会员收件地址")
    @PostMapping
    public ResultMessage<MemberAddress> addShippingAddress(@Valid MemberAddress shippingAddress) {
        //添加会员地址
        shippingAddress.setMemberId(Objects.requireNonNull(UserContext.getCurrentUser()).getId());
        if(Objects.isNull(shippingAddress.getIsDefault())){
            shippingAddress.setIsDefault(false);
        }
        return ResultUtil.data(memberAddressService.saveMemberAddress(shippingAddress));
    }

    @Operation(summary = "修改会员收件地址")
    @PutMapping
    public ResultMessage<MemberAddress> editShippingAddress(@Valid MemberAddress shippingAddress) {
        OperationalJudgment.judgment(memberAddressService.getById(shippingAddress.getId()));
        shippingAddress.setMemberId(Objects.requireNonNull(UserContext.getCurrentUser()).getId());
        return ResultUtil.data(memberAddressService.updateMemberAddress(shippingAddress));
    }

    @Operation(summary = "删除会员收件地址")
    @Parameter(name = "id", description = "会员地址ID")
    @DeleteMapping("/delById/{id}")
    public ResultMessage<Object> delShippingAddressById(@PathVariable String id) {
        OperationalJudgment.judgment(memberAddressService.getById(id));
        memberAddressService.removeMemberAddress(id);
        return ResultUtil.success();
    }

}
