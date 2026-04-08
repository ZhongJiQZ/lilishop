package cn.lili.modules.eid.serviceimpl;

import cn.lili.common.utils.StringUtils;
import cn.lili.modules.eid.entity.dos.MemberEidRecord;
import cn.lili.modules.eid.entity.dto.MemberEidRecordPageDTO;
import cn.lili.modules.eid.mapper.MemberEidRecordMapper;
import cn.lili.modules.eid.service.MemberEidRecordService;
import cn.lili.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 会员E证通核身记录业务层实现
 *
 * @author lensing
 * @since 2026-03-09 15:18:56
 */
@Slf4j
@Service
public class MemberEidRecordServiceImpl extends ServiceImpl<MemberEidRecordMapper, MemberEidRecord> implements MemberEidRecordService {

    @Override
    public boolean hasSuccessfulVerification(String memberId) {
        return count(new LambdaQueryWrapper<MemberEidRecord>()
                .eq(MemberEidRecord::getMemberId, memberId)
                .eq(MemberEidRecord::getStatus, "SUCCESS")) > 0;
    }

    @Override
    public IPage<MemberEidRecord> queryMemberEidRecordByParams(MemberEidRecordPageDTO page) {
        LambdaQueryWrapper<MemberEidRecord> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(page.getMemberId())) {
            queryWrapper.eq(MemberEidRecord::getMemberId, page.getMemberId());
        }
        queryWrapper.orderByDesc(MemberEidRecord::getCreateTime);
        Page<MemberEidRecord> data = this.page(PageUtil.initPage(page), queryWrapper);
        return data;
    }
}
