![img.png](img.png)
![img_1.png](img_1.png)

<br>
코드로 보자. 

index.html
```html
<!DOCTYPE html>
<html>
<body>
<img style="width: 45px; padding-right: 5px" src="/images/spring-security-project.png" alt="">
</body>
</html>
```
index.html 이 파일이 로딩 될 때, 이미지 태그도 함께 로드 된다. 그럼 이 이미지를 서버로 요청을 한다. 함께 읽어달라고 그럼 이 이미지를 요청 할 떄, <br>
스프링 시큐리티가 이 요청에 대해서는 필터를 거치지 않게 하려고 한다. 기본은 모두 검증을 한다. <br>

```java
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
@Configuration
public class SecurityConfig {

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (webSecurity) -> {
            webSecurity.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().authenticated())
                .formLogin(Customizer.withDefaults());

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

여기서 webSecurity.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations()); 이 부분을 살펴 보면 <br>
![img_2.png](img_2.png)
![img_3.png](img_3.png)
<br>
이렇게 기본적으로 설정된 경로들을 볼 수 있다. 저 경로에 있는 파일들은 필터들을 거치지 않는다. <br>
흐름들을 보자. <br>

![img_4.png](img_4.png)
먼저 WebSecurityCustomizer bean을 만든다. <br>
![img_5.png](img_5.png) 
![img_6.png](img_6.png) <Br>
 조금전에 설정한 정적 자원들을 추가하고 있다.
![img_7.png](img_7.png) <br>
어디에 추가하냐면? ignoredRequests에 추가하고 있다. <br>
![img_8.png](img_8.png) <br>
그리고나서 궁극적으로는 for(RequestMatcher ignoreRequest : this.ignoreRequests)를 돌면서 SecurityFilterChain을 만들어서 <br>
여기에다가 ignoreRequest를 전달하는 것이다. SecurityFilterChain은 2개의 속성을 가지고 있다. <br>
![img_9.png](img_9.png) <br>
하나는 requestMatcher이고 또 하나는 filters이다. 2개를 가지고 있는 이유는 지금 client의 요청이 어떤 요청이었는지에 따라가지고 어떤 필터를 사용할것인가를 <br>
이 객체(DefaultSecurityFilterChain)를 통해서 판단할 수 있다. 어떤 client의 요청 정보가 RequestMathcer의 정보와 일치하게 되면, 그때 filters를 가지고 <br>
가서 요청을 처리해라라고 할 때 우리는 SecurityFilterChain을 사용할 수 있다. <br>
그리고 이 SecurityFilterChain을 사용하는 클래스가 어디였냐면 <br>
![img_10.png](img_10.png) <Br>
FilterChainProxy이다. 
![img_11.png](img_11.png) <br>
그리고 지금 객체를 만들고 있는데 이 객체는 DefaultFilterChain을 하나 만들었고 여기에 requestMatcher가 static, 즉 정적 자원을 설정한 RequestMatcher의 구현체가 잇다. <br>
그런데 또 for문을 돌면서 하나가 생성이 된다. <br>
![img_12.png](img_12.png) <br>
이건  <br>
![img_13.png](img_13.png) <Br>
여기서 설정한 bean이다. 즉, anyRequest().authenticated() 어떤 요청에도 인증을 받도록 하는 설정이다. <br>
![img_14.png](img_14.png)
<br>
그리고 이 필터를 사용하는 filter는 총 14개로 설정이 돼있다. <br>
여기서 주의깊게 봐야할 것은 securityFilterChain은 항상 2개의 속성이 있는데 그게 matcher와 filters이다. 그런데 지금 첫번째 DeafaultSecurityFilterChain <br>
을 보면 requestMatcher는 있는데 filters가 없다. 이것은 무슨 의미일까? 지금 client요청에 대해서 2개의 DeafaultSecurityFilterChain을 통해 그 요청을 <br>
처리해야한다. securityFilterChain은 여러개의 DefaultFilterChain을 가지면서 각각의 DefaultFilterChain을 순회하며 하나의 가장 적합한 <br>
SecurityFilterChain에 포함된 filter들을 가지고 와서 요청을 처리하는 것이다. 지금 만약 사용자가 요청을 날렸는데 그 요청이 아래의 그림에 <br>
![img_15.png](img_15.png) <br>
5개의 요청에 포함되어 버리면 SecurityFilterChainProxy는 DefaultSecurityFilterChain@7713에 포함된 filter들을 가지고 와서 처리한다는 의미이다. <br>
그런데 우리가 설정한것은 filter 자체가 없기 때문에 filter 보안 처리를 할 수 없는 것이다. 그럼 직접 요청을 해보자 <br>

![img_16.png](img_16.png)
![img_17.png](img_17.png)
요청에 대해서는 가장 먼저 FilterChainProxy가 받게 된다. <br>

![img_18.png](img_18.png)
![img_19.png](img_19.png)
![img_21.png](img_21.png)
<br>
쭉 순회 하면서 root 경로이니까 2번째 Filter와 매치가 된다. <br>
![img_24.png](img_24.png)
그래서 2번쨰 SecurityFilterChain에 있는 filters(14개)를 가지고 온다! <br>
그리고 또 요청이 온다. <br>
![img_25.png](img_25.png) <br>
이번에는 image요청이 왔다. <br>
이 요청에는 첫번째 필터가 매칭이 된다. <br>
![img_26.png](img_26.png) <br>
근데 filter가 0개이다. <br>
![img_27.png](img_27.png) <br>
그냥 return 해버린다. 그 아래 코드를 타야 security의 기능들을 사용하게 되는것이다.
<br>

그런데 위 방식 보다. 아래처럼 permitAll을 하는게 낫다. <br>
```java
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/images/**").permitAll()
                        .anyRequest().authenticated())
                .formLogin(Customizer.withDefaults());

        return http.build();
    }

``` 
<br>
그 이유는 <br>

![img_28.png](img_28.png)
![img_29.png](img_29.png) 
![img_31.png](img_31.png) <br>
자세한 내용은 뒤에서 학습하자. <br>
permitAll로 하면 일단 여기로 온다. 여기로 왔다는 것은 보안 필터를 탔다는 것이다. <br>
여기로 와서 권한 심사를 한다. 근데 이 방식이 왜 좋을까? 
<br>
![img_32.png](img_32.png)
그 이유는 여기 있다. <br>
이게 호출 됐다. 여기서는 new AuthorizationDecision 객체인다 그 값이 true이다. 이 말은 여기에 AuthorizationDesicion이 true면 어떤 요청에도<br>
이 뒷부분은 섹션9에 정적자원관리 25분 이후부터 다시 보자.<br>







 




