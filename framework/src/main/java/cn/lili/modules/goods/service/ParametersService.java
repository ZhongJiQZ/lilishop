package cn.lili.modules.goods.service;

import cn.lili.modules.goods.entity.dos.Parameters;
import cn.lili.modules.goods.entity.dto.GoodsParamsDTO;
import cn.lili.modules.goods.entity.dto.GoodsParamsSearchDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 商品参数业务层
 *
 * @author pikachu
 * @since 2020-03-02 16:18:56
 */
public interface ParametersService extends IService<Parameters> {

    /**
     * 商品参数分页
     *
     * @param pageVo 分页参数
     * @return 商品参数分页
     */
    IPage<Parameters> parametersPage(GoodsParamsSearchDTO pageVo);

    /**
     * 添加参数信息
     *
     * @param parameters 参数组信息
     * @return 是否添加成功
     */
    boolean addParameter(GoodsParamsDTO parameters);

    /**
     * 更新参数信息
     *
     * @param parameters 参数组信息
     * @return 是否更新成功
     */
    boolean updateParameter(GoodsParamsDTO parameters);

    /**
     * 获取参数信息
     *
     * @param id 参数组id
     * @return 参数组信息
     */
    GoodsParamsDTO getGoodsParamsDTO(String id);

    /**
     * 根据分类ID获取参数信息
     *
     * @param categoryId 分类id
     * @return 参数组信息
     */
    List<Parameters> getParametersByCategoryId(String categoryId);

}