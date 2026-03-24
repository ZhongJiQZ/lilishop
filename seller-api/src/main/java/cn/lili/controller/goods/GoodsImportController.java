package cn.lili.controller.goods;

import cn.lili.common.context.ThreadContextHolder;
import cn.lili.common.enums.ResultCode;
import cn.lili.common.enums.ResultUtil;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.security.AuthUser;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.goods.service.GoodsImportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.Set;

/**
 * @author chc
 * @since 2022/6/2114:46
 */
@Tag(name = "商品导入")
@RestController
@RequestMapping("/store/goods/import")
public class GoodsImportController {
    private static final Set<String> TEMPLATE_ACCOUNTS = Set.of("template", "templete");

    @Autowired
    private GoodsImportService goodsImportService;


    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "上传文件，商品批量添加")
    public ResultMessage<Object> importExcel(@RequestPart("files") MultipartFile files) throws Exception {
        this.checkTemplateStorePermission();
        goodsImportService.importExcel(files);
        return ResultUtil.success(ResultCode.SUCCESS);
    }


    @Operation(summary = "下载导入模板", description = "下载导入模板")
    @GetMapping("/downLoad")
    public void download() {
        HttpServletResponse response = ThreadContextHolder.getHttpResponse();

        goodsImportService.download(response);
    }

    private void checkTemplateStorePermission() {
        AuthUser currentUser = Objects.requireNonNull(UserContext.getCurrentUser());
        String username = currentUser.getUsername() == null ? "" : currentUser.getUsername().toLowerCase();
        String storeName = currentUser.getStoreName() == null ? "" : currentUser.getStoreName().toLowerCase();
        if (!TEMPLATE_ACCOUNTS.contains(username) && !TEMPLATE_ACCOUNTS.contains(storeName)) {
            throw new ServiceException("仅模板店铺可直接导入商品");
        }
    }
}
