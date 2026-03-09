package cn.lili.modules.member.entity.dto;

import cn.lili.common.validation.Phone;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

/**
 * 会员地址DTO
 *
 * @author Bulbasaur
 * @since 2020/12/14 16:31
 */
@Data
public class MemberAddressDTO {

    @NotEmpty(message = "收货人姓名不能为空")
    @Schema(description = "收货人姓名")
    private String consigneeName;

    @Phone
    @Schema(description = "手机号码")
    private String consigneeMobile;

    @NotBlank(message = "地址不能为空")
    @Schema(description = "地址名称， '，'分割")
    private String consigneeAddressPath;

    @NotBlank(message = "地址不能为空")
    @Schema(description = "地址id，'，'分割 ")
    private String consigneeAddressIdPath;

    @NotEmpty(message = "详细地址不能为空")
    @Schema(description = "详细地址")
    private String consigneeDetail;
}
