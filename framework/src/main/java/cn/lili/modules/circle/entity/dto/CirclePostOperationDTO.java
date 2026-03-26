package cn.lili.modules.circle.entity.dto;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.http.HtmlUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 圈子帖子操作 DTO
 *
 * @author lensing
 * @since 2026-03-09 15:18:56
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CirclePostOperationDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "帖子ID（编辑时传，新增时不传）", hidden = true)
    private Long postId;

//    @Schema(description = "帖子标题（可选，不填自动取内容前20字）")
//    @Length(max = 100, message = "帖子标题不能超过100个字符")
//    private String title;

    @Schema(description = "店铺ID")
    private String storeId;

    @Schema(description = "帖子内容", required = true)
    @NotEmpty(message = "帖子内容不能为空")
    @Length(max = 2000, message = "帖子内容不能超过2000个字符")
    private String content;

    @Schema(description = "图片列表（支持多张）")
    private List<String> images = new ArrayList<>();

    @Schema(description = "关联商品ID列表（可选，带货帖子使用）")
    private List<Long> goodsIds = new ArrayList<>();

    @Schema(description = "是否在首页显示 1=是 0=否")
    private Integer isHomeShow;

//    @Schema(description = "是否立即发布（默认立即发布）")
//    @Builder.Default
//    private Boolean release = true;
//
//    @Schema(description = "是否置顶（商家专属，普通用户忽略）")
//    @Builder.Default
//    private Boolean top = false;
//
//    @Schema(description = "是否推荐到首页（管理员或商家专属）")
//    @Builder.Default
//    private Boolean recommend = false;
//
//    @Schema(description = "发帖人ID（系统自动填充）", hidden = true)
//    private Long userId;
//
//    @Schema(description = "发帖人类型 MERCHANT=商家 ORDINARY_USER=普通用户（系统自动填充）", hidden = true)
//    @EnumValue(strValues = {"MERCHANT", "ORDINARY_USER"}, message = "发帖人类型参数值错误")
//    private String userType;

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
//    public String getTitle() {
//        if (CharSequenceUtil.isNotEmpty(title)) {
//            return HtmlUtil.unescape(title);
//        }
//        return title;
//    }
}