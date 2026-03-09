package cn.lili.modules.procurement.service;

import cn.lili.modules.procurement.entity.dos.InventoryCount;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.lili.common.vo.PageVO;

/**
 * 盘点单业务接口
 * 定义盘点单创建与分页查询
 * @author Bulbasaur
 * @since 2025-12-18
 */
public interface InventoryCountService extends IService<InventoryCount> {
    /**
     * 创建盘点单
     * @return 新建的盘点单
     */
    InventoryCount create();
    /**
     * 盘点单分页查询
     * @param pageVO 分页参数
     * @return 盘点单分页数据
     */
    IPage<InventoryCount> page(PageVO pageVO);
}
