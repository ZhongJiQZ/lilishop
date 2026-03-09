package cn.lili.controller.other;

import cn.lili.common.enums.ResultUtil;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.page.entity.dos.PageData;
import cn.lili.modules.page.entity.dto.PageDataDTO;
import cn.lili.modules.page.entity.enums.PageEnum;
import cn.lili.modules.page.entity.vos.PageDataVO;
import cn.lili.modules.page.service.PageDataService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 买家端,页面接口
 *
 * @author Chopper
 * @since 2020/11/16 10:08 下午
 */
@RestController
@Tag(name = "买家端,页面接口")
@RequestMapping("/buyer/other/pageData")
public class PageBuyerController {

    /**
     * 页面管理
     */
    @Autowired
    private PageDataService pageService;

    @Operation(description = "获取首页数据")
    @Parameter(name = "clientType", description = "客户端类型", required = true)
    @GetMapping("/getIndex")
    public ResultMessage<PageDataVO> getIndex(@RequestParam String clientType) {
        PageDataDTO pageDataDTO = new PageDataDTO(PageEnum.INDEX.name());
        pageDataDTO.setPageClientType(clientType);
        PageDataVO pageDataVO=pageService.getPageData(pageDataDTO);
        return ResultUtil.data(pageDataVO);
    }

    @Operation(summary = "获取页面数据")
    @GetMapping
    public ResultMessage<PageDataVO> get(PageDataDTO pageDataDTO) {
        PageDataVO pageDataVO=pageService.getPageData(pageDataDTO);
        return ResultUtil.data(pageDataVO);
    }

    @Operation(description = "获取店铺首页")
    @Parameter(name = "clientType", description = "客户端类型", required = true)
    @Parameter(name = "storeId", description = "店铺ID", required = true)
    @GetMapping("/getStore")
    public ResultMessage<PageDataVO> getShopPage(@RequestParam String clientType,String storeId) {
        PageDataDTO pageDataDTO = new PageDataDTO(PageEnum.STORE.name());
        pageDataDTO.setPageClientType(clientType);
        pageDataDTO.setNum(storeId);
        PageDataVO pageDataVO=pageService.getPageData(pageDataDTO);
        return ResultUtil.data(pageDataVO);
    }

    @Operation(description = "获取页面数据")
    @Parameter(name = "id", description = "id", required = true)
    @GetMapping("/get/{id}")
    public ResultMessage<PageData> getPage(@PathVariable("id") String id) {
        return ResultUtil.data(pageService.getSpecial(id));
    }

    @Operation(description = "获取专题页面数据（根据消息内容得知）")
    @GetMapping("/getSpecial")
    public ResultMessage<PageData> getSpecial(@RequestParam String body) {
        String name = "";
        if (body.indexOf("』") >= 0 && body.indexOf("『") >= 0) {
            name = body.substring(body.indexOf("『") + 1, body.lastIndexOf("』"));
        } else if (body.indexOf("〉") >= 0 && body.indexOf("〈") >= 0) {
            name = body.substring(body.indexOf("〈") + 1, body.lastIndexOf("〉"));
        } else if (body.indexOf("」") >= 0 && body.indexOf("「") >= 0) {
            name = body.substring(body.indexOf("「") + 1, body.lastIndexOf("」"));
        } else if (body.indexOf("》") >= 0 && body.indexOf("《") >= 0) {
            name = body.substring(body.indexOf("《") + 1, body.lastIndexOf("》"));
        } else if (body.indexOf("）") >= 0 && body.indexOf("（") >= 0) {
            name = body.substring(body.indexOf("（") + 1, body.lastIndexOf("）"));
        } else if (body.indexOf("】") >= 0 && body.indexOf("【") >= 0) {
            name = body.substring(body.indexOf("【") + 1, body.lastIndexOf("】"));
        } else if (body.indexOf("｝") >= 0 && body.indexOf("｛") >= 0) {
            name = body.substring(body.indexOf("｛") + 1, body.lastIndexOf("｝"));
        } else if (body.indexOf("！") >= 0) {
            name = body.substring(body.indexOf("！") + 1, body.lastIndexOf("！"));
        } else if (body.indexOf("｜") >= 0) {
            name = body.substring(body.indexOf("｜") + 1, body.lastIndexOf("｜"));
        }

        PageData pageData = pageService.getOne(
                new LambdaQueryWrapper<PageData>()
                        .eq(PageData::getPageType, PageEnum.SPECIAL.name())
                        .eq(PageData::getName, name));
        return ResultUtil.data(pageData);

    }
}
