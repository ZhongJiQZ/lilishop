package cn.lili.modules.member.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 会员店铺收藏VO
 *
 * @author pikachu
 * @since 2020-02-25 14:10:16
 */
@Data
public class StoreCollectionVO {

    @Schema(description = "店铺id")
    private String id;

    @Schema(description = "店铺名称")
    private String storeName;

    @Schema(description = "店铺Logo")
    private String storeLogo;

    @Schema(description = "是否自营")
    private Boolean selfOperated;
}
