package cn.lili.modules.im.serviceimpl;

import cn.lili.common.utils.StringUtils;
import cn.lili.modules.im.entity.dos.ImMemberIncome;
import cn.lili.modules.im.mapper.ImMemberIncomeMapper;
import cn.lili.modules.im.service.ImMemberIncomeService;
import cn.lili.modules.member.entity.dto.ImMemberIncomePageDTO;
import cn.lili.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 聊天打赏 业务实现
 *
 * @author lensing
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ImMemberIncomeServiceImpl extends ServiceImpl<ImMemberIncomeMapper, ImMemberIncome> implements ImMemberIncomeService {
    @Override
    public IPage<ImMemberIncome> queryImMemberIncomeByParams(ImMemberIncomePageDTO page) {
        LambdaQueryWrapper<ImMemberIncome> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(page.getMemberId())) {
            queryWrapper.like(ImMemberIncome::getMemberId, page.getMemberId());
        }
        if (StringUtils.isNotEmpty(page.getMemberName())) {
            queryWrapper.like(ImMemberIncome::getMemberName, page.getMemberName());
        }
        queryWrapper.orderByDesc(ImMemberIncome::getCreateTime);
        return this.page(PageUtil.initPage(page), queryWrapper);
    }
}