package io.security.springsecuritymaster;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class IndexController {
    AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();

    @GetMapping("/")
    public String index() {
        Authentication authentication = SecurityContextHolder.getContextHolderStrategy().getContext().getAuthentication();
        // 만약 인증을 받지 못한 상태라면 authentication은 anonymous authentication일것이다.
        // 따라서 이걸 구분할 필요가 있다. trustResolver를 사용하자.
        return trustResolver.isAnonymous(authentication) ? "anonymous" : "authenticated";
    }

    @GetMapping("/user")
    public User user(@AuthenticationPrincipal User user) {
        return user;
    }

    @GetMapping("/user2")
    public String user(@AuthenticationPrincipal(expression = "username") String user) {
        return user;
    }

    @GetMapping("/currentUser")
    public User currentUser(@CurrentUser User user) {
        return user;
    }

    @GetMapping("/currentUser")
    public String currentUsername(@CurrentUsername String user) {
        return user;
    }


    @GetMapping("/db")
    public String db() {
        return "db";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

}
