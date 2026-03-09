package cn.lili.mybatis;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


/**
 * 租户超级类
 *
 * @author Chopper
 * @version v1.0
 * @since 2020/8/20 14:34
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseTenantEntity extends BaseEntity {

    @Schema(description = "租户id", hidden = true)
    private String tenantId;

}
