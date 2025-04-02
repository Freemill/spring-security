package io.security.springsecuritymaster;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @GetMapping("/user")
    public String user() {
        return "user";
    }

    @GetMapping("/myPage/points")
    public String myPage() {
        return "myPage";
    }

    @GetMapping("/manager")
    public String manager() {
        return "manager";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @GetMapping("/admin/payment")
    public String adminPayment() {
        return "adminPayment";
    }

    @GetMapping("/resource/address_01")
    public String address_01() {
        return "address_01";
    }

    @GetMapping("/resource/address01")
    public String address01() {
        return "address01";
    }

    @PostMapping("/post")
    public String post() {
        return "post";
    }


//    @GetMapping("/")
//    public Authentication index(Authentication authentication) {
//
//        return authentication;
//    }
//
//    @GetMapping("/loginPage")
//    public String loginPage() {
//        return "loginPage";
//    }
//
//    @GetMapping("/home")
//    public String home() {
//        return "home";
//    }
//
//    @GetMapping("/anonymous")
//    public String anonymous() {
//        return "anonymous";
//    }
//
//    @GetMapping("/authentication")
//    public String authentication(Authentication authentication) {
//        if (authentication instanceof AnonymousAuthenticationToken) {
//            return "anonymous";
//        } else {
//            return "not anonymous";
//        }
//    }
//
//    @GetMapping("/anonymousContext")
//    public String anonymousContext(@CurrentSecurityContext SecurityContext securityContext) {
//        return securityContext.getAuthentication().getName();
//    }
//
//    @GetMapping("/logoutSuccess")
//    public String logoutSuccess() {
//        return "logoutSuccess";
//    }
//
//    @GetMapping("/login")
//    public String login() {
//        return "loginPage";
//    }
//
//    @GetMapping("/denied")
//    public String denied() {
//        return "denied";
//    }
//
//    @PostMapping("/csrf")
//    public String csrf() {
//        return "csrf 적용됨.";
//    }
}
