package cn.lili.modules.member.entity.dto;

import cn.hutool.core.text.CharSequenceUtil;
import cn.lili.common.vo.PageVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author chc
 * @since 2022/6/2114:46
 */
@Data
@Schema
@EqualsAndHashCode(callSuper = false)
public class FootPrintQueryParams extends PageVO {

    @Schema(description = "用户Id")
    private String memberId;

    @Schema(description = "店铺Id")
    private String storeId;

    public <T> QueryWrapper<T> queryWrapper() {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        if (CharSequenceUtil.isNotEmpty(memberId)) {
            queryWrapper.eq("member_id", memberId);
        }
        if (CharSequenceUtil.isNotEmpty(storeId)) {
            queryWrapper.eq("store_id", storeId);
        }
        queryWrapper.eq("delete_flag",false);
        queryWrapper.orderByDesc("create_time");
        return queryWrapper;
    }
}
