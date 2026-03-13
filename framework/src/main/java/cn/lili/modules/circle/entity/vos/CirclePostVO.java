package cn.lili.modules.circle.entity.vos;

import com.alibaba.fastjson2.JSON;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 商品VO
 *
 * @author lensing
 * @since 2026-03-09 15:18:56
 */
@Data
public class CirclePostVO {

    private static final long serialVersionUID = 6377623919990713567L;

    @Schema(description = "店铺logo")
    private String storeLogo;

    @Schema(description = "店铺名称")
    private String storeName;

    @Schema(description = "帖子ID")
    private String contentId;

    @Schema(description = "帖子内容")
    private String content;

    @Schema(description = "商品图片")
    private String images;
//    private List<String> goodsGalleryList;

    @Schema(description = "评论数量")
    private Integer commentCount = 0;

    @Schema(description = "评论列表")
    private List<CirclePostCommentVO> commentList;

    public List<String> getImages() {
        return JSON.parseArray(this.images, String.class); // Fastjson
        // 或 ObjectMapper.readValue(product.getImages(), new TypeReference<List<String>>(){});
    }
}
