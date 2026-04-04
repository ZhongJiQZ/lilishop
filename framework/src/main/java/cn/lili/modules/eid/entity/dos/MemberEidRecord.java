package cn.lili.modules.eid.entity.dos;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 会员E证通核身记录
 *
 * @author lensing
 * @since 2026-04-04 15:18:56
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("li_member_eid_record")
@Schema(description = "会员E证通核身记录")
public class MemberEidRecord extends BaseEntity {

    @Schema(description = "用户ID")
    private String memberId;

    @Schema(description = "核身令牌", required = true)
    private String eidToken;

    // 整体结果：0成功
    @Schema(description = "错误码")
    private Long errCode;
    @Schema(description = "错误信息")
    private String errMsg;

    @Schema(description = "状态 SUCCESS 成功 / FAIL 失败")
    private String status;

    // 身份信息
    @Schema(description = "姓名")
    private String name;
    @Schema(description = "身份证")
    private String idCard;
//    @Schema(description = "证件号码类型 0身份证 1港澳台居住证 2其他")
//    private String useIdType;
//    @Schema(description = "身份校验环节采用的信息上传方式 NFC/OCR/手动输入/其他")
//    private String idInfoFrom;

    // OCR信息
    @Schema(description = "民族")
    private String ocrNation;
    @Schema(description = "家庭住址")
    private String ocrAddress;
    @Schema(description = "生日")
    private String ocrBirth;
    @Schema(description = "签发机关")
    private String ocrAuthority;
    @Schema(description = "身份证有效日期")
    private String ocrValidDate;
    @Schema(description = "性别")
    private String ocrGender;

    // 活体&比对结果
    @Schema(description = "本次流程最终活体结果 0为成功")
    private Long liveStatus;
    @Schema(description = "本次流程最终活体结果描述")
    private String liveMsg;
    @Schema(description = "本次一比一结果。0为成功")
    private Long compareStatus;
    @Schema(description = "本次一比一结果描述")
    private String compareMsg;
    @Schema(description = "本次流程活体一比一的分数 取值范围 [0.00, 100.00]")
    private String sim; // 相似度大于等于70时才判断为同一人，阈值不支持自定义。- 阈值70的误通过率为万分之一。
    @Schema(description = "本次流程最终比对库源类型。 权威库/业务方自有库（用户上传照片、客户的混合库、混合部署库）/二次验证库/人工审核库")
    private String compareLibType; // 权威库/自有库
    @Schema(description = "枚举活体检测类型 0：未知 1：数字活体 2：动作活体 3：静默活体 4：一闪活体（动作+光线）")
    private Long livenessMode; // 活体类型

    // 手机号
    @Schema(description = "手机号码")
    private String mobile;

    // 攻击风险标签
    /**
     * 01-用户全程闭眼
     * 02-用户未完成指定动作
     * 03-疑似翻拍攻击
     * 04-疑似合成攻击
     * 05-疑似黑产模版
     * 06-疑似存在水印
     * 07-反光校验未通过
     * 08-疑似中途换人
     * 09-人脸质量过差
     * 10-距离校验不通过
     * 11-疑似对抗样本攻击
     * 12-嘴巴区域疑似存在攻击痕迹
     * 13-眼睛区域疑似存在攻击痕迹
     * 14-眼睛或嘴巴被遮挡
     */
//    @Schema(description = "描述当前请求活体阶段被拒绝的详细原因，该参数仅限PLUS版本核身服务返回")
//    private String livenessInfoTag;

    // 意愿核身相关（朗读/问答/点头）
    /**
     * 0: "成功"
     * -1: "参数错误"
     * -2: "系统异常"
     * -101: "请保持人脸在框内"
     * -102: "检测到多张人脸"
     * -103: "人脸检测失败"
     * -104: "人脸检测不完整"
     * -105: "请勿遮挡眼睛"
     * -106: "请勿遮挡嘴巴"
     * -107: "请勿遮挡鼻子"
     * -201: "人脸比对相似度低"
     * -202: "人脸比对失败"
     * -301: "意愿核验不通过"
     * -800: "前端不兼容错误"
     * -801: "用户未授权摄像头和麦克风权限"
     * -802: "核验流程异常中断，请勿切屏或进行其他操作"
     * -803: "用户主动关闭链接/异常断开链接"
     * -804: "用户当前网络不稳定，请重试"
     * -998: "系统数据异常"
     * -999: "系统未知错误，请联系人工核实"
     */
    @Schema(description = "意愿核身错误码 ")
    private String FinalResultDetailCode;
    @Schema(description = "意愿核身错误信息")
    private String finalResultMessage;

    // 关键证件图片（最佳帧、OCR正反面、头像）
    @Schema(description = "活体比对最佳帧Base64编码")
    private String bestFrame;
    @Schema(description = "OCR正面照片的base64编码")
    private String ocrFront;
    @Schema(description = "OCR反面照片的base64编码")
    private String ocrBack;
    @Schema(description = "身份证正面人像图base64编码")
    private String avatar;

    @Schema(description = "完整结果JSON")
    private String resultJson;

}