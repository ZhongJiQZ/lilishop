package cn.lili.modules.store.entity.dos;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 店铺自提点
 *
 * @author Bulbasaur
 * @since 2020/12/7 15:09
 */
@Data
@TableName("li_store_address")
@Schema(description = "店铺自提点")
@EqualsAndHashCode(callSuper = false)
public class StoreAddress extends BaseEntity {

    @Schema(description = "店铺id", hidden = true)
    private String storeId;

    @NotEmpty
    @Schema(description = "自提点名称")
    private String addressName;

    @Schema(description = "经纬度")
    @NotEmpty
    private String center;

    @Schema(description = "地址")
    private String address;

    @Schema(description = "电话")
    private String mobile;

}
