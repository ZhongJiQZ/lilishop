package cn.lili.modules.member.mapper;

import cn.lili.modules.member.entity.dos.MemberCoinsHistory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * 会员平台币历史数据处理层
 *
 * @author lensing
 * @since 2026-03-25 14:10:16
 */
public interface MemberCoinsHistoryMapper extends BaseMapper<MemberCoinsHistory> {

    /**
     * 获取所有用户的平台币历史VO
     *
     * @param coinType 平台币类型
     * @return
     */
    @Select("SELECT SUM( variable_coin ) FROM li_member_coins_history WHERE coin_type = #{coinType}")
    Long getALLMemberCoinsHistoryVO(String coinType);

    /**
     * 获取用户的平台币数量
     *
     * @param coinType 平台币类型
     * @param memberId  会员ID
     * @return 平台币数量
     */
    @Select("SELECT SUM( variable_coin ) FROM li_member_coins_history WHERE coin_type = #{coinType} AND member_id=#{memberId}")
    Long getMemberCoinsHistoryVO(String coinType, String memberId);


}