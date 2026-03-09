package cn.lili.modules.store.entity.dos;

import cn.lili.modules.store.entity.dto.StoreLogisticsCustomerDTO;
import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 店铺-物流公司设置
 *
 * @author Chopper
 * @since 2020/11/17 8:01 下午
 */
@Data
@TableName("li_store_logistics")
@Schema(description = "店铺-物流公司")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class StoreLogistics extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "店铺ID")
    private String storeId;

    @Schema(description = "物流公司ID")
    @NotNull
    private String logisticsId;

    @Schema(description = "电子面单客户账户、月结账号、客户代码")
    private String customerName;

    @Schema(description = "客户密码、电子面单密码")
    private String customerPwd;

    @Schema(description = "电子面单密钥")
    private String monthCode;

    @Schema(description = "归属网点/网点编码,电子面单承载编号")
    private String sendSite;

    @Schema(description = "收件快递员")
    private String sendStaff;

    @Schema(description = "是否使用电子面单")
    private boolean faceSheetFlag;

    @Schema(description = "支付方式")
    private String payType;

    @Schema(description = "快递类型")
    private String expType;

    @Schema(description = "电子面单客户账户名称")
    private String partnerName;


    public StoreLogistics(StoreLogisticsCustomerDTO storeLogisticsCustomerDTO) {
        this.customerName = storeLogisticsCustomerDTO.getCustomerName();
        this.customerPwd = storeLogisticsCustomerDTO.getCustomerPwd();
        this.sendSite = storeLogisticsCustomerDTO.getSendSite();
        this.sendStaff = storeLogisticsCustomerDTO.getSendStaff();
        this.monthCode = storeLogisticsCustomerDTO.getMonthCode();
        this.faceSheetFlag = storeLogisticsCustomerDTO.isFaceSheetFlag();
        this.payType = storeLogisticsCustomerDTO.getPayType();
        this.expType = storeLogisticsCustomerDTO.getExpType();
        this.partnerName = storeLogisticsCustomerDTO.getPartnerName();
    }


}