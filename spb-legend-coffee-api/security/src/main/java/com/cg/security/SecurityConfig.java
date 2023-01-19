package com.cg.security;

import com.cg.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private IUserService userService;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    public RestAuthenticationEntryPoint restServicesEntryPoint() {
        return new RestAuthenticationEntryPoint();
    }

    @Bean
    public CustomAccessDeniedHandler customAccessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().ignoringAntMatchers("/**").disable();
        http.httpBasic().authenticationEntryPoint(restServicesEntryPoint());

        http.authorizeRequests()
                .antMatchers(
                        "/api/auth/staff/login",
                        "/api/auth/login",
                        "/api/auth/forget-password",
                        "/api/users/update-password/*",
                        "/api/users/update-password",
                        "/api/otp/*",
                        "/api/auth/register"
                ).permitAll()
                .antMatchers(
                        "/",
                        "/login",
                        "/logout",
                        "/cp/login",
                        "/cp/logout",
                        "/register",
                        "/cp/forget-password",
                        "/cp/update-password/*",
                        "/error/*"
                ).permitAll()
                .antMatchers(
                        "/resources/**",
                        "/assets/**")
                .permitAll()
                .antMatchers(
                        "/resources/**",
                        "/template/**")
                .permitAll()
//                .antMatchers(
//                        "/api/comment/**",
//                        "/api/users/**,",
//                        "/api/auth",
//                        "/api/information/**",
//                        "/api/medias/**",
//                        "/api/notification/**",
//                        "/api/notification",
//                        "/api/posts/**",
//                        "/api/posts",
//                        "/api/auth/changePassword",
//                        "/api/reactions/**")
//                .hasAnyAuthority("CUSTOMER")
                .antMatchers(
                        "/v3/api-docs",
                        "/swagger-resources/configuration/ui",
                        "/configuration/ui",
                        "/swagger-resources",
                        "/swagger-resources/configuration/security",
                        "/configuration/security",
                        "/swagger-ui/**"
                ).permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
//                .loginProcessingUrl("/login")
                .loginProcessingUrl("/cp/login")
//                .loginPage("/login")
                .loginPage("/cp/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/")
                .and()
                .logout()
//                .logoutUrl("/logout")
                .logoutUrl("/cp/logout")
//                .logoutSuccessUrl("/login")
                .logoutSuccessUrl("/cp/login")
                .deleteCookies("JWT")
                .invalidateHttpSession(true)
        ;

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling().accessDeniedHandler(customAccessDeniedHandler());

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.cors();
    }

}
