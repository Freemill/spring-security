package io.security.springsecuritymaster.method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.Advisor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthenticatedAuthorizationManager;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@EnableMethodSecurity(prePostEnabled = false)
public class MethodSecurityConfig {

    @Bean
    public MethodInterceptor methodInterceptor() {
        AuthorizationManager<MethodInvocation> authorizationManager = new AuthenticatedAuthorizationManager<>();

        return new CustomMethodInterceptor(authorizationManager);
    }
    // MethodInterceptor 타고 들어가보면 Interceptor가 Advice를 구현하고 있다. 따라서 MethodInterceptor가 그자체로 Advice이다.

    @Bean
    public Pointcut pointcut() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* io.security.springsecuritymaster.DataService.*(..))");
        return pointcut;
    }

    @Bean
    public Advisor serviceAdvisor() {
        return new DefaultPointcutAdvisor(pointcut(), methodInterceptor());
    }
}
