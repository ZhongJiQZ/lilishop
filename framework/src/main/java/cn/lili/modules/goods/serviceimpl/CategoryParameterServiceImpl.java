package cn.lili.modules.goods.serviceimpl;

import cn.lili.modules.goods.entity.dos.CategoryParameter;
import cn.lili.modules.goods.mapper.CategoryParameterMapper;
import cn.lili.modules.goods.service.CategoryParameterService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 分类参数业务层实现
 *
 * @author Bulbasaur
 * @since 2020/12/10 10:05 上午
 */
@Service
public class CategoryParameterServiceImpl extends ServiceImpl<CategoryParameterMapper, CategoryParameter> implements CategoryParameterService {

    @Override
    public List<CategoryParameter> getCategoryParamList(String categoryId) {
        return this.list(new LambdaQueryWrapper<CategoryParameter>().eq(CategoryParameter::getCategoryId, categoryId));
    }

    @Override
    public List<CategoryParameter> getCategoryParamListByParameterId(String parameterId) {
        return this.list(new LambdaQueryWrapper<CategoryParameter>().eq(CategoryParameter::getParameterId, parameterId));
    }

    @Override
    public void deleteByCategoryId(String categoryId) {
        this.baseMapper.delete(new LambdaUpdateWrapper<CategoryParameter>().eq(CategoryParameter::getCategoryId, categoryId));
    }

    @Override
    public void deleteByParameterId(String parameterId) {
        this.baseMapper.delete(new LambdaUpdateWrapper<CategoryParameter>().eq(CategoryParameter::getParameterId, parameterId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveCategoryParameterList(String parameterId, List<String> categoryIds) {
        this.deleteByParameterId(parameterId);
        if (categoryIds != null && !categoryIds.isEmpty()) {
            List<CategoryParameter> relations = new ArrayList<>();
            for (String categoryId : categoryIds) {
                relations.add(new CategoryParameter(categoryId, parameterId));
            }
            this.saveBatch(relations);
        }
    }
}

