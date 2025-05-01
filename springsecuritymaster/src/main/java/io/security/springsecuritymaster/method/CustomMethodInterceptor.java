package io.security.springsecuritymaster.method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.nio.file.AccessDeniedException;

public class CustomMethodInterceptor implements MethodInterceptor {
    private final AuthorizationManager<MethodInvocation> authorizationManager;

    public CustomMethodInterceptor(AuthorizationManager<MethodInvocation> authorizationManager) {
        this.authorizationManager = authorizationManager;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Authentication authentication = SecurityContextHolder.getContextHolderStrategy().getContext().getAuthentication();
        if (authorizationManager.check(() -> authentication, invocation).isGranted()) {
            return invocation.proceed();
        }

        throw new AccessDeniedException("Access Denied");
    }
}
