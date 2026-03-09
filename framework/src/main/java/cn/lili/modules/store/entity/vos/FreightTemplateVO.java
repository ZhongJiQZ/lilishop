package cn.lili.modules.store.entity.vos;

import cn.lili.modules.store.entity.dos.FreightTemplate;
import cn.lili.modules.store.entity.dos.FreightTemplateChild;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 店铺运费模板
 *
 * @author Bulbasaur
 * @since 2020/11/24 14:29
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FreightTemplateVO extends FreightTemplate {

    private static final long serialVersionUID = 2422138942308945537L;

    @Schema(description = "运费详细规则")
    private List<FreightTemplateChild> freightTemplateChildList;

}
