package io.security.springsecuritymaster;

import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("logoutSuccess").permitAll()
                        .anyRequest().authenticated())
                .csrf(csrf -> csrf.disable())

                .logout(logout -> logout
                        .logoutUrl("/logoutProc")
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logoutProc", "POST"))
                        // logout url이랑 logoutRequestMatcher 두개가 동시에 있으면
                        // logoutRequestMatcher가 적용된다.
                        .logoutSuccessUrl("/logoutSuccess")
                        //여기로 가려면 여기 경로도 허용해줘야한다.
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.sendRedirect("/");
                        })
                        .deleteCookies("JSESSIONID", "remember-me")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .addLogoutHandler((request, response, authentication) -> {
                            HttpSession session = request.getSession();
                            session.invalidate();
                            SecurityContextHolder.getContextHolderStrategy().getContext().setAuthentication(null);
                            SecurityContextHolder.getContextHolderStrategy().clearContext();
                        })
                        .permitAll()
                )
        ;

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withUsername("user").password("{noop}1111").roles("USER").build();

        return new InMemoryUserDetailsManager(user);
    }
}
