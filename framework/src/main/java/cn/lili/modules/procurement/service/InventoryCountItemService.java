package cn.lili.modules.procurement.service;

import cn.lili.modules.procurement.entity.dos.InventoryCountItem;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 盘点单明细业务接口
 * 提供按盘点单分页查询明细与导出
 * @author Bulbasaur
 * @since 2025-12-18
 */
public interface InventoryCountItemService extends IService<InventoryCountItem> {
    /**
     * 按盘点单分页查询明细
     * @param countId 盘点单ID
     * @param page 分页参数
     * @return 明细分页数据
     */
    IPage<InventoryCountItem> pageByCountId(String countId, Page<InventoryCountItem> page);
    /**
     * 按盘点单查询全部明细（用于导出）
     * @param countId 盘点单ID
     * @return 明细列表
     */
    java.util.List<InventoryCountItem> listByCountId(String countId);
}
