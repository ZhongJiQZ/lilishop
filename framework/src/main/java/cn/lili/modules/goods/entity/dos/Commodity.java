package cn.lili.modules.goods.entity.dos;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 小程序直播商品
 *
 * @author Bulbasaur
 * @since 2021/5/17 9:34 上午
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "直播商品")
@TableName("li_commodity")
public class Commodity extends BaseEntity {

    @Schema(description = "图片")
    private String goodsImage;

    @Schema(description = "商品名称")
    private String name;

    /**
     * 1：一口价（只需要传入price，price2不传）
     * 2：价格区间（price字段为左边界，price2字段为右边界，price和price2必传）
     * 3：显示折扣价（price字段为原价，price2字段为现价， price和price2必传
     */
    @Schema(description = "价格类型")
    private Integer priceType;

    @Schema(description = "价格")
    private Double price;

    @Schema(description = "价格2")
    private Double price2;

    @Schema(description = "商品详情页的小程序路径")
    private String url;

    @Schema(description = "微信程序直播商品ID")
    private Integer liveGoodsId;

    @Schema(description = "审核单ID")
    private String auditId;

    @Schema(description = "审核状态")
    private String auditStatus;

    @Schema(description = "店铺ID")
    private String storeId;

    @Schema(description = "商品ID")
    private String goodsId;

    @Schema(description = "规格ID")
    private String skuId;

}
