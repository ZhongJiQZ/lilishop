package cn.lili.controller.setting;

import cn.hutool.json.JSONUtil;
import cn.lili.cache.Cache;
import cn.lili.common.enums.ResultCode;
import cn.lili.common.enums.ResultUtil;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.system.entity.dos.Setting;
import cn.lili.modules.system.entity.dto.RechargeAmountSetting;
import cn.lili.modules.system.entity.dto.RechargePopupSetting;
import cn.lili.modules.system.entity.enums.SettingEnum;
import cn.lili.modules.system.service.SettingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 买家端,系统设置接口
 *
 * @author Chopper
 * @since 2020/11/26 15:53
 */
@RestController
@Tag(name = "买家端,系统设置接口")
@RequestMapping("/buyer/setting/setting")
public class SettingBuyerController {
    @Autowired
    private SettingService settingService;
    /**
     * 缓存
     */
    @Autowired
    private Cache<String> cache;

    @Operation(summary = "查看配置")
    @Parameter(name = "key", description = "配置key", required = true)
    @GetMapping( "/get/{key}")
    public ResultMessage settingGet(@PathVariable String key) {
        return createSetting(key);
    }


    /**
     * 获取表单
     * 这里主要包含一个配置对象为空，导致转换异常问题的处理，解决配置项增加减少，带来的系统异常，无法直接配置
     *
     * @param key
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private ResultMessage createSetting(String key) {
        SettingEnum settingEnum = SettingEnum.valueOf(key);
        cache.remove(key);
        Setting setting = settingService.get(key);
        switch (settingEnum) {
            case RECHARGE_POPUP_SETTING:
                return setting == null ?
                        ResultUtil.data(new RechargePopupSetting()) :
                        ResultUtil.data(JSONUtil.toBean(setting.getSettingValue(), RechargePopupSetting.class));
            case RECHARGE_AMOUNT_SETTING:
                return setting == null ?
                        ResultUtil.data(new RechargeAmountSetting()) :
                        ResultUtil.data(JSONUtil.toBean(setting.getSettingValue(), RechargeAmountSetting.class));
            default:
                throw new ServiceException(ResultCode.SETTING_NOT_TO_SET);
        }
    }
}
