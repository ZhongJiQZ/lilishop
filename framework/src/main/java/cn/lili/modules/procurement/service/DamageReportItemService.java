package cn.lili.modules.procurement.service;

import cn.lili.modules.procurement.entity.dos.DamageReportItem;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 报损单明细业务接口
 * 提供按报损单ID查询明细
 * @author Bulbasaur
 * @since 2025-12-18
 */
public interface DamageReportItemService extends IService<DamageReportItem> {
    /**
     * 按报损单ID查询明细列表
     * @param reportId 报损单ID
     * @return 报损明细列表
     */
    List<DamageReportItem> listByReportId(String reportId);
}
