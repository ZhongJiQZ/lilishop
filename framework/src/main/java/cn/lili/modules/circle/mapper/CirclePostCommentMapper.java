package cn.lili.modules.circle.mapper;

import cn.lili.modules.circle.entity.dos.CirclePostComment;
import cn.lili.modules.circle.entity.vos.CirclePostCommentVO;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 圈子帖子评论数据处理层
 *
 * @author lensing
 * @since 2026-03-09 15:18:56
 */
public interface CirclePostCommentMapper extends BaseMapper<CirclePostComment> {
//    /**
//     * 获取圈子帖子评论VO分页
//     *
//     * @param page         分页
//     * @param queryWrapper 查询条件
//     * @return 圈子帖子VO分页
//     */
//    @Select("SELECT nick_name,content FROM li_circle_post_comment c left join li_member m on m.id=c.user_id ${ew.customSqlSegment}")
//    IPage<CirclePostCommentVO> getCirclePostCommentList(IPage<CirclePostCommentVO> page, @Param(Constants.WRAPPER) Wrapper<CirclePostCommentVO> queryWrapper);

    /**
     * 获取圈子帖子评论VO列表
     *
     * @param queryWrapper 查询条件
     * @return 圈子帖子VO分页
     */
    @Select("SELECT nick_name,content FROM li_circle_post_comment c left join li_member m on m.id=c.user_id ${ew.customSqlSegment}")
    List<CirclePostCommentVO> getCirclePostCommentByList(@Param(Constants.WRAPPER) Wrapper<CirclePostCommentVO> queryWrapper);
}