package cn.lili.modules.circle.mapper;

import cn.lili.modules.circle.entity.dos.CirclePostComment;
import cn.lili.modules.circle.entity.vos.CirclePostCommentVO;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

    /**
     * 根据圈子帖子id查评论列表
     *
     * @param circlePostId 圈子帖子id
     * @return 圈子帖子绑定的评论列表
     */
    @Select("SELECT * FROM li_circle_post_comment where post_id = #{circlePostId} and delete_flag = 0")
    List<CirclePostComment> getCommentCirclePostList(String circlePostId);

    /**
     * 统计指定评论ID列表中，有多少条评论所属的帖子不属于指定店铺
     *
     * @param commentIds 评论ID集合
     * @param storeId    当前店铺ID
     * @return 不属于该店铺的评论数量（>0 表示有越权）
     */
    @Select("""
        SELECT COUNT(1)
        FROM li_circle_post_comment c
        INNER JOIN li_circle_post cp ON cp.id = c.post_id
        WHERE c.id IN (#{commentIds})
          AND cp.store_id != #{storeId}
    """)
    long checkStorePermission(List<String> commentIds, String storeId);

    /**
     * 分页查询店铺端评论列表（自动过滤只属于当前店铺的帖子评论）
     *
     * @param page       分页参数
     * @param queryWrapper MyBatis-Plus 的查询条件（动态 WHERE 部分）
     * @return 分页结果
     */
    @Select("""
        <script>
            SELECT c.*
            FROM li_circle_post_comment c
            INNER JOIN li_circle_post cp ON cp.id = c.post_id
            ${ew != null ? ew.customSqlSegment : ''}
        </script>
    """)
    IPage<CirclePostComment> queryStoreCommentPage(
            Page<CirclePostComment> page,
            @Param("ew") QueryWrapper<CirclePostComment> queryWrapper
    );
}