package io.security.springsecuritymaster;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MethodController {

    private final DataService dataService;

    @PostMapping("/writeList")
    public List<Account> writeList(@RequestBody List<Account> data) {

        return dataService.writeList(data);
    }

    @PostMapping("/writeList")
    public Map<String, Account> writeMap(@RequestBody List<Account> data) {
        Map<String, Account> accountMap = data.stream()
                .collect(Collectors.toMap(account -> account.getOwner(), account -> account));

        return dataService.writeMap(accountMap);
    }

    @GetMapping("/readList")
    public List<Account> readList() {
        return dataService.readList();
    }

    @GetMapping("/readList")
    public Map<String, Account> readMap() {
        return dataService.readMap();
    }

}
