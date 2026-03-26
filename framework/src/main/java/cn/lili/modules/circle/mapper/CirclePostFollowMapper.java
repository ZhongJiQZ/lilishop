package cn.lili.modules.circle.mapper;

import cn.lili.modules.circle.entity.dos.CirclePostFollow;
import cn.lili.modules.circle.entity.vos.CirclePostFollowVO;
import cn.lili.modules.store.entity.vos.StoreVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 店铺粉丝数据处理层
 *
 * @author lensing
 * @since 2026-03-25 15:18:56
 */
public interface CirclePostFollowMapper extends BaseMapper<CirclePostFollow> {

    /**
     * 根据圈子帖子id查店铺粉丝列表
     *
     * @param circlePostId 圈子帖子id
     * @return 圈子帖子绑定的店铺粉丝列表
     */
    @Select("SELECT face,nick_name FROM li_circle_post_follow m left join li_member n on n.id=m.member_id WHERE followed_member_id=(SELECT user_id FROM li_circle_post WHERE id=#{circlePostId})")
    List<CirclePostFollowVO> getFollowCirclePostList(String circlePostId);

    /**
     * 根据店铺id查粉丝列表
     *
     * @param storeId 店铺ID
     * @return 店铺绑定的粉丝列表
     */
    @Select("SELECT face,nick_name FROM li_circle_post_follow m left join li_member n on n.id=m.member_id WHERE followed_member_id=(SELECT member_id FROM li_store WHERE id=#{storeId})")
    IPage<CirclePostFollowVO> getFollowStoreList(IPage<StoreVO> page, String storeId);
}