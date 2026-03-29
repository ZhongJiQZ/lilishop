package cn.lili.modules.circle.entity.dto;

import cn.lili.common.vo.PageVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * 店铺粉丝查询条件
 *
 * @author lensing
 * @since 2026-03-09 15:18:56
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CirclePostFollowSearchParams extends PageVO {

    private static final long serialVersionUID = 2544015852728566887L;

    @Schema(description = "粉丝名称")
    private String nickName;

    public <T> QueryWrapper<T> queryWrapper() {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();

        // 统一使用 CharSequenceUtil.isNotEmpty() 处理字符串
//        if (CharSequenceUtil.isNotEmpty(nickName)) {
//            queryWrapper.eq("post_id", nickName);
//        }

//        queryWrapper.eq("delete_flag", false);
        queryWrapper.orderByAsc("create_time");
        return queryWrapper;
    }


}
