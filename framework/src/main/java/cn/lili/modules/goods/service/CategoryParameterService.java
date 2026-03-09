package cn.lili.modules.goods.service;

import cn.lili.modules.goods.entity.dos.CategoryParameter;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 参数关联分类 业务层
 *
 * @author Bulbasaur
 * @since 2025-12-21
 */
public interface CategoryParameterService extends IService<CategoryParameter> {

    /**
     * 根据分类id查询参数信息
     *
     * @param categoryId 分类id
     * @return 分类参数关联信息列表
     */
    List<CategoryParameter> getCategoryParamList(String categoryId);

    /**
     * 根据参数ID查询参数信息
     *
     * @param parameterId 参数ID
     * @return 分类参数关联信息列表
     */
    List<CategoryParameter> getCategoryParamListByParameterId(String parameterId);

    /**
     * 通过分类ID删除关联参数
     *
     * @param categoryId 分类ID
     */
    void deleteByCategoryId(String categoryId);

    /**
     * 通过参数ID删除关联参数
     *
     * @param parameterId 参数ID
     */
    void deleteByParameterId(String parameterId);

    /**
     * 保存参数分类关系
     *
     * @param categoryIds 分类ids
     * @param parameterId 参数id
     */
    void saveCategoryParameterList(String parameterId, List<String> categoryIds);
}

