package cn.lili.modules.procurement.serviceimpl;

import cn.lili.modules.procurement.entity.dos.InventoryCountItem;
import cn.lili.modules.procurement.mapper.InventoryCountItemMapper;
import cn.lili.modules.procurement.service.InventoryCountItemService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 盘点单明细业务实现
 * 提供按盘点单ID分页及列表查询
 * @author Bulbasaur
 * @since 2025-12-18
 */
@Service
public class InventoryCountItemServiceImpl extends ServiceImpl<InventoryCountItemMapper, InventoryCountItem> implements InventoryCountItemService {

    @Override
    public IPage<InventoryCountItem> pageByCountId(String countId, Page<InventoryCountItem> page) {
        LambdaQueryWrapper<InventoryCountItem> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(InventoryCountItem::getCountId, countId);
        wrapper.orderByDesc(InventoryCountItem::getCreateTime);
        return this.page(page, wrapper);
    }

    @Override
    public List<InventoryCountItem> listByCountId(String countId) {
        LambdaQueryWrapper<InventoryCountItem> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(InventoryCountItem::getCountId, countId);
        return this.list(wrapper);
    }
}
