package cn.lili.modules.member.entity.dos;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


/**
 * 浏览历史
 *
 * @author Chopper
 * @since 2020/11/17 7:22 下午
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("li_foot_print")
@Schema(description = "浏览历史")
@NoArgsConstructor
@AllArgsConstructor
public class FootPrint extends BaseEntity {

    private static final long serialVersionUID = 1L;


    @Schema(description = "会员ID")
    private String memberId;

    @Schema(description = "店铺Id")
    private String storeId;

    @Schema(description = "商品ID")
    private String goodsId;

    @Schema(description = "规格ID")
    private String skuId;

}