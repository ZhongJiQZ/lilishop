package cn.lili.modules.order.order.entity.dos;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


/**
 * 交易投诉通信
 *
 * @author paulG
 * @since 2020/12/5
 **/
@Data
@TableName("li_order_complaint_communication")
@Schema(description = "订单交易投诉通信")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class OrderComplaintCommunication extends BaseEntity {

    private static final long serialVersionUID = -2384351827382795547L;

    /**
     * 投诉id
     */
    @Schema(description = "投诉id")
    private String complainId;
    /**
     * 对话内容
     */
    @Schema(description = "对话内容")
    private String content;
    /**
     * 所属，买家/卖家
     */
    @Schema(description = "所属，买家/卖家")
    private String owner;
    /**
     * 对话所属名称
     */
    @Schema(description = "对话所属名称")
    private String ownerName;
    /**
     * 对话所属id,卖家id/买家id
     */
    @Schema(description = "对话所属id,卖家id/买家id")
    private String ownerId;



}
