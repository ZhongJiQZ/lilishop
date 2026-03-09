package cn.lili.modules.procurement.serviceimpl;

import cn.hutool.core.text.CharSequenceUtil;
import cn.lili.modules.procurement.entity.dos.StockReason;
import cn.lili.modules.procurement.mapper.StockReasonMapper;
import cn.lili.modules.procurement.service.StockReasonService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 出入库原因业务实现
 * 提供出入库原因的分页查询、增删改能力
 * @author Bulbasaur
 * @since 2025-12-18
 */
@Service
public class StockReasonServiceImpl extends ServiceImpl<StockReasonMapper, StockReason> implements StockReasonService {
    @Override
    public IPage<StockReason> page(String reason, String category, Page<StockReason> page) {
        LambdaQueryWrapper<StockReason> wrapper = new LambdaQueryWrapper<>();
        if (CharSequenceUtil.isNotEmpty(reason)) {
            wrapper.like(StockReason::getReason, reason);
        }
        if (CharSequenceUtil.isNotEmpty(category)) {
            wrapper.eq(StockReason::getCategory, category);
        }
        wrapper.orderByDesc(StockReason::getCreateTime);
        return this.page(page, wrapper);
    }
}
