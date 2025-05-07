package io.security.springsecuritymaster.controller;

import io.security.springsecuritymaster.domain.dto.AccountDto;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping(value = "/")
    public String dashboard() {
        return "/dashboard";
    }

    @GetMapping("/user")
    public String user() {
        return "/user";
    }

    @GetMapping("/manager")
    public String manager() {
        return "/manager";
    }

    @GetMapping(value = "/admin")
    public String admin() {
        return "/admin";
    }

    @GetMapping("/api")
    public String restDashboard(@AuthenticationPrincipal AccountDto accountDto) {
        return "rest/dashboard";
    }
}
