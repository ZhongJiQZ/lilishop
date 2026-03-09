package cn.lili.modules.order.aftersale.entity.dos;

import cn.lili.common.security.enums.UserEnums;
import cn.lili.mybatis.BaseIdEntity;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 售后日志
 *
 * @author Bulbasaur
 * @since 2020-03-25 2:30 下午
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("li_after_sale_log")
@Schema(description = "售后日志")
@NoArgsConstructor
public class AfterSaleLog extends BaseIdEntity {

    @CreatedBy
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建者", hidden = true)
    private String createBy;

    @CreatedDate
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间", hidden = true)
    private Date createTime;

    @Schema(description = "售后服务单号")
    private String sn;

    @Schema(description = "操作者id(可以是卖家)")
    private String operatorId;

    /**
     * @see UserEnums
     */
    @Schema(description = "操作者类型")
    private String operatorType;


    @Schema(description = "操作者名称")
    private String operatorName;

    @Schema(description = "日志信息")
    private String message;

    public AfterSaleLog(String sn, String operatorId, String operatorType, String operatorName, String message) {
        this.sn = sn;
        this.operatorId = operatorId;
        this.operatorType = operatorType;
        this.operatorName = operatorName;
        this.message = message;
    }
}
