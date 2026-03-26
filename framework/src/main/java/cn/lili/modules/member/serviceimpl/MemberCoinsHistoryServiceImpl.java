package cn.lili.modules.member.serviceimpl;


import cn.hutool.core.text.CharSequenceUtil;
import cn.lili.common.vo.PageVO;
import cn.lili.modules.member.entity.dos.Member;
import cn.lili.modules.member.entity.dos.MemberCoinsHistory;
import cn.lili.modules.member.entity.vo.MemberCoinsHistoryVO;
import cn.lili.modules.member.entity.vo.MemberCoinsStatisticsVO;
import cn.lili.modules.member.mapper.MemberMapper;
import cn.lili.modules.member.mapper.MemberCoinsHistoryMapper;
import cn.lili.modules.member.service.MemberCoinsHistoryService;
import cn.lili.modules.member.service.MemberService;
import cn.lili.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 会员平台币历史业务层实现
 *
 * @author lensing
 * @since 2026-03-25 14:10:16
 */
@Service
public class MemberCoinsHistoryServiceImpl extends ServiceImpl<MemberCoinsHistoryMapper, MemberCoinsHistory> implements MemberCoinsHistoryService {


    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberMapper memberMapper;

    @Override
    public MemberCoinsHistoryVO getMemberCoinsHistoryVO(String memberId) {
        //获取会员平台币历史
        Member member = memberService.getById(memberId);
        MemberCoinsHistoryVO memberCoinsHistoryVO = new MemberCoinsHistoryVO();
        if (member != null) {
            memberCoinsHistoryVO.setCoin(member.getCoin());
            memberCoinsHistoryVO.setTotalCoin(member.getTotalCoin());
            return memberCoinsHistoryVO;
        }
        return new MemberCoinsHistoryVO();
    }

    @Override
    public IPage<MemberCoinsHistory> MemberCoinsHistoryList(PageVO page, String memberId, String memberName) {
        LambdaQueryWrapper<MemberCoinsHistory> lambdaQueryWrapper = new LambdaQueryWrapper<MemberCoinsHistory>()
                .eq(CharSequenceUtil.isNotEmpty(memberId), MemberCoinsHistory::getMemberId, memberId)
                .like(CharSequenceUtil.isNotEmpty(memberName), MemberCoinsHistory::getMemberName, memberName);
        //如果排序为空，则默认创建时间倒序
        if (CharSequenceUtil.isEmpty(page.getSort())) {
            page.setSort("createTime");
            page.setOrder("desc");
        }
        return this.page(PageUtil.initPage(page), lambdaQueryWrapper);
    }

    @Override
    public MemberCoinsStatisticsVO queryMemberCoinsStatistics() {
        return memberMapper.queryMemberCoinsStatistics();
    }

}