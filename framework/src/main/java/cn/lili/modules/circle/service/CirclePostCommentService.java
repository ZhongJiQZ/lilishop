package cn.lili.modules.circle.service;

import cn.lili.modules.circle.entity.dos.CirclePostComment;
import cn.lili.modules.circle.entity.dto.CirclePostCommentOperationDTO;
import cn.lili.modules.circle.entity.dto.CirclePostCommentSearchParams;
import cn.lili.modules.circle.entity.vos.CirclePostCommentVO;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.validation.Valid;

import java.util.List;

/**
 * 圈子帖子评论业务层
 *
 * @author lensing
 * @since 2026-03-09 15:18:56
 */
public interface CirclePostCommentService extends IService<CirclePostComment> {
//    /**
//     * 圈子帖子评论查询
//     *
//     * @param circlePostCommentSearchParams 查询参数
//     * @return 圈子帖子评论分页
//     */
//    IPage<CirclePostCommentVO> queryByParams(CirclePostCommentSearchParams circlePostCommentSearchParams);

    /**
     * 列表查询圈子帖子评论信息
     *
     * @param searchParams 查询参数
     * @return 圈子帖子评论信息
     */
    List<CirclePostCommentVO> getCirclePostCommentByList(CirclePostCommentSearchParams searchParams);

    /**
     * 添加评论
     *
     * @param commentOperationDTO 评论查询条件
     */
    void addCirclePostComment(@Valid CirclePostCommentOperationDTO commentOperationDTO);
}
