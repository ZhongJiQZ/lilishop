package cn.lili.modules.store.serviceimpl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.text.CharSequenceUtil;
import cn.lili.cache.Cache;
import cn.lili.cache.CachePrefix;
import cn.lili.common.enums.ResultCode;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.properties.RocketmqCustomProperties;
import cn.lili.common.security.AuthUser;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.security.enums.UserEnums;
import cn.lili.common.utils.BeanUtil;
import cn.lili.common.vo.PageVO;
import cn.lili.modules.eid.entity.dos.MemberEidRecord;
import cn.lili.modules.eid.service.MemberEidRecordService;
import cn.lili.modules.goods.entity.dos.Goods;
import cn.lili.modules.goods.entity.dos.GoodsSku;
import cn.lili.modules.goods.entity.enums.GoodsStatusEnum;
import cn.lili.modules.goods.entity.vos.GoodsTradeRankingVO;
import cn.lili.modules.goods.service.GoodsService;
import cn.lili.modules.goods.service.GoodsSkuService;
import cn.lili.modules.member.entity.dos.Clerk;
import cn.lili.modules.member.entity.dos.FootPrint;
import cn.lili.modules.member.entity.dos.Member;
import cn.lili.modules.member.entity.dto.ClerkAddDTO;
import cn.lili.modules.member.entity.dto.CollectionDTO;
import cn.lili.modules.member.service.ClerkService;
import cn.lili.modules.member.service.FootprintService;
import cn.lili.modules.member.service.MemberService;
import cn.lili.modules.store.entity.dos.Store;
import cn.lili.modules.store.entity.dos.StoreDetail;
import cn.lili.modules.store.entity.dto.*;
import cn.lili.modules.store.entity.enums.StoreStatusEnum;
import cn.lili.modules.store.entity.vos.StoreSearchParams;
import cn.lili.modules.store.entity.vos.StoreTradeRankingVO;
import cn.lili.modules.store.entity.vos.StoreVO;
import cn.lili.modules.store.mapper.StoreMapper;
import cn.lili.modules.store.service.StoreDetailService;
import cn.lili.modules.store.service.StoreService;
import cn.lili.modules.system.entity.dos.Setting;
import cn.lili.modules.system.entity.dto.EidSetting;
import cn.lili.modules.system.entity.enums.SettingEnum;
import cn.lili.modules.system.service.SettingService;
import cn.lili.mybatis.util.PageUtil;
import cn.lili.rocketmq.RocketmqSendCallbackBuilder;
import cn.lili.rocketmq.tags.StoreTagsEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 店铺业务层实现
 *
 * @author pikachu
 * @since 2020-03-07 16:18:56
 */
@Slf4j
@Service
public class StoreServiceImpl extends ServiceImpl<StoreMapper, Store> implements StoreService {

    /**
     * 会员
     */
    @Autowired
    @Lazy
    private MemberService memberService;

    /**
     * 店员
     */
    @Autowired
    private ClerkService clerkService;
    /**
     * 商品
     */
    @Autowired
    @Lazy
    private GoodsService goodsService;

    @Autowired
    @Lazy
    private GoodsSkuService goodsSkuService;
    /**
     * 店铺详情
     */
    @Autowired
    @Lazy
    private StoreDetailService storeDetailService;

    @Autowired
    private RocketmqCustomProperties rocketmqCustomProperties;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Autowired
    @Lazy
    private FootprintService footprintService;

    @Autowired
    private MemberEidRecordService memberEidRecordService;

    @Autowired
    private SettingService settingService;

    @Autowired
    private Cache cache;

    @Override
    public IPage<StoreVO> findByConditionPage(StoreSearchParams storeSearchParams, PageVO page) {
        return this.baseMapper.getStoreList(PageUtil.initPage(page), storeSearchParams.queryWrapper());
    }

