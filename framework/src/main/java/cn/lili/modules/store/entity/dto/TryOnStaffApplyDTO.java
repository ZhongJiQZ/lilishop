package cn.lili.modules.store.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 试穿员入驻 / E 证通前置资料（买家端提交）
 */
@Data
public class TryOnStaffApplyDTO {

    @NotBlank(message = "试穿员姓名不能为空")
    @Length(min = 2, max = 32)
    @Schema(description = "试穿员姓名", example = "李明", requiredMode = Schema.RequiredMode.REQUIRED)
    private String realName;

    @NotBlank(message = "试穿员昵称不能为空")
    @Length(min = 2, max = 64)
    @Schema(description = "试穿员昵称", example = "喜宝", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nickname;

    @NotNull(message = "身高不能为空")
    @Min(50)
    @Max(260)
    @Schema(description = "身高(cm)", example = "180", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer height;

    @NotNull(message = "体重不能为空")
    @Min(20)
    @Max(500)
    @Schema(description = "体重(斤)", example = "150", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer weight;

    @NotBlank(message = "职业不能为空")
    @Length(min = 1, max = 64)
    @Schema(description = "职业", example = "主播", requiredMode = Schema.RequiredMode.REQUIRED)
    private String occupation;

    @NotBlank(message = "试穿员简介不能为空")
    @Length(min = 2, max = 500)
    @Schema(description = "试穿员简介", example = "主播", requiredMode = Schema.RequiredMode.REQUIRED)
    private String intro;

    @Pattern(regexp = "^$|^\\d{17}[\\dXxX]$", message = "试穿员证件号须为18位身份证号")
    @Schema(description = "试穿员证件号（可空；获取 E 证通链接前须为18位合法号）", example = "300521199805040056")
    private String idCard;

    @NotBlank(message = "联系电话不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "联系电话格式有误")
    @Schema(description = "联系电话", example = "19959777555", requiredMode = Schema.RequiredMode.REQUIRED)
    private String phone;

    @NotBlank(message = "试穿员照片不能为空")
    @Schema(description = "试穿员照片 URL", requiredMode = Schema.RequiredMode.REQUIRED)
    private String photoUrl;

    @NotBlank(message = "证件电子版不能为空")
    @Schema(description = "证件电子版 URL", requiredMode = Schema.RequiredMode.REQUIRED)
    private String idCardImageUrl;
}
