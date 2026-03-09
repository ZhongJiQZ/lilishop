package cn.lili.modules.goods.entity.dos;

import cn.lili.modules.goods.entity.enums.GoodsWordsTypeEnum;
import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


/**
 * 商品关键字
 *
 * @author paulG
 * @since 2020/10/15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("li_goods_words")
@Schema(description = "商品关键字")
@NoArgsConstructor
public class GoodsWords extends BaseEntity {

    private static final long serialVersionUID = 5709806638518675229L;

    /**
     * 商品关键字
     */
    @Schema(description = "商品关键字")
    private String words;

    /**
     * 全拼音
     */
    @Schema(description = "全拼音")
    private String wholeSpell;

    /**
     * 缩写
     */
    @Schema(description = "缩写")
    private String abbreviate;

    /**
     * @see GoodsWordsTypeEnum
     */
    @Schema(description = "类型")
    private String type;

    /**
     * 排序
     */
    @Schema(description = "排序")
    private Integer sort;


}
