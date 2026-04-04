package cn.lili.modules.eid.serviceimpl;

import cn.lili.modules.eid.entity.dos.MemberEidRecord;
import cn.lili.modules.eid.mapper.MemberEidRecordMapper;
import cn.lili.modules.eid.service.MemberEidRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 圈子帖子业务层实现
 *
 * @author lensing
 * @since 2026-03-09 15:18:56
 */
@Slf4j
@Service
public class MemberEidRecordServiceImpl extends ServiceImpl<MemberEidRecordMapper, MemberEidRecord> implements MemberEidRecordService {
}
