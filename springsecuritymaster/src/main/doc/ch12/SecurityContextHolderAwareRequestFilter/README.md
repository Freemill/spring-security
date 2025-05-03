![img.png](img.png)
![img_1.png](img_1.png)
![img_2.png](img_2.png)
![img_3.png](img_3.png)
SecurityContextHolderAwareRequestFilter가 있다. <br>
SecurityContextHolderAwareRequestFilter 이건 인증을 수행하기 위한 가장 핵심점인 클래스이다. 이것만 있어도 대부분의 인증관련 기능들은 수행할 수 있다. <br>
이 필터는 실행이 되면 HttpServlet3RequestFactory 클래스 객체를 생성한다. 이 클래스에는 setMethod들이 있다. 이 Method에 인증관련 객체들을 넘겨둔다. <br>
HttpServlet3RequestFactory 이 객체들은 인증을 수행할 수 있는 핵심 관련 객체들을 모두 가지고 있는 상태가 된다. <br>
그리고나서 이 HttpServlet3RequestFactory 객체가 어떤 클래스를 또 생성하냐면 Servlet3SecurityContextHolderAwareRequestWrapper를 생성한다. <br>
이 클래스는 HttpServletRequest를 wrapping하고 있는 클래스이다. <br>
즉, 이 클래스도 request 객체이다. 그런데 이 클래스를 보니 보안관련 메서드들이 추가적으로 정의 되어있다. <br>
인증을 수행하거나 인증관련 여러가지 편리한 메서드들을 추가해서 이 객체가 장차 spring mvc나 servlet에서 사용할 수 있는 객체가 된 것이다.<br>
최종적으로 이 request 객체들은 filter를 거쳐 servlet까지 전달된다. 만약 서블릿에서 logout을 하면 HttpServlet3RequestFactory가 가지는 <br>
logoutHandler가 Servlet3SecurityContextHolderAwareRequestWrapper의 logout을 실행시켜 준다. <br>
그리고 만약 로그인을 한다면, HttpServlet3RequestFactory의 AuthenticationManager를 참조해서 Servlet3SecurityContextHolderAwareRequestWrapper의 <br>
login을 수행할 것이다. <br>
이렇게 스프링 시큐리티와 서블릿간의 통합이 되는 것이다. <br>
즉, spring security는 인증을 수행할 수 있는 여러가지 객체를 제공하고 서블릿에서는 인증을 수행할 수 있는 추가적인 method를 정의해서 인증을 수행하게 되는것이다. <br>

![img_4.png](img_4.png)
HttServletRequest request로 전달된 객체가 Servlet3SecurityContextHolderAwareRequestWrapper이 객체이다 이 객체가 저기까지 전달이 된 것이다. <br>
그래서 request.login같은 추가적인 메서드가 보여지는 것이다. 그리고 여기에는 사용자의 id와 password가 있고 이걸 수행하면 마치 springsecurity에서 <br>
인증 filter에서 처리되는 과정들이 저 Login안에서 처리가 되는것이다. <br>
그러니까 굉장히 편리한것이다. 그것도 security가 아닌 servlet에서도 이런 기능들을 구현할 수 있는것이다. <br>


```java
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return webSecurity -> webSecurity.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/user").hasAuthority("ROLE_USER")
                        .requestMatchers("/db").hasAuthority("ROLE_DB")
                        .requestMatchers("/admin").hasAuthority("ROLE_ADMIN")
                        .anyRequest().permitAll())
//                .formLogin(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withUsername("user").password("{noop}1111").roles("USER").build();
        UserDetails db = User.withUsername("db").password("{noop}1111").roles("DB").build();
        UserDetails admin = User.withUsername("admin").password("{noop}1111").roles("ADMIN", "SECURE").build();

        return new InMemoryUserDetailsManager(user, db, admin);
    }
}
```
일단 먼저 초기화 과정을 확인해보자. 초기화 과정 속에서 스프링 security가 서블릿하고 통합되는 과정이 어떤식으로 이루어지는지 확인하고 인증처리와 기타 기능들을 확인하자<br>
먼저 <br>
![img_5.png](img_5.png)
이 필터는 초기화 과장이 되면서 <br>
![img_6.png](img_6.png)
이 필터가 갖는 속성들 ex) requestFactory, authenticationEntryPoint, authenticationManager등 인증과 관련된 여러 속성들을 set으로 저장을 시킨다. <br>
그리고나서 HttpServlet3RequestFactory까지 생성을 하게 된다. <br>
![img_7.png](img_7.png)
그 과정을 살펴보자. <br>
![img_8.png](img_8.png)
![img_9.png](img_9.png)
이 객체들은 여러 객체들을 초기화 과정에서 전달 받은것을 확인할 수 있다. <br>
![img_10.png](img_10.png)
이 객체들을 전달하고 있다. <br>
그렇게 되면 HttpServlet3RequestFactory이 클래스가 <br>
![img_11.png](img_11.png)
여러 속성들을 가지고 있게 된다. <br>
그리고 이 클래스는 아래를 보면 <br>
![img_12.png](img_12.png)
Servlet3SecurityContextHolderAwareRequestWrapper로 wrapper 클래스를 생성하고 있다. <br>
![img_13.png](img_13.png)
![img_14.png](img_14.png)
그리고 이 wrapper 클래스에는 여러 보안 관련된 메서드들이 정의되어 있다. <br>
근데 아래 login Method만 보더라도 <br>
![img_16.png](img_16.png)
이 Factory class로 부터 객체를 참조해서 수행하는 것을 볼 수 있다. <br>
그런데 method들을 보면 spring security의 클래스들을 이용하는것 뿐이라는것을 확인할 수 있다. <br>
서블릿에서 사용한다는 그 위치만 다를뿐이지 기능은 똑같다. <br>
이렇게 초기화 과정이 끝이났다. 요청을 해보면 <br>
root에 접속 시도. <br>
![img_17.png](img_17.png)
![img_18.png](img_18.png)
![img_19.png](img_19.png)
![img_20.png](img_20.png)
wrapper로 감싸고 저장하고 다음 필터로 넘어간다. <Br>
![img_21.png](img_21.png)
![img_22.png](img_22.png)
![img_23.png](img_23.png)
nextFilter가 Anonymous Filter이다. <br>
![img_24.png](img_24.png)
그리고 이 AnonymousAuthenticationFilter에 전달된 Request를 보면 <br>
![img_25.png](img_25.png)
Servlet3SecurityContextHolderAwareRequestWrapper 객체이다. <br>
![img_27.png](img_27.png)
그리고 이 객체가 여기까지 전달되는 것이다. <br>
<br>
한번 사용해보자. <br>

```java
@Data
@AllArgsConstructor
public class MemberDto {
    private String username;
    private String password;
}
```

```java
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
```
![img_28.png](img_28.png)
![img_29.png](img_29.png)
![img_30.png](img_30.png)
이 클래그의 login 실행 <br>
![img_31.png](img_31.png)







 