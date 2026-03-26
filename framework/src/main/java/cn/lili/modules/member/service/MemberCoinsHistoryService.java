package cn.lili.modules.member.service;

import cn.lili.common.vo.PageVO;
import cn.lili.modules.member.entity.dos.MemberCoinsHistory;
import cn.lili.modules.member.entity.vo.MemberCoinsHistoryVO;
import cn.lili.modules.member.entity.vo.MemberCoinsStatisticsVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 会员平台币历史业务层
 *
 * @author lensing
 * @since 2026-03-25 14:10:16
 */
public interface MemberCoinsHistoryService extends IService<MemberCoinsHistory> {

    /**
     * 获取会员平台币VO
     *
     * @param memberId 会员ID
     * @return 会员平台币VO
     */
    MemberCoinsHistoryVO getMemberCoinsHistoryVO(String memberId);

    /**
     * 会员平台币历史
     *
     * @param page       分页
     * @param memberId   会员ID
     * @param memberName 会员名称
     * @return 平台币历史分页
     */
    IPage<MemberCoinsHistory> MemberCoinsHistoryList(PageVO page, String memberId, String memberName);


    MemberCoinsStatisticsVO queryMemberCoinsStatistics();

}