package cn.lili.modules.member.entity.vo;

import cn.lili.modules.member.entity.dos.StoreMenu;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * RoleMenuVO
 *
 * @author Chopper
 * @since 2020-11-24 11:45
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class StoreUserMenuVO extends StoreMenu {

    private static final long serialVersionUID = -7478870595109016162L;

    /**
     * 是否是超级管理员
     */
    private Boolean isSuper;

    public Boolean getSuper() {
        if (this.isSuper == null) {
            return false;
        }
        return isSuper;
    }
}
