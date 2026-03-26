package cn.lili.modules.circle.serviceimpl;

import cn.hutool.core.text.CharSequenceUtil;
import cn.lili.common.enums.ResultCode;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.security.AuthUser;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.security.enums.UserEnums;
import cn.lili.common.sensitive.SensitiveWordsFilter;
import cn.lili.common.utils.StringUtils;
import cn.lili.modules.circle.entity.dos.CirclePost;
import cn.lili.modules.circle.entity.dos.CirclePostComment;
import cn.lili.modules.circle.entity.dto.CirclePostCommentSearchParams;
import cn.lili.modules.circle.entity.dto.CirclePostOperationDTO;
import cn.lili.modules.circle.entity.dto.CirclePostPageDTO;
import cn.lili.modules.circle.entity.dto.CirclePostSearchParams;
import cn.lili.modules.circle.entity.vos.CirclePostCommentVO;
import cn.lili.modules.circle.entity.vos.CirclePostVO;
import cn.lili.modules.circle.mapper.CirclePostMapper;
import cn.lili.modules.circle.service.CirclePostCommentService;
import cn.lili.modules.circle.service.CirclePostService;
import cn.lili.modules.member.entity.dos.Member;
import cn.lili.modules.member.mapper.MemberMapper;
import cn.lili.modules.system.aspect.annotation.SystemLogPoint;
import cn.lili.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
            List<CirclePostCommentVO> circlePostCommentByList = circlePostCommentService.getCirclePostCommentByList(params);
            circlePostCommentByList.forEach(circlePostComment -> {
                circlePostComment.setContent(SensitiveWordsFilter.filter(circlePostComment.getContent()));
            });
            circlePost.setCommentList(circlePostCommentByList);

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
        circlePost.setUserId(tokenUser.getId());
        circlePost.setUserType(tokenUser.getRole().name());
        //买家端
        if (CharSequenceUtil.equals(tokenUser.getRole().name(), UserEnums.MEMBER.name())) {
            //检查圈子帖子
            this.checkCirclePost(circlePost);
        }
        //店铺端
        if (CharSequenceUtil.equals(tokenUser.getRole().name(), UserEnums.STORE.name())) {
            circlePost.setStoreId(tokenUser.getStoreId());
        }
        //管理端
        if (CharSequenceUtil.equals(tokenUser.getRole().name(), UserEnums.MANAGER.name())) {
            if(StringUtils.isNotBlank(circlePost.getStoreId())) {
                circlePost.setStoreId(circlePostOperationDTO.getStoreId());
            }
        }
        circlePost.setTitle(CharSequenceUtil.sub(circlePostOperationDTO.getContent(), 0, 20));
        //添加圈子帖子
        this.save(circlePost);
    }

    @Override
    public IPage<CirclePost> queryCirclePostByParams(CirclePostPageDTO page) {
        LambdaQueryWrapper<CirclePost> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(page.getStoreId())) {
            queryWrapper.eq(CirclePost::getStoreId, page.getStoreId());
        }
        if (StringUtils.isNotEmpty(page.getContent())) {
            queryWrapper.like(CirclePost::getContent, page.getContent());
        }
        if (page.getIsHomeShow() != null) {
            queryWrapper.eq(CirclePost::getIsHomeShow, page.getIsHomeShow());
        }
        if (StringUtils.isNotEmpty(page.getUserId())) {
            queryWrapper.eq(CirclePost::getUserId, page.getUserId());
        }
        queryWrapper.orderByDesc(CirclePost::getCreateTime);
        Page<CirclePost> data = this.page(PageUtil.initPage(page), queryWrapper);
        data.getRecords().forEach(circlePost -> {
            circlePost.setContent(SensitiveWordsFilter.filter(circlePost.getContent()));
        });
        return data;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteCirclePosts(List<String> ids) {
        AuthUser tokenUser = UserContext.getCurrentUser();
        if (tokenUser == null) {
            throw new ServiceException(ResultCode.USER_NOT_LOGIN);
        }
        if (ids == null || ids.isEmpty()) {
            throw new ServiceException(ResultCode.PARAMS_ERROR, "帖子ID列表不能为空");
        }
        // 如果是卖家角色，只允许删除自己的帖子
        if (UserEnums.STORE.equals(tokenUser.getRole())) {
            String storeId = tokenUser.getStoreId();
            if (CharSequenceUtil.isBlank(storeId)) {
                throw new ServiceException(ResultCode.STORE_NOT_EXIST, "当前商家信息异常，无法删除帖子");
            }

            // 查询这些帖子中，有多少不是当前商家的
            long notOwnCount = this.lambdaQuery()
                    .in(CirclePost::getId, ids)
                    .ne(CirclePost::getStoreId, storeId)
                    .count();

            if (notOwnCount > 0) {
                throw new ServiceException(ResultCode.CIRCLE_POST_UPDATE_ERROR, "您只能删除自己店铺发布的帖子");
            }
        } else if (UserEnums.MEMBER.equals(tokenUser.getRole())) {
            Member member = memberMapper.selectById(tokenUser.getId());
            String storeId = "";
            if (member != null && member.getHaveStore() && member.getStoreId() != null) {
                storeId = member.getStoreId();
            }
            if (CharSequenceUtil.isBlank(storeId)) {
                throw new ServiceException(ResultCode.STORE_NOT_EXIST, "当前商家信息异常，无法删除帖子");
            }
            // 查询这些帖子中，有多少不是当前商家的
            long notOwnCount = this.lambdaQuery()
                    .in(CirclePost::getId, ids)
                    .ne(CirclePost::getStoreId, storeId)
                    .count();

            if (notOwnCount > 0) {
                throw new ServiceException(ResultCode.CIRCLE_POST_UPDATE_ERROR, "您只能删除自己店铺发布的帖子");
            }
        }
        // 1. 删除帖子
        this.removeByIds(ids);
        // 2. 删除关联的所有评论（使用 in 条件）
        if (!ids.isEmpty()) {
            circlePostCommentService.lambdaUpdate()
                    .in(CirclePostComment::getPostId, ids)
                    .remove();
        }
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
            circlePost.setStoreId(member.getStoreId());
//        if (Objects.requireNonNull(UserContext.getCurrentUser()).getRole().equals(UserEnums.STORE)) {
        } else {
            throw new ServiceException(ResultCode.STORE_NOT_LOGIN_ERROR);
        }
    }
}
