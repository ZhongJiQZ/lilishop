package cn.lili.modules.system.entity.dos;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 物流公司设置
 *
 * @author Chopper
 * @since 2020/11/17 8:01 下午
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("li_logistics")
@Schema(description = "物流公司")
public class Logistics extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @NotEmpty(message = "物流公司名称必填")
    @Schema(description = "物流公司名称")
    private String name;

    @NotEmpty(message = "物流公司code必填")
    @Schema(description = "物流公司code")
    private String code;

    @Schema(description = "支持电子面单")
    private String standBy;

    @Schema(description = "物流公司电子面单表单")
    private String formItems;

    @Schema(description = "禁用状态 OPEN：开启，CLOSE：禁用")
    private String disabled;
}