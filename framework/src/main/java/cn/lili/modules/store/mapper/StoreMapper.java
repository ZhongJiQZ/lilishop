package cn.lili.modules.store.mapper;

import cn.lili.modules.store.entity.dos.Store;
import cn.lili.modules.store.entity.vos.StoreTradeRankingVO;
import cn.lili.modules.store.entity.vos.StoreVO;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 店铺数据处理层
 *
 * @author pikachu
 * @since2020-03-07 09:18:56
 */
public interface StoreMapper extends BaseMapper<Store> {

    /**
     * 获取店铺详细
     *
     * @param id 店铺ID
     * @return 店铺VO
     */
    @Select("select s.*,d.* from li_store s inner join li_store_detail d on s.id=d.store_id where s.id=#{id} ")
    StoreVO getStoreDetail(String id);

    /**
     * 获取店铺分页列表
     *
     * @param page         分页
     * @param queryWrapper 查询条件
     * @return 店铺VO分页列表
     */
    @Select("select s.*,mobile,legal_name from li_store as s left join li_member m on m.id=s.member_id left join li_store_detail d on d.store_id=s.id ${ew.customSqlSegment}")
    IPage<StoreVO> getStoreList(IPage<StoreVO> page, @Param(Constants.WRAPPER) Wrapper<StoreVO> queryWrapper);


    /**
     * 修改店铺收藏数据
     *
     * @param storeId 店铺id
     * @param num     收藏数量
     */
    @Update("update li_store set collection_num = collection_num + #{num} where id = #{storeId}")
    void updateCollection(String storeId, Integer num);

    /**
     * 店铺成交排行榜
     *
     * @param page         分页
     * @param queryWrapper 查询条件
     * @return 店铺VO分页列表
     */
    @Select("SELECT s.id, s.store_name, s.store_logo, s.store_desc, s.height, s.weight, s.occupation, (COUNT(o.id) + ifnull(s.virtual_sales_num,0)) AS order_count " +
            "FROM li_store s " +
            "LEFT JOIN li_order o ON s.id = o.store_id " +
            "AND o.order_status = 'COMPLETE' " + // 只统计已完成订单
            "AND o.pay_status = 'PAID' " +
            "AND o.deliver_status = 'SIGN' " +
            "${ew.customSqlSegment} " +
            "GROUP BY s.id " +
            "ORDER BY order_count DESC, s.id ASC")
    IPage<StoreTradeRankingVO> getStoreTradeRankingList(Page<Object> page, @Param(Constants.WRAPPER) QueryWrapper<Store> queryWrapper);
}