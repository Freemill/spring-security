package io.security.springsecuritymaster;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

//        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
//        AuthenticationManager authenticationManager = builder.build();
////        AuthenticationManager object = builder.getObject();
//
//        http
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/", "/api/login").permitAll()
//                        .anyRequest().authenticated())
////                .formLogin(Customizer.withDefaults())
//                .authenticationManager(authenticationManager)
//                .addFilterBefore(customAuthenticationFilter(http, authenticationManager), UsernamePasswordAuthenticationFilter.class);
//        ;

        http
                .authorizeRequests(auth -> auth
                        .anyRequest().authenticated())
                .formLogin(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
         return new CustomUserDetailsService();
    }

//    public CustomAuthenticationFilter customAuthenticationFilter(HttpSecurity http, AuthenticationManager authenticationManager) {
//        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(http);
//        customAuthenticationFilter.setAuthenticationManager(authenticationManager);
//
//        return customAuthenticationFilter;
//    }

//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails user = User.withUsername("user").password("{noop}1111").roles("USER").build();
//
//        return new InMemoryUserDetailsManager(user);
//    }
}
