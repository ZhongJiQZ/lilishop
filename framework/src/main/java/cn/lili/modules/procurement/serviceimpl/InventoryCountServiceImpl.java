package cn.lili.modules.procurement.serviceimpl;

import cn.hutool.core.text.CharSequenceUtil;
import cn.lili.common.enums.ResultCode;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.security.AuthUser;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.utils.SnowFlake;
import cn.lili.common.vo.PageVO;
import cn.lili.modules.goods.entity.dos.GoodsSku;
import cn.lili.modules.goods.service.GoodsSkuService;
import cn.lili.modules.procurement.entity.dos.InventoryCount;
import cn.lili.modules.procurement.entity.dos.InventoryCountItem;
import cn.lili.modules.procurement.mapper.InventoryCountMapper;
import cn.lili.modules.procurement.mapper.InventoryCountItemMapper;
import cn.lili.modules.procurement.service.InventoryCountService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.lili.mybatis.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 盘点单业务实现
 * 生成盘点单并快照SKU库存，提供分页查询
 * @author Bulbasaur
 * @since 2025-12-18
 */
@Service
public class InventoryCountServiceImpl extends ServiceImpl<InventoryCountMapper, InventoryCount> implements InventoryCountService {

    @Autowired
    private GoodsSkuService goodsSkuService;
    @Autowired
    private InventoryCountItemMapper inventoryCountItemMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public InventoryCount create() {
        AuthUser user = UserContext.getCurrentUser();
        if (user == null || CharSequenceUtil.isEmpty(user.getStoreId())) {
            throw new ServiceException(ResultCode.USER_AUTHORITY_ERROR);
        }
        InventoryCount count = new InventoryCount();
        count.setSn(SnowFlake.createStr("IC"));
        count.setStoreId(user.getStoreId());
        count.setMakerId(user.getClerkId());
        count.setMakerName(user.getUsername());
        count.setCountTime(new Date());
        this.save(count);

        LambdaQueryWrapper<GoodsSku> wrapper = Wrappers.<GoodsSku>lambdaQuery().eq(GoodsSku::getStoreId, user.getStoreId());
        List<GoodsSku> skus = goodsSkuService.list(wrapper);
        List<InventoryCountItem> items = new ArrayList<>();
        for (GoodsSku sku : skus) {
            InventoryCountItem item = new InventoryCountItem();
            item.setCountId(count.getId());
            item.setGoodsId(sku.getGoodsId());
            item.setSkuId(sku.getId());
            item.setGoodsName(sku.getGoodsName());
            item.setSkuName(sku.getSimpleSpecs());
            item.setMarketEnable(sku.getMarketEnable());
            Integer stock = goodsSkuService.getStock(sku.getId());
            item.setQuantity(stock == null ? 0 : stock);
            items.add(item);
        }
        if (!items.isEmpty()) {
            // 使用Mapper批量插入以保持风格与现有实现一致
            for (InventoryCountItem item : items) {
                inventoryCountItemMapper.insert(item);
            }
        }
        count.setItemTotal(items.size());
        this.updateById(count);
        return count;
    }

    @Override
    public IPage<InventoryCount> page(PageVO pageVO) {
        AuthUser user = UserContext.getCurrentUser();
        LambdaQueryWrapper<InventoryCount> wrapper = Wrappers.lambdaQuery();
        if (user != null && CharSequenceUtil.isNotEmpty(user.getStoreId())) {
            wrapper.eq(InventoryCount::getStoreId, user.getStoreId());
        }
        wrapper.orderByDesc(InventoryCount::getCreateTime);
        return this.page(PageUtil.initPage(pageVO), wrapper);
    }
}
