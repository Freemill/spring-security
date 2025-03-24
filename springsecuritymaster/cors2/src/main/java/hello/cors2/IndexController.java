package hello.cors2;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class IndexController {

    @GetMapping("/users")
    public String users() {
        return "{\"name\": \"hong gil dong\"}";

    }
}
