package cn.lili.modules.member.aop.annotation;


import java.lang.annotation.*;

/**
 * 会员平台币操作aop
 *
 * @author lensing
 * @since 2026/03/25 7:22 下午
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CoinLogPoint {

}
