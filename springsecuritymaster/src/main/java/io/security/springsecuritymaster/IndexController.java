package io.security.springsecuritymaster;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class IndexController {
    private final DataService dataService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/user")
    public String user() {
        return dataService.getUser();
    }

    @GetMapping("/owner")
    public Account owner(String name) {
        return dataService.getOwner(name);
    }

    @GetMapping("/display")
    public String secure() {
        return dataService.display();
    }

    @GetMapping("/login")
    public String login(HttpServletRequest request, MemberDto memberDto) throws ServletException {
        request.login(memberDto.getUsername(), memberDto.getPassword());
        System.out.println("login is success");

        return "login";
    }

    @GetMapping("/users")
    public List<MemberDto> users(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean authenticate = request.authenticate(response);

        if (authenticate) {
            return List.of(new MemberDto("user", "1111"));
        }

        return Collections.emptyList();
    }
}
