package cn.lili.modules.circle.service;

import cn.lili.modules.circle.entity.dos.CirclePostFollow;
import cn.lili.modules.circle.entity.vos.CirclePostFollowVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 圈子帖子用户关注业务层
 *
 * @author lensing
 * @since 2026-03-25 15:18:56
 */
public interface CirclePostFollowService extends IService<CirclePostFollow> {

    /**
     * 根据圈子帖子id查询用户关注信息
     *
     * @param circlePostId 圈子帖子id
     * @return 圈子帖子关联用户关注列表
     */
    List<CirclePostFollowVO> getFollowCirclePostList(String circlePostId);

    /**
     * 店铺关注
     * @param storeId
     */
    void addStoreFollow(String storeId);

    void unfollowStore(String storeId);
}
