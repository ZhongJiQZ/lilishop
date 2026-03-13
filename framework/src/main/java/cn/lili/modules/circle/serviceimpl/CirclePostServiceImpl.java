package cn.lili.modules.circle.serviceimpl;

import cn.hutool.core.text.CharSequenceUtil;
import cn.lili.common.enums.ResultCode;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.security.AuthUser;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.security.enums.UserEnums;
import cn.lili.common.sensitive.SensitiveWordsFilter;
import cn.lili.modules.circle.entity.dos.CirclePost;
import cn.lili.modules.circle.entity.dto.CirclePostCommentSearchParams;
import cn.lili.modules.circle.entity.dto.CirclePostOperationDTO;
import cn.lili.modules.circle.entity.dto.CirclePostSearchParams;
import cn.lili.modules.circle.entity.vos.CirclePostVO;
import cn.lili.modules.circle.mapper.CirclePostMapper;
import cn.lili.modules.circle.service.CirclePostCommentService;
import cn.lili.modules.circle.service.CirclePostService;
import cn.lili.modules.member.entity.dos.Member;
import cn.lili.modules.member.mapper.MemberMapper;
import cn.lili.modules.system.aspect.annotation.SystemLogPoint;
import cn.lili.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 圈子帖子业务层实现
 *
 * @author lensing
 * @since 2026-03-09 15:18:56
 */
@Service
public class CirclePostServiceImpl extends ServiceImpl<CirclePostMapper, CirclePost> implements CirclePostService {
    /**
     * 帖子评论
     */
    @Autowired
    private CirclePostCommentService circlePostCommentService;
    @Autowired
    private MemberMapper memberMapper;

    @Override
    public IPage<CirclePostVO> queryByParams(CirclePostSearchParams circlePostSearchParams) {
        circlePostSearchParams.setSort("c.create_time");
        circlePostSearchParams.setOrder("desc");
        QueryWrapper queryWrapper = circlePostSearchParams.queryWrapper();
        queryWrapper.eq("c.status", 1);
        IPage<CirclePostVO> circlePostList = this.baseMapper.getCirclePostList(PageUtil.initPage(circlePostSearchParams), queryWrapper);
        circlePostList.getRecords().forEach(circlePost -> {
            circlePost.setContent(SensitiveWordsFilter.filter(circlePost.getContent()));
            CirclePostCommentSearchParams params = new CirclePostCommentSearchParams();
            params.setPostId(circlePost.getContentId());
            circlePost.setCommentList(circlePostCommentService.getCirclePostCommentByList(params));

        });
        return circlePostList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @SystemLogPoint(description = "发布圈子帖子", customerLog = "'新增圈子帖子标题:['+#circlePostOperationDTO.title+']'")
    public void addCirclePost(CirclePostOperationDTO circlePostOperationDTO) {
        AuthUser tokenUser = UserContext.getCurrentUser();
        if (tokenUser == null) {
            throw new ServiceException(ResultCode.USER_NOT_LOGIN);
        }
        CirclePost circlePost = new CirclePost(circlePostOperationDTO);
        circlePost.setUserId(Long.valueOf(tokenUser.getId()));
        //检查圈子帖子
        this.checkCirclePost(circlePost);
        circlePost.setTitle(CharSequenceUtil.sub(circlePostOperationDTO.getContent(), 0, 20));
        //添加圈子帖子
        this.save(circlePost);
    }

    /**
     * 检查圈子帖子信息
     * 判断当前用户是否为店铺
     *
     * @param circlePost 圈子帖子
     */
    private void checkCirclePost(CirclePost circlePost) {
        circlePost.setUserId(circlePost.getUserId());
        Member member = memberMapper.selectById(circlePost.getUserId());
        circlePost.setUserType(member.getStoreId()!=null? UserEnums.STORE.name():UserEnums.MEMBER.name());
        if (member.getStoreId() != null) {
            //判断当前用户是否为店铺
//        if (Objects.requireNonNull(UserContext.getCurrentUser()).getRole().equals(UserEnums.STORE)) {
        } else {
            throw new ServiceException(ResultCode.STORE_NOT_LOGIN_ERROR);
        }
    }
}
