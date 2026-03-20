package cn.lili.modules.circle.entity.dos;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.http.HtmlUtil;
import cn.lili.common.enums.ResultCode;
import cn.lili.common.exception.ServiceException;
import cn.lili.modules.circle.entity.dto.CirclePostOperationDTO;
import cn.lili.mybatis.BaseEntity;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

/**
 * 圈子帖子
 *
 * @author lensing
 * @since 2026-03-09 15:18:56
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("li_circle_post")
@Schema(description = "圈子帖子")
public class CirclePost extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "帖子标题（可选，如果不填则取内容前 20 字）")
    @Length(max = 100, message = "帖子标题太长，不能超过100个字符")
    private String title;

    @Schema(description = "帖子内容", required = true)
    @NotEmpty(message = "帖子内容不能为空")
    @Length(max = 2000, message = "帖子内容太长，不能超过2000个字符")
    private String content;

    @Schema(description = "图片列表 JSON 字符串")
    private String images;

    @Schema(description = "关联商品 ID 列表 JSON 字符串")
    private String goodsIds;

    @Schema(description = "点赞数量")
    private Integer likeCount = 0;

    @Schema(description = "评论数量")
    private Integer commentCount = 0;

    @Schema(description = "浏览数量")
    private Integer viewCount = 0;

    @Schema(description = "店铺ID")
//    @NotNull(message = "店铺ID不能为空")
    private String storeId;

    @Schema(description = "发帖人ID")
//    @NotNull(message = "发帖人不能为空")
    private String userId;

    @Schema(description = "发帖人类型")
    private String userType;

    @Schema(description = "状态 1=正常 0=删除/隐藏")
    private Integer status;

    @Schema(description = "是否在首页显示 1=是 0=否")
    private Integer isHomeShow;

    /**
     * 无参构造
     */
    public CirclePost() {
    }

    /**
     * 从 DTO 构造帖子实体
     *
     * @param dto 帖子操作 DTO
     */
    public CirclePost(CirclePostOperationDTO dto) {
        this.content = dto.getContent();
//        this.userId = dto.getUserId();
//        this.userType = dto.getUserType();

        // 图片处理
        if (CollectionUtil.isNotEmpty(dto.getImages())) {
            this.images = JSON.toJSONString(dto.getImages());
        }

        // 关联商品处理
        if (CollectionUtil.isNotEmpty(dto.getGoodsIds())) {
            this.goodsIds = JSON.toJSONString(dto.getGoodsIds());
        }

        // 标题：如果没传标题，取内容前20字
//        if (CharSequenceUtil.isBlank(dto.getTitle())) {
//            this.title = CharSequenceUtil.sub(dto.getContent(), 0, 20);
//        } else {
//            this.title = dto.getTitle();
//        }

        // 默认值
        this.likeCount = 0;
        this.commentCount = 0;
        this.viewCount = 0;
        this.status = 1;

        // 校验必填项
        if (CharSequenceUtil.isBlank(this.content)) {
            throw new ServiceException(ResultCode.CIRCLE_POST_CONTENT_REQUIRED);
        }

        // 校验图片和商品（可选，根据业务）
        if (CollectionUtil.isEmpty(dto.getImages()) && CollectionUtil.isEmpty(dto.getGoodsIds())) {
            // 可以根据需求决定是否允许纯文字发帖
            // throw new ServiceException(ResultCode.CIRCLE_POST_NEED_IMAGES_OR_GOODS);
        }
    }

//    public void setImages(List<String> images) {
//        this.images = JSON.toJSONString(images);
//    }

    /**
     * 获取转义后的内容（防止 XSS）
     */
    public String getContent() {
        if (CharSequenceUtil.isNotEmpty(content)) {
            return HtmlUtil.unescape(content);
        }
        return content;
    }

    /**
     * 获取转义后的标题
     */
    public String getTitle() {
        if (CharSequenceUtil.isNotEmpty(title)) {
            return HtmlUtil.unescape(title);
        }
        return title;
    }
}