    @Override
    public StoreVO getStoreDetail() {
        AuthUser currentUser = Objects.requireNonNull(UserContext.getCurrentUser());
        StoreVO storeVO = this.baseMapper.getStoreDetail(currentUser.getStoreId());
        storeVO.setNickName(currentUser.getNickName());
        return storeVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Store add(AdminStoreApplyDTO adminStoreApplyDTO) {

        //判断店铺名称是否存在
        QueryWrapper<Store> queryWrapper = Wrappers.query();
        queryWrapper.eq("store_name", adminStoreApplyDTO.getStoreName());
        if (this.getOne(queryWrapper) != null) {
            throw new ServiceException(ResultCode.STORE_NAME_EXIST_ERROR);
        }

        Member member = memberService.getById(adminStoreApplyDTO.getMemberId());
        //判断用户是否存在
        if (member == null) {
            throw new ServiceException(ResultCode.USER_NOT_EXIST);
        }
        //判断是否拥有店铺
        if (Boolean.TRUE.equals(member.getHaveStore())) {
            throw new ServiceException(ResultCode.STORE_APPLY_DOUBLE_ERROR);
        }

        //添加店铺
        Store store = new Store(member, adminStoreApplyDTO);
        this.save(store);

        //判断是否存在店铺详情，如果没有则进行新建，如果存在则进行修改
        StoreDetail storeDetail = new StoreDetail(store, adminStoreApplyDTO);

        storeDetailService.save(storeDetail);

        //设置会员-店铺信息
        memberService.update(new LambdaUpdateWrapper<Member>()
                .eq(Member::getId, member.getId())
                .set(Member::getHaveStore, true)
                .set(Member::getStoreId, store.getId()));
        return store;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Store edit(StoreEditDTO storeEditDTO) {
        if (storeEditDTO != null) {
            //判断店铺名是否唯一
            Store storeTmp = getOne(new QueryWrapper<Store>().eq("store_name", storeEditDTO.getStoreName()));
            if (storeTmp != null && !CharSequenceUtil.equals(storeTmp.getId(), storeEditDTO.getStoreId())) {
                throw new ServiceException(ResultCode.STORE_NAME_EXIST_ERROR);
            }
            //同步店铺信息到个人信息
            syncShopInfoToUserInfo(storeEditDTO);
            //修改店铺详细信息
            updateStoreDetail(storeEditDTO);
            //修改店铺信息
            return updateStore(storeEditDTO);
        } else {
            throw new ServiceException(ResultCode.STORE_NOT_EXIST);
        }
    }

    /**
     * 同步店铺信息到个人信息
     * @param storeEditDTO
     */
    private void syncShopInfoToUserInfo(StoreEditDTO storeEditDTO) {
        Store store = this.getById(storeEditDTO.getStoreId());
        Member member = memberService.getById(store.getMemberId());
        if(member == null) {
            throw new ServiceException(ResultCode.USER_NOT_EXIST);
        }
        member.setFullName(storeEditDTO.getLegalName());//姓名
        member.setNickName(storeEditDTO.getStoreName());//昵称
        member.setMemberDesc(storeEditDTO.getStoreDesc());//个人简介
        member.setIdCard(storeEditDTO.getLegalId());//证件号
        member.setMobile(storeEditDTO.getLinkPhone());//联系电话
        member.setFace(storeEditDTO.getStoreLogo());//照片
        member.setHeight(storeEditDTO.getHeight());//身高
        member.setWeight(storeEditDTO.getWeight());//体重
        member.setOccupation(storeEditDTO.getOccupation());//职业
        memberService.updateById(member);
    }

    /**
     * 修改店铺基本信息
     *
     * @param storeEditDTO 修改店铺信息
     */
    private Store updateStore(StoreEditDTO storeEditDTO) {
        Store store = this.getById(storeEditDTO.getStoreId());
        if (store != null) {
            BeanUtil.copyProperties(storeEditDTO, store);
            store.setId(storeEditDTO.getStoreId());
            boolean result = this.updateById(store);
            if (result) {
                storeDetailService.updateStoreGoodsInfo(store);
            }
            String destination = rocketmqCustomProperties.getStoreTopic() + ":" + StoreTagsEnum.EDIT_STORE_SETTING.name();
            //发送订单变更mq消息
            rocketMQTemplate.asyncSend(destination, store, RocketmqSendCallbackBuilder.commonCallback());
        }

        cache.remove(CachePrefix.STORE.getPrefix() + storeEditDTO.getStoreId());
        return store;
    }

    /**
     * 修改店铺详细信息
     *
     * @param storeEditDTO 修改店铺信息
     */
    private void updateStoreDetail(StoreEditDTO storeEditDTO) {
        StoreDetail storeDetail = new StoreDetail();
        BeanUtil.copyProperties(storeEditDTO, storeDetail);
        storeDetailService.update(storeDetail, new QueryWrapper<StoreDetail>().eq("store_id", storeEditDTO.getStoreId()));
    }

    /**
     * 检测会员
     *
     * @param userName    会员名称
     * @param mobilePhone 手机号
     */
    private void checkMember(String userName, String mobilePhone) {
        //判断手机号是否存在
        if (findMember(mobilePhone, userName) > 0) {
            throw new ServiceException(ResultCode.USER_EXIST);
        }
    }

    /**
     * 根据手机号获取会员
     *
     * @param mobilePhone 手机号
     * @return 会员
     */
    private Long findMember(String mobilePhone, String userName) {
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile", mobilePhone)
                .or().eq("username", userName);
        return memberService.count(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean audit(String id, Integer passed) {
        Store store = this.getById(id);
        if (store == null) {
            throw new ServiceException(ResultCode.STORE_NOT_EXIST);
        }
        if (passed == 0) {
            store.setStoreDisable(StoreStatusEnum.OPEN.value());
            //修改会员 表示已有店铺
            Member member = memberService.getById(store.getMemberId());
            member.setHaveStore(true);
            member.setStoreId(id);
            // 同步店铺信息到个人信息
            syncShopInfoToUserInfo(store, member);
            memberService.updateById(member);
            //创建店员
            ClerkAddDTO clerkAddDTO = new ClerkAddDTO();
            clerkAddDTO.setMemberId(member.getId());
            clerkAddDTO.setIsSuper(true);
            clerkAddDTO.setShopkeeper(true);
            clerkAddDTO.setStoreId(id);
            clerkService.saveClerk(clerkAddDTO);
            //设定商家的结算日
            storeDetailService.update(new LambdaUpdateWrapper<StoreDetail>()
                    .eq(StoreDetail::getStoreId, id)
                    .set(StoreDetail::getSettlementDay, new DateTime()));
        } else {
            store.setStoreDisable(StoreStatusEnum.REFUSED.value());
        }
        cache.remove(CachePrefix.STORE.getPrefix() + store.getId());
        return this.updateById(store);
    }

    /**
     * 同步店铺信息到个人信息
     * @param store
     */
    private void syncShopInfoToUserInfo(Store store, Member member) {
        StoreDetail storeDetail = storeDetailService.getStoreDetail(store.getId());
        member.setFullName(store.getFullName());//姓名
        member.setNickName(storeDetail.getStoreName());//昵称
        member.setMemberDesc(store.getStoreDesc());//个人简介
        member.setIdCard(storeDetail.getLegalId());//证件号
        member.setMobile(storeDetail.getLinkPhone());//联系电话
        member.setFace(store.getStoreLogo());//照片
        member.setHeight(store.getHeight());//身高
        member.setWeight(store.getWeight());//体重
        member.setOccupation(store.getOccupation());//职业
    }

    @Override
    public boolean disable(String id) {
        Store store = this.getById(id);
        if (store != null) {

            LambdaUpdateWrapper<Store> storeLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            storeLambdaUpdateWrapper.eq(Store::getId, id);
            storeLambdaUpdateWrapper.set(Store::getStoreDisable, StoreStatusEnum.CLOSED.value());
            boolean update = this.update(storeLambdaUpdateWrapper);
            //下架所有此店铺商品
            if (update) {
                goodsService.underStoreGoods(id);
            }

            //删除店员token
            clerkService.list(new LambdaQueryWrapper<Clerk>().eq(Clerk::getStoreId, id)).forEach(clerk -> {
                cache.vagueDel(CachePrefix.ACCESS_TOKEN.getPrefix(UserEnums.STORE, clerk.getMemberId()));
                cache.vagueDel(CachePrefix.REFRESH_TOKEN.getPrefix(UserEnums.STORE, clerk.getMemberId()));
            });

            return update;
        }

        throw new ServiceException(ResultCode.STORE_NOT_EXIST);
    }

    @Override
    public boolean enable(String id) {
        Store store = this.getById(id);
        if (store != null) {
            store.setStoreDisable(StoreStatusEnum.OPEN.value());
            return this.updateById(store);
        }
        throw new ServiceException(ResultCode.STORE_NOT_EXIST);
    }

    /**
     * 获取E证通设置
     *
     * @return E证通设置
     */
    private EidSetting getEidConfig() {
        Setting setting = settingService.get(SettingEnum.EID_SETTING.name());
        return new Gson().fromJson(setting.getSettingValue(), EidSetting.class);
    }

    /**
     * 校验用户是否完成E证通核身
     * 且店铺申请信息与E证通实名信息一致
     * @param storeCompanyDTO 店铺申请信息
     */
    private void checkEidVerify(StoreCompanyDTO storeCompanyDTO) {
        EidSetting eidConfig = getEidConfig();
        if(eidConfig != null) {
            if(eidConfig.getIsOpen()){
                AuthUser authUser = UserContext.getCurrentUser();
                if (authUser == null) {
                    throw new ServiceException(ResultCode.USER_NOT_LOGIN);
                }

                // 查询用户最新一条成功的核身记录
                LambdaQueryWrapper<MemberEidRecord> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(MemberEidRecord::getMemberId, authUser.getId());
                queryWrapper.eq(MemberEidRecord::getStatus, "SUCCESS");
                queryWrapper.orderByDesc(MemberEidRecord::getCreateTime);
                queryWrapper.last("LIMIT 1");

                MemberEidRecord eidRecord = memberEidRecordService.getOne(queryWrapper);

                // 未完成核身
                if (eidRecord == null) {
                    throw new ServiceException(ResultCode.EID_VERIFY_REQUIRED);
                }

                // 信息不一致
                if (!CharSequenceUtil.equals(storeCompanyDTO.getLegalName(), eidRecord.getName())
                        || !CharSequenceUtil.equals(storeCompanyDTO.getLegalId(), eidRecord.getIdCard())) {
                    throw new ServiceException(ResultCode.EID_INFO_NOT_MATCH);
                }
            }
        }
    }

    @Override
    public boolean applyFirstStep(StoreCompanyDTO storeCompanyDTO) {
        AuthUser authUser = Objects.requireNonNull(UserContext.getCurrentUser());
        Member member = memberService.getById(authUser.getId());
        //需要人脸认证判断
        if(member != null && member.getNoFaceAuth() == 0){
            //校验用户是否完成E证通核身
            checkEidVerify(storeCompanyDTO);
        }
        return doApplyFirstStep(storeCompanyDTO, true);
    }

    @Override
    public boolean saveCompanyInfoBeforeEidVerify(StoreCompanyDTO storeCompanyDTO) {
        AuthUser authUser = Objects.requireNonNull(UserContext.getCurrentUser());
        if (memberEidRecordService.hasSuccessfulVerification(authUser.getId())) {
            throw new ServiceException(ResultCode.EID_ALREADY_VERIFIED);
        }
        return doApplyFirstStep(storeCompanyDTO, false);
    }

    @Override
    public boolean saveTryOnStaffBeforeEidVerify(TryOnStaffApplyDTO dto) {
        AuthUser authUser = Objects.requireNonNull(UserContext.getCurrentUser());
        if (memberEidRecordService.hasSuccessfulVerification(authUser.getId())) {
            throw new ServiceException(ResultCode.EID_ALREADY_VERIFIED);
        }
        return doApplyTryOnStaffFirstStep(dto);
    }

    private boolean doApplyTryOnStaffFirstStep(TryOnStaffApplyDTO dto) {
        Store store = getStoreByMember();
        AuthUser authUser = Objects.requireNonNull(UserContext.getCurrentUser());
        assertTryOnStaffIdCardUnique(dto.getIdCard(), store != null ? store.getId() : null, authUser.getId());
        if (store == null) {
            Member member = memberService.getById(authUser.getId());
            store = new Store(member);
            applyTryOnStaffToStore(store, dto);
            if (CharSequenceUtil.isBlank(store.getStoreCenter())) {
                store.setStoreCenter("0,0");
            }
            store.setStoreDisable(StoreStatusEnum.APPLYING.name());
            this.save(store);
            StoreDetail detail = new StoreDetail();
            detail.setStoreId(store.getId());
            applyTryOnStaffToStoreDetail(detail, dto);
            return storeDetailService.save(detail);
        }
        checkStoreStatusAllowCompanyEditBeforeEid(store);
        applyTryOnStaffToStore(store, dto);
        store.setStoreDisable(StoreStatusEnum.APPLYING.name());
        this.updateById(store);
        StoreDetail storeDetail = storeDetailService.getStoreDetail(store.getId());
        if (storeDetail == null) {
            storeDetail = new StoreDetail();
            storeDetail.setStoreId(store.getId());
            applyTryOnStaffToStoreDetail(storeDetail, dto);
            return storeDetailService.save(storeDetail);
        }
        applyTryOnStaffToStoreDetail(storeDetail, dto);
        return storeDetailService.updateById(storeDetail);
    }

    private void applyTryOnStaffToStore(Store store, TryOnStaffApplyDTO dto) {
        store.setFullName(dto.getRealName());
        store.setStoreName(dto.getNickname());
        store.setStoreDesc(dto.getIntro());
        store.setHeight(dto.getHeight());
        store.setWeight(dto.getWeight());
        store.setOccupation(dto.getOccupation());
        store.setStoreLogo(dto.getPhotoUrl());
    }

    /**
     * 试穿员身份证：18 位只允许在系统中出现一次（排除当前店铺明细、当前会员已绑定）。
     */
    private void assertTryOnStaffIdCardUnique(String idCard, String excludeStoreId, String excludeMemberId) {
        if (CharSequenceUtil.isBlank(idCard)) {
            return;
        }
        String normalized = idCard.trim().toUpperCase();
        if (normalized.length() != 18) {
            return;
        }
        LambdaQueryWrapper<StoreDetail> detailQuery = new LambdaQueryWrapper<StoreDetail>()
                .apply("UPPER(legal_id) = {0}", normalized);
        if (CharSequenceUtil.isNotEmpty(excludeStoreId)) {
            detailQuery.ne(StoreDetail::getStoreId, excludeStoreId);
        }
        if (storeDetailService.count(detailQuery) > 0) {
            throw new ServiceException(ResultCode.EID_ID_CARD_DUPLICATE);
        }
        LambdaQueryWrapper<Member> memberQuery = new LambdaQueryWrapper<Member>()
                .apply("UPPER(id_card) = {0}", normalized);
        if (CharSequenceUtil.isNotEmpty(excludeMemberId)) {
            memberQuery.ne(Member::getId, excludeMemberId);
        }
        if (memberService.count(memberQuery) > 0) {
            throw new ServiceException(ResultCode.EID_ID_CARD_DUPLICATE);
        }
    }

    private void applyTryOnStaffToStoreDetail(StoreDetail detail, TryOnStaffApplyDTO dto) {
        detail.setStoreName(dto.getNickname());
        detail.setLegalName(dto.getRealName());
        detail.setLegalId(CharSequenceUtil.isBlank(dto.getIdCard()) ? ""
                : dto.getIdCard().trim().toUpperCase());
        detail.setLegalPhoto(dto.getIdCardImageUrl());
        detail.setLinkName(dto.getRealName());
        detail.setLinkPhone(dto.getPhone());
        detail.setCompanyName(dto.getNickname());
        detail.setCompanyAddress("-");
        detail.setCompanyPhone(dto.getPhone());
        detail.setLicencePhoto(dto.getIdCardImageUrl());
    }

    /**
     * @param strictStoreStatus true 时与历史逻辑一致：OPEN/CLOSED/APPLYING 不允许改第一步资料
     */
    private boolean doApplyFirstStep(StoreCompanyDTO storeCompanyDTO, boolean strictStoreStatus) {
        Store store = getStoreByMember();

        if (store == null) {
            AuthUser authUser = Objects.requireNonNull(UserContext.getCurrentUser());
            Member member = memberService.getById(authUser.getId());
            store = new Store(member);
            BeanUtil.copyProperties(storeCompanyDTO, store);
            store.setStoreDisable(StoreStatusEnum.APPLYING.name());
            this.save(store);
            StoreDetail storeDetail = new StoreDetail();
            storeDetail.setStoreId(store.getId());
            BeanUtil.copyProperties(storeCompanyDTO, storeDetail);
            return storeDetailService.save(storeDetail);
        }

        if (strictStoreStatus) {
            checkStoreStatus(store);
        } else {
            checkStoreStatusAllowCompanyEditBeforeEid(store);
        }
        BeanUtil.copyProperties(storeCompanyDTO, store);
        store.setStoreDisable(StoreStatusEnum.APPLYING.name());
        this.updateById(store);
        StoreDetail storeDetail = storeDetailService.getStoreDetail(store.getId());
        if (storeDetail == null) {
            storeDetail = new StoreDetail();
            storeDetail.setStoreId(store.getId());
            BeanUtil.copyProperties(storeCompanyDTO, storeDetail);
            return storeDetailService.save(storeDetail);
        }
        BeanUtil.copyProperties(storeCompanyDTO, storeDetail);
        return storeDetailService.updateById(storeDetail);
    }

    /**
     * E 证通资料阶段：仅禁止已开业/已关闭店铺改企业资料；允许申请中反复修改。
     */
    private void checkStoreStatusAllowCompanyEditBeforeEid(Store store) {
        String s = store.getStoreDisable();
        if (StoreStatusEnum.OPEN.name().equals(s) || StoreStatusEnum.CLOSED.name().equals(s)) {
            throw new ServiceException(ResultCode.STORE_STATUS_ERROR);
        }
    }

    @Override
    public boolean applySecondStep(StoreBankDTO storeBankDTO) {

        //获取当前操作的店铺
        Store store = getStoreByMember();
        //校验店铺状态
        checkStoreStatus(store);
        StoreDetail storeDetail = storeDetailService.getStoreDetail(store.getId());
        //设置店铺的银行信息
        BeanUtil.copyProperties(storeBankDTO, storeDetail);
        return storeDetailService.updateById(storeDetail);
    }

    @Override
    public boolean applyThirdStep(StoreOtherInfoDTO storeOtherInfoDTO) {
        //获取当前操作的店铺
        Store store = getStoreByMember();

        //校验店铺状态
        checkStoreStatus(store);
        BeanUtil.copyProperties(storeOtherInfoDTO, store);

        StoreDetail storeDetail = storeDetailService.getStoreDetail(store.getId());
        //设置店铺的其他信息
        BeanUtil.copyProperties(storeOtherInfoDTO, storeDetail);
        //设置店铺经营范围
        storeDetail.setGoodsManagementCategory(storeOtherInfoDTO.getGoodsManagementCategory());
        //最后一步申请，给予店铺设置库存预警默认值
        storeDetail.setStockWarning(10);
        //修改店铺详细信息
        storeDetailService.updateById(storeDetail);
        //设置店铺名称,修改店铺信息
        store.setStoreDisable(StoreStatusEnum.APPLYING.name());
        return this.updateById(store);
    }

    /**
     * 申请店铺时 对店铺状态进行校验判定
     *
     * @param store 店铺
     */
    private void checkStoreStatus(Store store) {

        //如果店铺状态为已开启、已关闭、申请中，则抛出异常
        if (store.getStoreDisable().equals(StoreStatusEnum.OPEN.name())
                || store.getStoreDisable().equals(StoreStatusEnum.CLOSED.name())
                || store.getStoreDisable().equals(StoreStatusEnum.APPLYING.name())
        ) {
            throw new ServiceException(ResultCode.STORE_STATUS_ERROR);
        }

    }

    @Override
    public void updateStoreGoodsNum(String storeId, Long num) {
        //修改店铺商品数量
        this.update(new LambdaUpdateWrapper<Store>()
                .set(Store::getGoodsNum, num)
                .eq(Store::getId, storeId));
    }

    @Override
    public void updateStoreCollectionNum(CollectionDTO collectionDTO) {
        baseMapper.updateCollection(collectionDTO.getId(), collectionDTO.getNum());
    }

    @Override
    public void storeToClerk() {
        //清空店铺信息方便重新导入不会有重复数据
        clerkService.remove(new LambdaQueryWrapper<Clerk>().eq(Clerk::getShopkeeper, true));
        List<Clerk> clerkList = new ArrayList<>();
        //遍历已开启的店铺
        for (Store store : this.list(new LambdaQueryWrapper<Store>().eq(Store::getDeleteFlag, false).eq(Store::getStoreDisable,
                StoreStatusEnum.OPEN.name()))) {
            clerkList.add(new Clerk(store));
        }
        clerkService.saveBatch(clerkList);
    }

    @Override
    public List<GoodsSku> getToMemberHistory(String memberId) {
        AuthUser currentUser = UserContext.getCurrentUser();
        List<String> skuIdList = new ArrayList<>();
        for (FootPrint footPrint :
                footprintService.list(new LambdaUpdateWrapper<FootPrint>().eq(FootPrint::getStoreId, currentUser.getStoreId()).eq(FootPrint::getMemberId, memberId))) {
            if (footPrint.getSkuId() != null) {
                skuIdList.add(footPrint.getSkuId());
            }
        }
        return goodsSkuService.getGoodsSkuByIdFromCache(skuIdList);
    }

    @Override
    public IPage<StoreTradeRankingVO> getTradeRanking(PageVO page) {
        QueryWrapper<Store> wrapper = new QueryWrapper<>();
        // 只查询正常营业的店铺
        wrapper.eq("store_disable", StoreStatusEnum.OPEN.value());
        wrapper.notLike("s.member_name", "template");
        wrapper.notLike("s.store_name", "template");

        // 1. 查询店铺排行榜
        IPage<StoreTradeRankingVO> resultPage = this.baseMapper.getStoreTradeRankingList(PageUtil.initPage(page), wrapper);
        List<StoreTradeRankingVO> records = resultPage.getRecords();

        if (CollectionUtil.isEmpty(records)) {
            return resultPage;
        }

        // 2. 给每个店铺 【加载前5个商品】
        for (StoreTradeRankingVO vo : records) {
            List<Goods> goodsList = goodsService.list(new LambdaQueryWrapper<Goods>()
                    .eq(Goods::getStoreId, vo.getId()) // 按店铺ID查
                    .eq(Goods::getMarketEnable, GoodsStatusEnum.UPPER.name()) // 只查上架商品
                    .orderByDesc(Goods::getBuyCount) // 按销量倒序（最热在前）
                    .last("LIMIT 5") // 只取前5个
            );
            List<GoodsTradeRankingVO> list = goodsList.stream().map(goods -> {
                GoodsTradeRankingVO goodsTradeRankingVO = new GoodsTradeRankingVO();
                BeanUtil.copyProperties(goods, goodsTradeRankingVO);
                goodsTradeRankingVO.setGoodsId(goods.getId());
                return goodsTradeRankingVO;
            }).toList();
            vo.setGoodsList(list);
        }

        // 3. 自动赋值排名序号（第几名）
        int start = (page.getPageNumber() - 1) * page.getPageSize();
        for (int i = 0; i < records.size(); i++) {
            records.get(i).setRank(start + i + 1);
        }

        return resultPage;
    }

    /**
     * 获取当前登录操作的店铺
     *
     * @return 店铺信息
     */
    private Store getStoreByMember() {
        LambdaQueryWrapper<Store> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (UserContext.getCurrentUser() != null) {
            lambdaQueryWrapper.eq(Store::getMemberId, UserContext.getCurrentUser().getId());
        }
        return this.getOne(lambdaQueryWrapper, false);
    }

}