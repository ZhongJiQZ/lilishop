package cn.lili.modules.goods.entity.vos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 规格值
 *
 * @author Chopper
 * @since 2020-02-26 23:24:13
 */
@Data
public class SpecValueVO implements Serializable {

    private static final long serialVersionUID = -4433579132929428572L;

    @Schema(description = "规格项名字")
    private String specName;

    @Schema(description = "规格值")
    private String specValue;

    @Schema(description = "该规格是否有图片，1 有 0 没有")
    private Integer specType;
    /**
     * 规格图片
     */
    @Schema(description = "规格的图片")
    private List<String> specImage;
}
