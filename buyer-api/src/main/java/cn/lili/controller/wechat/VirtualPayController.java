package cn.lili.controller.wechat;

import cn.lili.common.enums.ResultUtil;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.wallet.entity.dos.Recharge;
import cn.lili.modules.wallet.service.RechargeService;
import cn.lili.modules.wechat.service.WechatVirtualPayService;
import com.alibaba.fastjson.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/buyer/virtual/pay")
public class VirtualPayController {

    @Autowired
    private RechargeService rechargeService;
    @Autowired
    private WechatVirtualPayService wechatVirtualPayService;

    /**
     * 获取虚拟支付拉起参数
     * 前端只传 rechargeSn，不用传 sessionKey
     */
    @PostMapping("/getPayParams")
    public ResultMessage<Map<String, Object>> getPayParams(@RequestParam String rechargeSn) {
        Recharge recharge = rechargeService.getRecharge(rechargeSn);
        Map<String, Object> payParams = wechatVirtualPayService.getPayParams(recharge);
        return ResultUtil.data(payParams);
    }

    /**
     * 微信虚拟支付回调地址
     */
    @PostMapping("/notify")
    public Map<String, Object> notify(HttpServletRequest request) {
        try {
            BufferedReader reader = request.getReader();
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            String body = sb.toString();
            JSONObject json = JSONObject.parseObject(body);

            // 1. 必传参数
            String outTradeNo = json.getString("OutTradeNo");
            String transactionId = json.getString("TransactionId");
            String offerId = json.getString("OfferId");
            String productId = json.getString("ProductId");
            int buyQuantity = json.getInteger("BuyQuantity");

            // 2. 幂等判断：已处理过直接返回成功
            if (rechargeService.isOrderFinished(outTradeNo)) {
                return Map.of("ErrCode",0,"ErrMsg","success");
            }

            // 3. 执行业务发货：到账余额/会员
            wechatVirtualPayService.handleNotify(outTradeNo, transactionId);

            // 4. 米大师标准发货返回结构（必须按这个格式）
            Map<String,Object> resp = new HashMap<>();
            resp.put("ErrCode", 0);
            resp.put("ErrMsg", "success");
            // 履约发货字段
            resp.put("OrderNo", outTradeNo);
            resp.put("TradeNo", transactionId);
            resp.put("DeliveryResult", 1); // 1=发货成功
            return resp;
        } catch (Exception e) {
            log.error("虚拟支付回调处理异常",e);
            // 失败返回非0，米大师会重试
            return Map.of("ErrCode", -1, "ErrMsg", "fail");
        }
    }
}