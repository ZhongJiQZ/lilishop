package cn.lili.modules.member.entity.dos;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.lili.modules.member.entity.dto.ClerkAddDTO;
import cn.lili.modules.store.entity.dos.Store;
import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 店员Model
 *
 * @author wget
 * @title: Clerk
 * @projectName lilishop
 * @date 2021/12/28 7:39 下午
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("li_clerk")
@Schema(description = "店员")
@NoArgsConstructor
@AllArgsConstructor
public class Clerk extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "店员名称")
    private String clerkName;

    @Schema(description = "会员ID")
    private String memberId;

    @Schema(description = "店铺ID")
    private String storeId;

    @Schema(description = "所属部门id")
    private String departmentId;

    @Schema(description = "角色id集合")
    private String roleIds;

    @Schema(description = "是否是店主", hidden = true)
    private Boolean shopkeeper = false;

    @Schema(description = "是否是超级管理员 超级管理员/普通管理员")
    private Boolean isSuper = false;

    @Schema(description = "状态 默认true正常 false禁用")
    private Boolean status = true;


    /**
     * 构建店员
     *
     * @param clerkAddDTO
     */
    public Clerk(ClerkAddDTO clerkAddDTO) {
        if (CollUtil.isNotEmpty(clerkAddDTO.getRoles()) && !clerkAddDTO.getRoles().isEmpty()) {
            this.roleIds = CharSequenceUtil.join(",", clerkAddDTO.getRoles());
        }
        this.memberId = clerkAddDTO.getMemberId();
        this.departmentId = clerkAddDTO.getDepartmentId();
        this.storeId = clerkAddDTO.getStoreId();
        this.clerkName = clerkAddDTO.getUsername();

    }


    public Clerk(Store store) {
        this.memberId = store.getMemberId();
        this.storeId = store.getId();
        this.clerkName = store.getMemberName();
        this.setShopkeeper(true);
        this.setIsSuper(true);
        this.setStatus(true);
    }
}
