package cn.lili.modules.system.entity.dos;

import cn.lili.mybatis.BaseIdEntity;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


/**
 * app历史版本维护
 *
 * @author zh
 * @since 2020-06-20 09:29:19
 */
@Data
@TableName("li_app_version")
@Schema(description = "app版本控制")
@EqualsAndHashCode(callSuper = false)
public class AppVersion extends BaseIdEntity {

    private static final long serialVersionUID = 3034686331756935L;

    @CreatedDate
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间", hidden = true)
    private Date createTime;

    @CreatedBy
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建者", hidden = true)
    private String createBy;

    @Schema(description = "版本号")
    private String version;

    @Schema(description = "版本名称")
    private String versionName;

    @Schema(description = "更新内容")
    private String content;

    @Schema(description = "是否强制更新")
    private Boolean forceUpdate;

    @Schema(description = "下载地址")
    private String downloadUrl;

    /**
     * @see cn.lili.modules.system.entity.enums.AppType
     */
    @Schema(description = "类型")
    private String type;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "版本更新时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date versionUpdateDate;

}