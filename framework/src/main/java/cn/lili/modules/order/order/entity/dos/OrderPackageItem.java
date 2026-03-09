package cn.lili.modules.order.order.entity.dos;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author chc
 * @since 2022/6/2114:46
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("li_order_package_item")
@Schema(description = "订单分包裹详情")
@NoArgsConstructor
@AllArgsConstructor
public class OrderPackageItem extends BaseEntity {

    @Schema(description = "包裹单号")
    private String packageNo;

    @Schema(description = "订单编号")
    private String orderSn;

    @Schema(description = "子订单编号")
    private String orderItemSn;

    @Schema(description = "商品名称")
    private String goodsName;

    @Schema(description = "商品图片")
    private String thumbnail;

    @Schema(description = "已发货数量")
    private Integer deliverNumber;

    @Schema(description = "送货时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date logisticsTime;

}
