package cn.lili.modules.procurement.serviceimpl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.NumberUtil;
import cn.lili.common.enums.ResultCode;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.security.AuthUser;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.security.OperationalJudgment;
import cn.lili.common.utils.SnowFlake;
import cn.lili.modules.goods.entity.dto.GoodsSkuStockDTO;
import cn.lili.modules.goods.entity.enums.GoodsStockTypeEnum;
import cn.lili.modules.goods.entity.dos.GoodsSku;
import cn.lili.modules.goods.service.GoodsSkuService;
import cn.lili.modules.procurement.entity.dos.DamageReport;
import cn.lili.modules.procurement.entity.dos.DamageReportItem;
import cn.lili.modules.procurement.entity.dto.DamageReportCreateDTO;
import cn.lili.modules.procurement.entity.dto.DamageReportItemDTO;
import cn.lili.modules.procurement.entity.enums.DamageReportStatusEnum;
import cn.lili.modules.procurement.mapper.DamageReportMapper;
import cn.lili.modules.procurement.service.DamageReportItemService;
import cn.lili.modules.procurement.service.DamageReportService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 报损单业务实现
 * 实现报损单生命周期操作及库存扣减
 * @author Bulbasaur
 * @since 2025-12-18
 */
@Service
public class DamageReportServiceImpl extends ServiceImpl<DamageReportMapper, DamageReport> implements DamageReportService {

    @Autowired
    private DamageReportItemService damageReportItemService;
    @Autowired
    private GoodsSkuService goodsSkuService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DamageReport create(DamageReportCreateDTO dto) {
        AuthUser currentUser = Objects.requireNonNull(UserContext.getCurrentUser());
        DamageReport report = new DamageReport();
        report.setSn(SnowFlake.createStr("DR"));
        report.setStoreId(currentUser.getStoreId());
        report.setStatus(DamageReportStatusEnum.DRAFT.name());
        report.setDamageDate(dto.getDamageDate() != null ? dto.getDamageDate() : new Date());
        report.setDamageReasonId(dto.getDamageReasonId());
        report.setRemark(dto.getRemark());
        report.setEvidence(dto.getEvidence());
        int totalQty = 0;
        double totalAmount = 0D;
        this.save(report);
        List<DamageReportItem> items = new ArrayList<>();
        if (dto.getItems() != null) {
            for (DamageReportItemDTO it : dto.getItems()) {
                DamageReportItem item = new DamageReportItem();
                item.setReportId(report.getId());
                item.setGoodsId(it.getGoodsId());
                item.setSkuId(it.getSkuId());
                item.setQuantity(it.getQuantity());
                item.setUnitPrice(it.getUnitPrice());
                double amount = NumberUtil.mul(Convert.toDouble(it.getUnitPrice()), Convert.toDouble(it.getQuantity()));
                item.setAmount(amount);
                totalQty += Convert.toInt(it.getQuantity());
                totalAmount = NumberUtil.add(totalAmount, amount);
                items.add(item);
            }
            damageReportItemService.saveBatch(items);
        }
        report.setTotalQuantity(totalQty);
        report.setTotalAmount(totalAmount);
        this.updateById(report);
        return report;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DamageReport submit(String id) {
        DamageReport report = OperationalJudgment.judgment(this.getById(id));
        if (!DamageReportStatusEnum.DRAFT.name().equals(report.getStatus())) {
            throw new ServiceException(ResultCode.PARAMS_ERROR);
        }
        report.setStatus(DamageReportStatusEnum.SUBMITTED.name());
        this.updateById(report);
        return report;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DamageReport approve(String id) {
        DamageReport report = OperationalJudgment.judgment(this.getById(id));
        if (!DamageReportStatusEnum.SUBMITTED.name().equals(report.getStatus())) {
            throw new ServiceException(ResultCode.PARAMS_ERROR);
        }
        report.setStatus(DamageReportStatusEnum.APPROVED.name());
        this.updateById(report);
        return report;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DamageReport reject(String id, String remark) {
        DamageReport report = OperationalJudgment.judgment(this.getById(id));
        if (!DamageReportStatusEnum.SUBMITTED.name().equals(report.getStatus())) {
            throw new ServiceException(ResultCode.PARAMS_ERROR);
        }
        report.setRemark(remark);
        report.setStatus(DamageReportStatusEnum.REJECTED.name());
        this.updateById(report);
        return report;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DamageReport cancel(String id) {
        DamageReport report = OperationalJudgment.judgment(this.getById(id));
        if (DamageReportStatusEnum.COMPLETED.name().equals(report.getStatus())) {
            throw new ServiceException(ResultCode.PARAMS_ERROR);
        }
        report.setStatus(DamageReportStatusEnum.CANCELLED.name());
        this.updateById(report);
        return report;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DamageReport complete(String id) {
        DamageReport report = OperationalJudgment.judgment(this.getById(id));
        if (!DamageReportStatusEnum.APPROVED.name().equals(report.getStatus())) {
            throw new ServiceException(ResultCode.PARAMS_ERROR);
        }
        List<DamageReportItem> items = damageReportItemService.listByReportId(report.getId());
        List<GoodsSkuStockDTO> stockDTOS = new ArrayList<>();
        for (DamageReportItem item : items) {
            Integer currentStock = goodsSkuService.getStock(item.getSkuId());
            if (currentStock == null || currentStock < item.getQuantity()) {
                throw new ServiceException(ResultCode.GOODS_SKU_QUANTITY_NOT_ENOUGH);
            }
            GoodsSku goodsSku = goodsSkuService.getGoodsSkuByIdFromCache(item.getSkuId());
            GoodsSkuStockDTO dto = new GoodsSkuStockDTO();
            dto.setGoodsId(goodsSku.getGoodsId());
            dto.setSkuId(item.getSkuId());
            dto.setQuantity(item.getQuantity());
            dto.setType(GoodsStockTypeEnum.SUB.name());
            stockDTOS.add(dto);
        }
        goodsSkuService.updateStocksByType(stockDTOS);
        report.setStatus(DamageReportStatusEnum.COMPLETED.name());
        this.updateById(report);
        return report;
    }
}
