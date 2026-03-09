package cn.lili.controller.security;

import cn.lili.common.properties.IgnoredUrlsProperties;
import cn.lili.common.security.CustomAccessDeniedHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
/**
 * 统一安全配置 - 通用接口
 * 通用服务默认放行，保留统一异常处理与CORS配置
 * 若后续接口需鉴权，请按模块前缀与角色规范接入
 */
public class CommonSecurityConfig {

    @Autowired
    private CorsConfigurationSource corsConfigurationSource;

    @Autowired
    private IgnoredUrlsProperties ignoredUrlsProperties;

    @Autowired
    private CustomAccessDeniedHandler accessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 配置不需要授权的URL
        String[] ignoredUrls = ignoredUrlsProperties.getUrls().toArray(new String[0]);
        
        http
            .headers(headers -> headers.frameOptions(frame -> frame.disable()))
            .authorizeHttpRequests(auth -> {
                // 配置忽略的URL
                auth.requestMatchers(ignoredUrls).permitAll();
                // 其他请求允许访问（因为这是common-api，主要提供公共服务）
                auth.anyRequest().permitAll();
            })
            .cors(cors -> cors.configurationSource(corsConfigurationSource))
            .csrf(csrf -> csrf.disable())
            // 统一异常处理与禁用交互式登录方式
            .exceptionHandling(eh -> eh
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint((req, res, ex) ->
                    cn.lili.common.utils.ResponseUtil.output(res, 403,
                        cn.lili.common.utils.ResponseUtil.resultMap(false, 403, "未登录或token失效"))
                )
            )
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable());
        return http.build();
    }

}
