package cn.lili.modules.goods.entity.vos;


import cn.lili.modules.goods.entity.dto.GoodsParamsDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品关联参数的VO
 *
 * @author pikachu
 * @since 2020-02-26 23:24:13
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class GoodsParamsVO extends GoodsParamsDTO {

    private static final long serialVersionUID = -4904700751774005326L;
    @Schema(description = "1 输入项   2 选择项")
    private Integer paramType;
    @Schema(description = " 选择项的内容获取值，使用optionList")
    private String options;
    @Schema(description = "是否必填是  1    否   0")
    private Integer required;
    @Schema(description = "参数组id")
    private String groupId;
    @Schema(description = "是否可索引  1 可以   0不可以")
    private Integer isIndex;

    private String[] optionList;

    public void setOptionList(String[] optionList) {
        this.optionList = optionList;
    }

    public String[] getOptionList() {
        if (options != null) {
            return options.replaceAll("\r|\n", "").split(",");
        }
        return optionList;
    }


}
