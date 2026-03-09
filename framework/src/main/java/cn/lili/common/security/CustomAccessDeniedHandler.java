package cn.lili.common.security;


import cn.lili.common.utils.ResponseUtil;
// 替换 Lombok 的 @Slf4j 为显式日志器
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 自定义 拒绝访问响应
 *
 * @author Chopper
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {


    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) {
        ResponseUtil.output(response, ResponseUtil.resultMap(false, 401, "抱歉，您没有访问权限"));
    }

}
