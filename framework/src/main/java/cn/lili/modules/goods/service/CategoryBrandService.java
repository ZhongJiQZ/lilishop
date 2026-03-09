package cn.lili.modules.goods.service;

import cn.lili.modules.goods.entity.dos.CategoryBrand;
import cn.lili.modules.goods.entity.dos.Category;
import cn.lili.modules.goods.entity.vos.CategoryBrandVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 商品分类品牌业务层
 *
 * @author pikachu
 * @since 2020-02-26 16:18:56
 */
public interface CategoryBrandService extends IService<CategoryBrand> {
    /**
     * 根据分类id查询品牌信息
     *
     * @param categoryId 分类id
     * @return 分类品牌关联信息列表
     */
    List<CategoryBrandVO> getCategoryBrandList(String categoryId);

    /**
     * 通过分类ID删除关联品牌
     *
     * @param categoryId 品牌ID
     */
    void deleteByCategoryId(String categoryId);

    /**
     * 根据品牌id查询分类信息
     *
     * @param brandId 品牌id
     * @return 品牌分类关联信息列表
     */
    List<Category> getBrandCategoryList(String brandId);

    /**
     * 通过品牌ID删除关联分类
     *
     * @param brandId 品牌ID
     */
    void deleteByBrandId(String brandId);


    /**
     * 根据品牌ID获取分类品牌关联信息
     *
     * @param brandId 品牌ID
     * @return 分类品牌关联信息
     */
    List<CategoryBrand> getCategoryBrandListByBrandId(List<String> brandId);

    /**
     * 保存分类品牌关系
     *
     * @param categoryId 分类id
     * @param brandIds   品牌ids
     */
    void saveCategoryBrandList(String categoryId, List<String> brandIds);

    /**
     * 保存品牌分类关系
     *
     * @param brandId 品牌id
     * @param categoryIds 分类ids
     */
    void saveBrandCategoryList(String brandId, List<String> categoryIds);

}
