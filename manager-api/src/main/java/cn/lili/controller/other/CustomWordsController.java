package cn.lili.controller.other;

import cn.hutool.core.text.CharSequenceUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import cn.lili.common.enums.ResultUtil;
import cn.lili.common.vo.PageVO;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.permission.SettingKeys;
import cn.lili.modules.search.entity.dos.CustomWords;
import cn.lili.modules.search.entity.vo.CustomWordsVO;
import cn.lili.modules.search.service.CustomWordsService;
import cn.lili.modules.system.entity.dos.Setting;
import cn.lili.modules.system.service.SettingService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;

/**
 * 管理端,自定义分词接口
 *
 * @author paulG
 * @since 2020/10/16
 **/
@Slf4j
@RestController
@Tag(name = "管理端,自定义分词接口")
@RequestMapping("/manager/other/customWords")
public class CustomWordsController {

    /**
     * 分词
     */
    @Autowired
    private CustomWordsService customWordsService;
    /**
     * 设置
     */
    @Autowired
    private SettingService settingService;

    @GetMapping
    public String getCustomWords(String secretKey) {
        if (CharSequenceUtil.isEmpty(secretKey)) {
            return "";
        }
        Setting setting = settingService.get(SettingKeys.ES_SIGN.name());
        if (setting == null || CharSequenceUtil.isEmpty(setting.getSettingValue())) {
            return "";
        }

        JSONObject jsonObject = JSON.parseObject(setting.getSettingValue());
        //如果密钥不正确，返回空
        if (!secretKey.equals(jsonObject.getString("secretKey"))) {
            return "";
        }

        String res = customWordsService.deploy();
        try {
            return new String(res.getBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("获取分词错误", e);
        }
        return "";
    }

    @Operation(summary = "添加自定义分词")
    @PostMapping
    public ResultMessage<CustomWordsVO> addCustomWords(@Valid CustomWordsVO customWords) {
        customWordsService.addCustomWords(customWords);
        return ResultUtil.data(customWords);
    }

    @Operation(summary = "修改自定义分词")
    @PutMapping
    public ResultMessage<CustomWordsVO> updateCustomWords(@Valid CustomWordsVO customWords) {
        customWordsService.updateCustomWords(customWords);
        return ResultUtil.data(customWords);
    }

    @Operation(summary = "删除自定义分词")
    @DeleteMapping("/{id}")
    public ResultMessage<String> deleteCustomWords(
            @Parameter(description = "文章ID", required = true) @NotNull @PathVariable String id) {
        customWordsService.deleteCustomWords(id);
        return ResultUtil.success();
    }

    @Operation(summary = "分页获取自定义分词")
    @GetMapping("/page")
    public ResultMessage<IPage<CustomWords>> getCustomWords(
            @Parameter(description = "分词", required = true) @RequestParam String words, PageVO pageVo) {
        return ResultUtil.data(customWordsService.getCustomWordsByPage(words, pageVo));
    }

}
