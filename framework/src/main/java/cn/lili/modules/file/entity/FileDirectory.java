package cn.lili.modules.file.entity;

import cn.lili.common.security.enums.UserEnums;
import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("li_file_directory")
@Schema(description = "文件目录")
@EqualsAndHashCode(callSuper = false)
public class FileDirectory extends BaseEntity {

    /**
     * @see UserEnums
     */
    @Schema(description = "文件目录类型")
    private String directoryType;
    @Schema(description = "拥有者名称")
    private String directoryName;
    @Schema(description = "拥有者id")
    private String ownerId;
    @Schema(description = "父分类ID")
    private String parentId;

    @Schema(description = "层级")
    @NotNull(message = "层级不能为空")
    @Min(value = 0, message = "层级最小为0")
    @Max(value = 2, message = "层级最大为2")
    private Integer level;
}
