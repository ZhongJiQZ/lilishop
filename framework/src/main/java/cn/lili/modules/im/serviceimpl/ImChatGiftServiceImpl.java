package cn.lili.modules.im.serviceimpl;

import cn.lili.common.utils.StringUtils;
import cn.lili.modules.im.entity.dos.ImChatGift;
import cn.lili.modules.im.entity.dto.ImChatGiftPageDTO;
import cn.lili.modules.im.entity.dto.ImChatGiftQueryParams;
import cn.lili.modules.im.entity.vo.ImChatGiftVO;
import cn.lili.modules.im.mapper.ImChatGiftMapper;
import cn.lili.modules.im.service.ImChatGiftService;
import cn.lili.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 聊天礼物 业务实现
 *
 * @author lensing
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ImChatGiftServiceImpl extends ServiceImpl<ImChatGiftMapper, ImChatGift> implements ImChatGiftService {


    @Override
    public List<ImChatGiftVO> getGiftList(ImChatGiftQueryParams chatGiftQueryParams) {
        List<ImChatGift> chatGiftList = this.page(PageUtil.initPage(chatGiftQueryParams), chatGiftQueryParams.initQueryWrapper()).getRecords();
        return chatGiftList.stream().map(imChatGift -> {
            ImChatGiftVO chatGiftVO = new ImChatGiftVO();
            BeanUtils.copyProperties(imChatGift, chatGiftVO);
            return chatGiftVO;
        }).collect(Collectors.toList());
    }

    @Override
    public IPage<ImChatGift> queryImChatGiftByParams(ImChatGiftPageDTO page) {
        LambdaQueryWrapper<ImChatGift> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(page.getGiftName())) {
            queryWrapper.like(ImChatGift::getGiftName, page.getGiftName());
        }
        queryWrapper.orderByDesc(ImChatGift::getSort);
        return this.page(PageUtil.initPage(page), queryWrapper);
    }
}