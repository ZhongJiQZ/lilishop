package cn.lili.controller.eid;

import cn.lili.common.enums.ResultUtil;
import cn.lili.common.utils.TencentEidUtil;
import cn.lili.common.vo.ResultMessage;
import com.tencentcloudapi.faceid.v20180301.models.GetEidResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 买家端,E证通接口
 *
 * @author lensing
 * @since 2026/04/03 10:05 下午
 */
@RestController
@Tag(name = "买家端,E证通接口")
@RequestMapping("/buyer/eid")
public class EidController {

    private final TencentEidUtil tencentEidUtil;

    public EidController(TencentEidUtil tencentEidUtil) {
        this.tencentEidUtil = tencentEidUtil;
    }

    @Operation(summary = "获取E证通认证链接")
    @GetMapping("/getToken")
    public ResultMessage<TencentEidUtil.EidTokenResult> getToken(String name, String idCard) {
        TencentEidUtil.EidTokenResult result = tencentEidUtil.getEidToken(name, idCard);
        return ResultUtil.data(result);
    }

    @Operation(summary = "查询认证结果")
    @GetMapping("/getResult")
    public ResultMessage<GetEidResultResponse> getResult(String eidToken) {
        GetEidResultResponse response = tencentEidUtil.getEidResult(eidToken);
        return ResultUtil.data(response);
    }
}
