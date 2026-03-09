package cn.lili.modules.system.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 搜索热词
 *
 * @author Bulbasaur
 * @since 2021/5/16 11:10 下午
 */
@Data
public class HotWordsSetting implements Serializable {

    //热词1-5，默认分数1-5

    @Schema(description = "热词默认配置")
    private List<HotWordsSettingItem> hotWordsSettingItems = new ArrayList<>();


    @Schema(description = "每日保存数量")
    private Integer saveNum;

}
