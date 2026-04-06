package cn.lili.modules.eid.service;

import cn.lili.modules.eid.entity.dos.MemberEidRecord;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 会员E证通核身记录业务层
 *
 * @author lensing
 * @since 2026-04-04 15:18:56
 */
public interface MemberEidRecordService extends IService<MemberEidRecord> {

    /**
     * 是否已有 E 证通核身成功记录
     */
    boolean hasSuccessfulVerification(String memberId);
}
