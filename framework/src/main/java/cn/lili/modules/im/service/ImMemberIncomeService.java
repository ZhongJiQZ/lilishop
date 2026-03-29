package cn.lili.modules.im.service;

import cn.lili.modules.im.entity.dos.ImMemberIncome;
import cn.lili.modules.member.entity.dto.ImMemberIncomePageDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 聊天打赏 业务层
 *
 * @author lensing
 */
public interface ImMemberIncomeService extends IService<ImMemberIncome> {

    IPage<ImMemberIncome> queryImMemberIncomeByParams(ImMemberIncomePageDTO page);
}