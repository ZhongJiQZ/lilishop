package cn.lili.modules.procurement.service;

import cn.lili.modules.procurement.entity.dos.DamageReport;
import cn.lili.modules.procurement.entity.dto.DamageReportCreateDTO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 报损单业务接口
 * 定义报损单的生命周期操作
 * @author Bulbasaur
 * @since 2025-12-18
 */
public interface DamageReportService extends IService<DamageReport> {
    /**
     * 创建报损单
     * @param dto 报损单创建参数
     * @return 创建后的报损单
     */
    DamageReport create(DamageReportCreateDTO dto);
    /**
     * 提交报损单
     * @param id 报损单ID
     * @return 提交后的报损单
     */
    DamageReport submit(String id);
    /**
     * 审批通过报损单
     * @param id 报损单ID
     * @return 审批通过后的报损单
     */
    DamageReport approve(String id);
    /**
     * 审批拒绝报损单
     * @param id 报损单ID
     * @param remark 拒绝备注
     * @return 审批拒绝后的报损单
     */
    DamageReport reject(String id, String remark);
    /**
     * 撤销报损单
     * @param id 报损单ID
     * @return 撤销后的报损单
     */
    DamageReport cancel(String id);
    /**
     * 完成报损单
     * @param id 报损单ID
     * @return 完成后的报损单
     */
    DamageReport complete(String id);
}
