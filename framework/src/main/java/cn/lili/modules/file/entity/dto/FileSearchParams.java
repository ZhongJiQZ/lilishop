package cn.lili.modules.file.entity.dto;

import cn.lili.common.vo.PageVO;
import cn.lili.common.vo.SearchVO;
import cn.lili.modules.file.entity.File;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class FileSearchParams extends PageVO {

    @Schema(description = "文件")
    private File file;
    @Schema(description = "搜索VO")
    private SearchVO searchVO;
    @Schema(description = "文件夹ID")
    private String fileDirectoryId;
}
