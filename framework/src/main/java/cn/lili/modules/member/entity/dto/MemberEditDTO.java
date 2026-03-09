package cn.lili.modules.member.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 会员信息修改DTO
 *
 * @author Bulbasaur
 * @since 2020/12/11 14:39
 */
@Data
public class MemberEditDTO {


    @Schema(description = "昵称", requiredMode = Schema.RequiredMode.REQUIRED)
    @Size(min = 2, max = 20, message = "会员昵称必须为2到20位之间")
    private String nickName;

    @Schema(description = "会员地址ID")
    private String regionId;

    @Schema(description = "会员地址")
    private String region;

    @Min(message = "必须为数字且1为男,0为女", value = 0)
    @Max(message = "必须为数字且1为男,0为女", value = 1)
    @NotNull(message = "会员性别不能为空")
    @Schema(description = "会员性别,1为男，0为女", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer sex;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "会员生日")
    private Date birthday;

    @Schema(description = "详细地址")
    private String address;

    @Schema(description = "会员头像")
    private String face;

}
