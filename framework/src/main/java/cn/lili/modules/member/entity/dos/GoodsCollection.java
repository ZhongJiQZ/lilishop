package cn.lili.modules.member.entity.dos;

import cn.lili.mybatis.BaseIdEntity;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 会员商品收藏
 *
 * @author Chopper
 * @since 2020/11/18 3:31 下午
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Schema(description = "会员商品收藏")
@TableName("li_goods_collection")
public class GoodsCollection extends BaseIdEntity {

    @CreatedDate
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间", hidden = true)
    private Date createTime;

    @Schema(description = "会员id")
    private String memberId;

    @Schema(description = "商品id")
    private String skuId;

    public GoodsCollection(String memberId, String goodsId) {
        this.memberId = memberId;
        this.skuId = goodsId;
    }

}