package cn.lili.modules.circle.mapper;

import cn.lili.modules.circle.entity.dos.CirclePost;
import cn.lili.modules.circle.entity.vos.CirclePostVO;
import cn.lili.modules.page.entity.vos.ArticleVO;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 圈子帖子数据处理层
 *
 * @author lensing
 * @since 2026-03-09 15:18:56
 */
public interface CirclePostMapper extends BaseMapper<CirclePost> {
    /**
     * 获取圈子帖子VO分页
     *
     * @param page         分页
     * @param queryWrapper 查询条件
     * @return 圈子帖子VO分页
     */
    @Select("select c.id as contentId,content,images,comment_count,IFNULL(store_logo, u.avatar) store_logo,IFNULL(store_name, u.nick_name) store_name,user_type from li_circle_post c left join li_member m on m.id=c.user_id left join li_store s on s.member_id=m.id LEFT JOIN li_admin_user u ON u.id = c.user_id ${ew.customSqlSegment}")
    IPage<CirclePostVO> getCirclePostList(IPage<ArticleVO> page, @Param(Constants.WRAPPER) Wrapper<ArticleVO> queryWrapper);

//    /**
//     * 根据店铺ID获取商品ID列表
//     *
//     * @param storeId 店铺ID
//     * @return 商品ID列表
//     */
//    @Select("SELECT id FROM li_goods WHERE store_id = #{storeId}")
//    List<String> getGoodsIdByStoreId(String storeId);
//
//    /**
//     * 添加商品评价数量
//     *
//     * @param commentNum 评价数量
//     * @param goodsId    商品ID
//     */
//    @Update("UPDATE li_goods SET comment_num = comment_num + #{commentNum} WHERE id = #{goodsId}")
//    void addGoodsCommentNum(Integer commentNum, String goodsId);
//
//    /**
//     * 查询商品VO分页
//     *
//     * @param page         分页
//     * @param queryWrapper 查询条件
//     * @return 商品VO分页
//     */
//    @Select("select g.* from li_goods as g ")
//    IPage<GoodsVO> queryByParams(IPage<GoodsVO> page, @Param(Constants.WRAPPER) Wrapper<GoodsVO> queryWrapper);
}