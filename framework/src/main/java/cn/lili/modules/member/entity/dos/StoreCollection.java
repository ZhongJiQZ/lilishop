package cn.lili.modules.member.entity.dos;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


/**
 * 会员店铺收藏
 *
 * @author Chopper
 * @since 2020/11/18 3:32 下午
 */
@Data
@TableName("li_store_collection")
@Schema(description = "会员收藏")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class StoreCollection extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "会员id")
    private String memberId;

    @Schema(description = "店铺id")
    private String storeId;


}