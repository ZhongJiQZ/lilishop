package cn.lili.modules.store.entity.vos;

import cn.lili.modules.store.entity.dto.StoreEditDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 店铺详细VO
 *
 * @author pikachu
 * @since 2020-03-09 21:53:20
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StoreDetailVO extends StoreEditDTO {

    @Schema(description = "会员名称")
    private String memberName;

}
