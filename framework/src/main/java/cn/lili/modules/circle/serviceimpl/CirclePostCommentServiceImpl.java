package cn.lili.modules.circle.serviceimpl;

import cn.lili.common.enums.ResultCode;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.security.AuthUser;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.security.enums.UserEnums;
import cn.lili.modules.circle.entity.dos.CirclePost;
import cn.lili.modules.circle.entity.dos.CirclePostComment;
import cn.lili.modules.circle.entity.dto.CirclePostCommentOperationDTO;
import cn.lili.modules.circle.entity.dto.CirclePostCommentSearchParams;
import cn.lili.modules.circle.entity.vos.CirclePostCommentVO;
import cn.lili.modules.circle.mapper.CirclePostCommentMapper;
import cn.lili.modules.circle.mapper.CirclePostMapper;
import cn.lili.modules.circle.service.CirclePostCommentService;
import cn.lili.modules.system.aspect.annotation.SystemLogPoint;
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
        circlePostComment.setUserId(Long.valueOf(tokenUser.getId()));
        circlePostComment.setUserType(UserEnums.MEMBER.name());

        //添加评论
        boolean result = this.save(circlePostComment);
        if(result){
            circlePost.setCommentCount(circlePost.getCommentCount()+1);
            circlePostMapper.updateById(circlePost);
        }
    }
}
