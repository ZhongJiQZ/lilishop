package cn.lili.common.vo;

import cn.hutool.core.text.CharSequenceUtil;
import cn.lili.common.utils.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 查询参数
 *
 * @author Chopper
 */
@Data
public class PageVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "页号")
    private Integer pageNumber = 1;

    @Schema(description = "页面大小")
    private Integer pageSize = 10;

    @Schema(description = "排序字段")
    private String sort;

    @Schema(description = "排序方式 asc/desc")
    private String order;

    @Schema(description = "需要驼峰转换蛇形", example = "一般不做处理，如果数据库中就是蛇形，则这块需要处理。")
    private Boolean notConvert;

    public String getSort() {
        if (CharSequenceUtil.isNotEmpty(sort)) {
            if (notConvert == null || Boolean.FALSE.equals(notConvert)) {
                return StringUtils.camel2Underline(sort);
            } else {
                return sort;
            }
        }
        return sort;
    }

}
