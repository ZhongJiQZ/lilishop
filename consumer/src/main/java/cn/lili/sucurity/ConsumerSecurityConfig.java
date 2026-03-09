package cn.lili.sucurity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author paulG
 * @since 2022/2/18
 **/
@Configuration
public class ConsumerSecurityConfig {

    @Bean
    public SecurityFilterChain consumerSecurityFilterChain(HttpSecurity http) throws Exception {
        http.formLogin(form -> form.disable());
        return http.build();
    }
}
