package cn.lili.modules.store.entity.dto;

import cn.hutool.core.text.CharSequenceUtil;
import cn.lili.common.vo.PageVO;
import cn.lili.modules.store.entity.enums.BillStatusEnum;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 结算单搜索参数
 *
 * @author Chopper
 * @since 2021/3/17 6:08 下午
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BillSearchParams extends PageVO {

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "起始日期")
    private String startDate;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "结束日期")
    private String endDate;

    @Schema(description = "账单号")
    private String sn;

    /**
     * @see BillStatusEnum
     */
    @Schema(description = "状态：OUT(已出账),CHECK(已对账),EXAMINE(已审核),PAY(已付款)")
    private String billStatus;

    @Schema(description = "流水类型")
    private String flowType;

    @Schema(description = "店铺名称")
    private String storeName;

    @Schema(description = "店铺ID")
    private String storeId;

    @Schema(description = "支付方式")
    private String paymentName;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "起始日期")
    private Date startTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "起始日期")
    private Date endTime;

    public <T> QueryWrapper<T> queryWrapper() {
        QueryWrapper<T> wrapper = new QueryWrapper<>();

        //创建时间
        if (CharSequenceUtil.isNotEmpty(startDate) && CharSequenceUtil.isNotEmpty(endDate)) {
            wrapper.between("bill_time", startDate, endDate);
        } else if (CharSequenceUtil.isNotEmpty(startDate)) {
            wrapper.ge("bill_time", startDate);
        } else if (CharSequenceUtil.isNotEmpty(endDate)) {
            wrapper.le("bill_time", endDate);
        }
        //账单号
        wrapper.eq(CharSequenceUtil.isNotEmpty(sn), "sn", sn);
        //结算状态
        wrapper.eq(CharSequenceUtil.isNotEmpty(billStatus), "bill_status", billStatus);
        //店铺名称
        wrapper.eq(CharSequenceUtil.isNotEmpty(storeName), "store_name", storeName);
        //按卖家查询
        wrapper.eq(CharSequenceUtil.isNotEmpty(storeId),
                "store_id", storeId);

        //按卖家查询
        wrapper.eq(CharSequenceUtil.isNotEmpty(paymentName),
                "payment_name", paymentName);

        wrapper.eq(CharSequenceUtil.isNotEmpty(flowType), "flow_type", flowType);
        if (startTime != null && endTime != null) {
            wrapper.between("bill_time", startTime, endTime);
        } else if (startTime != null) {
            wrapper.ge("bill_time", startTime);
        } else if (endTime != null) {
            wrapper.le("bill_time", endTime);
        }

        return wrapper;
    }

    public <T> QueryWrapper<T> queryWrapperBillList() {
        QueryWrapper<T> wrapper = new QueryWrapper<>();

        //创建时间
        if (CharSequenceUtil.isNotEmpty(startDate) && CharSequenceUtil.isNotEmpty(endDate)) {
            wrapper.between("create_time", startDate, endDate);
        } else if (CharSequenceUtil.isNotEmpty(startDate)) {
            wrapper.ge("create_time", startDate);
        } else if (CharSequenceUtil.isNotEmpty(endDate)) {
            wrapper.le("create_time", endDate);
        }
        //账单号
        wrapper.eq(CharSequenceUtil.isNotEmpty(sn), "sn", sn);
        //结算状态
        wrapper.eq(CharSequenceUtil.isNotEmpty(billStatus), "bill_status", billStatus);
        //店铺名称
        wrapper.eq(CharSequenceUtil.isNotEmpty(storeName), "store_name", storeName);
        //按卖家查询
        wrapper.eq(CharSequenceUtil.isNotEmpty(storeId),
                "store_id", storeId);

        //按卖家查询
        wrapper.eq(CharSequenceUtil.isNotEmpty(paymentName),
                "payment_name", paymentName);

        wrapper.eq(CharSequenceUtil.isNotEmpty(flowType), "flow_type", flowType);
        if (startTime != null && endTime != null) {
            wrapper.between("create_time", startTime, endTime);
        } else if (startTime != null) {
            wrapper.ge("create_time", startTime);
        } else if (endTime != null) {
            wrapper.le("create_time", endTime);
        }

        return wrapper;
    }

}
