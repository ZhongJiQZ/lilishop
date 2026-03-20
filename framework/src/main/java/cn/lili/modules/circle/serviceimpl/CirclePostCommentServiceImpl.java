package cn.lili.modules.circle.serviceimpl;

import cn.hutool.core.text.CharSequenceUtil;
import cn.lili.common.enums.ResultCode;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.security.AuthUser;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.security.enums.UserEnums;
import cn.lili.common.sensitive.SensitiveWordsFilter;
import cn.lili.modules.circle.entity.dos.CirclePost;
import cn.lili.modules.circle.entity.dos.CirclePostComment;
import cn.lili.modules.circle.entity.dto.CirclePostCommentOperationDTO;
import cn.lili.modules.circle.entity.dto.CirclePostCommentSearchParams;
import cn.lili.modules.circle.entity.vos.CirclePostCommentVO;
import cn.lili.modules.circle.mapper.CirclePostCommentMapper;
import cn.lili.modules.circle.mapper.CirclePostMapper;
import cn.lili.modules.circle.service.CirclePostCommentService;
import cn.lili.modules.system.aspect.annotation.SystemLogPoint;
import cn.lili.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
public class CirclePostCommentServiceImpl extends ServiceImpl<CirclePostCommentMapper, CirclePostComment> implements CirclePostCommentService {

    /**
     * 圈子帖子
     */
    @Autowired
    private CirclePostMapper circlePostMapper;

    @Override
    public List<CirclePostCommentVO> getCirclePostCommentByList(CirclePostCommentSearchParams searchParams) {
        return this.baseMapper.getCirclePostCommentByList(searchParams.queryWrapper());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @SystemLogPoint(description = "添加评论", customerLog = "'新增评论:['+#commentOperationDTO.content+']'")
    public void addCirclePostComment(CirclePostCommentOperationDTO commentOperationDTO) {
        AuthUser tokenUser = UserContext.getCurrentUser();
        if (tokenUser == null) {
            throw new ServiceException(ResultCode.USER_NOT_LOGIN);
        }
        CirclePost circlePost = circlePostMapper.selectById(commentOperationDTO.getPostId());
        if(circlePost == null){
            throw new ServiceException(ResultCode.CIRCLE_POST_NOT_EXIST);
        }
        CirclePostComment circlePostComment = new CirclePostComment(commentOperationDTO);
//        CirclePostComment circlePostComment = CirclePostComment.fromDTO(commentOperationDTO);
        circlePostComment.setUserId(tokenUser.getId());
        circlePostComment.setUserType(UserEnums.MEMBER.name());

        //添加评论
        boolean result = this.save(circlePostComment);
        if(result){
            circlePost.setCommentCount(circlePost.getCommentCount()+1);
            circlePostMapper.updateById(circlePost);
        }
    }

    @Override
    public List<CirclePostComment> getCommentCirclePostList(String circlePostId) {
        AuthUser currentUser = UserContext.getCurrentUser();

        // 校验该帖子是否属于当前店铺
        if (UserEnums.STORE.equals(currentUser.getRole())) {
            String storeId = currentUser.getStoreId();
            if (storeId != null) {
                CirclePost post = circlePostMapper.selectById(circlePostId);
                if (post == null || !storeId.equals(post.getStoreId())) {
                    throw new ServiceException(ResultCode.CIRCLE_COMMENT_PERMISSION_DENIED);
                }
            }
        }
        List<CirclePostComment> commentCirclePostList = this.baseMapper.getCommentCirclePostList(circlePostId);
        commentCirclePostList.forEach(circlePostComment -> {
            circlePostComment.setContent(SensitiveWordsFilter.filter(circlePostComment.getContent()));
        });
        return commentCirclePostList;
    }

    @Override
    public IPage<CirclePostComment> queryByParams(CirclePostCommentSearchParams commentSearchParams) {
//        return this.page(PageUtil.initPage(commentSearchParams), commentSearchParams.queryWrapper());
        AuthUser tokenUser = UserContext.getCurrentUser();
        if (tokenUser == null) {
            throw new ServiceException(ResultCode.USER_NOT_LOGIN);
        }

        QueryWrapper<CirclePostComment> queryWrapper = commentSearchParams.queryWrapper();

        String storeId = null;
        if (UserEnums.STORE.equals(tokenUser.getRole())) {
            storeId = tokenUser.getStoreId();
            if (CharSequenceUtil.isBlank(storeId)) {
                throw new ServiceException("当前商家信息异常，无法查询评论");
            }
            queryWrapper.eq("store_id", storeId);
        }

        IPage<CirclePostComment> page = this.baseMapper.queryStoreCommentPage(
                PageUtil.initPage(commentSearchParams),
                queryWrapper
        );
        page.getRecords().forEach(circlePostComment -> {
            circlePostComment.setContent(SensitiveWordsFilter.filter(circlePostComment.getContent()));
        });

        // 使用自定义 Mapper 方法（如果 storeId 为 null，则不加限制）
        return page;
    }

    @Override
    public long checkStorePermission(List<String> commentIds) {
        AuthUser tokenUser = UserContext.getCurrentUser();
        if (tokenUser == null) {
            throw new ServiceException(ResultCode.USER_NOT_LOGIN);
        }
        long invalidCount = this.baseMapper.checkStorePermission(commentIds, tokenUser.getStoreId());
        if (invalidCount > 0) {
            throw new ServiceException(ResultCode.CIRCLE_COMMENT_PERMISSION_DENIED);
        }
        List<CirclePostComment> circlePostComments = this.baseMapper.selectByIds(commentIds);
        if(circlePostComments!=null && circlePostComments.size()==0){
            throw new ServiceException(ResultCode.CIRCLE_POST_COMMENT_NOT_EXIST);
        }
        return invalidCount;
    }
}
