package cn.lili.modules.circle.service;

import cn.lili.modules.circle.entity.dos.CirclePost;
import cn.lili.modules.circle.entity.dto.CirclePostOperationDTO;
import cn.lili.modules.circle.entity.dto.CirclePostPageDTO;
import cn.lili.modules.circle.entity.dto.CirclePostSearchParams;
import cn.lili.modules.circle.entity.vos.CirclePostVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.validation.Valid;

import java.util.List;

/**
 * 圈子帖子业务层
 *
 * @author lensing
 * @since 2026-03-09 15:18:56
 */
public interface CirclePostService extends IService<CirclePost> {
    /**
     * 圈子帖子查询
     *
     * @param circlePostSearchParams 查询参数
     * @return 圈子帖子分页
     */
    IPage<CirclePostVO> queryByParams(CirclePostSearchParams circlePostSearchParams);

    /**
     * 发布圈子帖子
     *
     * @param circlePostOperationDTO 圈子帖子查询条件
     */
    void addCirclePost(@Valid CirclePostOperationDTO circlePostOperationDTO);

    IPage<CirclePost> queryCirclePostByParams(CirclePostPageDTO page);

    void deleteCirclePosts(List<String> ids);
}
