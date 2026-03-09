//package cn.lili.controller.common;
//
//import cn.lili.common.utils.IpHelper;
//import cn.lili.common.enums.ResultUtil;
//import cn.lili.common.vo.ResultMessage;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
//import jakarta.servlet.http.HttpServletRequest;
//
///**
// * 管理端,IP接口
// *
// * @author Chopper
// * @since 2020-02-25 14:10:16
// */
//@RestController
//@Tag(name = "获取IP信息以及天气")
//@RequestMapping("/common/common/ip")
//public class IpInfoManagerController {
//    @Autowired
//    private IpHelper ipHelper;
//
//    @RequestMapping(value = "/info", method = RequestMethod.GET)
//    @Operation(summary = "IP及天气相关信息")
//    public ResultMessage<Object> upload(HttpServletRequest request) {
//
//        String result = ipHelper.getIpCity(request);
//        return ResultUtil.data(result);
//    }
//}