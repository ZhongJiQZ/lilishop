package cn.lili.modules.system.entity.dos;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


/**
 * 设置
 * @author Chopper
 * @since 2020-02-25 14:10:16
 */
@Data
@TableName("li_setting")
@Schema(description = "配置")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Setting extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "配置值value")
    private String settingValue;

}