package cn.lili.modules.circle.serviceimpl;

import cn.lili.common.enums.ResultCode;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.security.AuthUser;
import cn.lili.common.security.context.UserContext;
import cn.lili.modules.circle.entity.dos.CirclePostFollow;
import cn.lili.modules.circle.entity.dto.CirclePostFollowSearchParams;
import cn.lili.modules.circle.entity.vos.CirclePostFollowVO;
import cn.lili.modules.circle.mapper.CirclePostFollowMapper;
import cn.lili.modules.circle.service.CirclePostFollowService;
import cn.lili.modules.store.entity.dos.Store;
import cn.lili.modules.store.mapper.StoreMapper;
import cn.lili.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 店铺粉丝业务层实现
 *
 * @author lensing
 * @since 2026-03-25 15:18:56
 */
@Service
public class CirclePostFollowServiceImpl extends ServiceImpl<CirclePostFollowMapper, CirclePostFollow> implements CirclePostFollowService {

    /**
     * 店铺
     */
    @Autowired
    private StoreMapper storeMapper;

    @Override
    public List<CirclePostFollowVO> getFollowCirclePostList(String circlePostId) {
        List<CirclePostFollowVO> followCirclePostList = this.baseMapper.getFollowCirclePostList(circlePostId);
        return followCirclePostList;
    }

    @Override
    public void addStoreFollow(String storeId) {
        AuthUser tokenUser = UserContext.getCurrentUser();
        if (tokenUser == null) {
            throw new ServiceException(ResultCode.USER_NOT_LOGIN);
        }
        Store store = storeMapper.selectById(storeId);
        if(store == null){
            throw new ServiceException(ResultCode.STORE_NOT_EXIST);
        }
        CirclePostFollow circlePostFollow = new CirclePostFollow();
        circlePostFollow.setMemberId(tokenUser.getId());
        circlePostFollow.setFollowedMemberId(store.getMemberId());
        this.baseMapper.insert(circlePostFollow);
    }

    @Override
    public void unfollowStore(String storeId) {
        AuthUser tokenUser = UserContext.getCurrentUser();
        if (tokenUser == null) {
            throw new ServiceException(ResultCode.USER_NOT_LOGIN);
        }
        Store store = storeMapper.selectById(storeId);
        if(store == null){
            throw new ServiceException(ResultCode.STORE_NOT_EXIST);
        }
        CirclePostFollow circlePostFollow = this.baseMapper.selectOne(new QueryWrapper<CirclePostFollow>()
                .eq("member_id", store.getMemberId())
                .eq("followed_member_id", store.getMemberId())
        );
        if(circlePostFollow != null){
            this.baseMapper.deleteById(circlePostFollow.getId());
        }
    }

    @Override
    public IPage<CirclePostFollowVO> queryByParams(CirclePostFollowSearchParams followSearchParams) {
        AuthUser tokenUser = UserContext.getCurrentUser();
        if (tokenUser == null) {
            throw new ServiceException(ResultCode.USER_NOT_LOGIN);
        }
        QueryWrapper queryWrapper = followSearchParams.queryWrapper();
        queryWrapper.eq("member_id", tokenUser.getId());
        return this.baseMapper.getCirclePostFollowList(PageUtil.initPage(followSearchParams), queryWrapper);
    }

}
