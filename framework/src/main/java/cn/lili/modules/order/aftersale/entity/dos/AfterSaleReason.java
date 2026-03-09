package cn.lili.modules.order.aftersale.entity.dos;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 售后原因
 *
 * @author Bulbasaur
 * @since 2021/7/9 1:39 上午
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("li_after_sale_reason")
@Schema(description = "售后原因")
public class AfterSaleReason extends BaseEntity {

    @NotNull
    @Schema(description = "售后原因")
    private String reason;

    /**
     * @see cn.lili.modules.order.trade.entity.enums.AfterSaleTypeEnum
     */
    @Schema(description = "原因类型", allowableValues = "CANCEL,RETURN_GOODS,RETURN_MONEY,COMPLAIN")
    @NotNull
    private String serviceType;

}
