package cn.lili.modules.goods.entity.dos;

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

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("li_category_parameter")
@Schema(description = "商品分类参数关联")
@NoArgsConstructor
public class CategoryParameter extends BaseIdEntity {

    private static final long serialVersionUID = -8590218282691127746L;

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

    @TableField(value = "category_id")
    @Schema(description = "分类id")
    private String categoryId;

    @TableField(value = "parameter_id")
    @Schema(description = "参数id")
    private String parameterId;

    public CategoryParameter(String categoryId, String parameterId) {
        this.categoryId = categoryId;
        this.parameterId = parameterId;
    }
}

