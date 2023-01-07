package com.officemanagementsystemapi.configuration.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class CustomSecurityConfig {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final RestAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomUserDetailsService customDetailsService;

    @Autowired
    public CustomSecurityConfig(RestAuthenticationEntryPoint authenticationEntryPoint,
                                CustomUserDetailsService customDetailsService) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.customDetailsService = customDetailsService;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(customDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .antMatchers("/signin", "/signup");
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeRequests(configurer -> configurer
                    .expressionHandler(defaultWebSecurityExpressionHandler())
                    .antMatchers("/**").permitAll()
//                    .antMatchers(HttpMethod.GET, "/**").access("hasAuthority('ROLE_USER')")
                    .antMatchers(HttpMethod.GET, "/**").hasRole("USER")
//                    .antMatchers(HttpMethod.PUT, "/**").access("hasAuthority('ROLE_STAFF')")
                    .antMatchers(HttpMethod.PUT, "/**").hasRole("STAFF")
//                    .antMatchers(HttpMethod.DELETE, "/**").access("hasAuthority('ROLE_ADMIN')")
                    .antMatchers(HttpMethod.DELETE, "/**").hasRole("ADMIN")
                    .anyRequest()
                    .authenticated()
            )
            .cors(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable)
            .authenticationProvider(authenticationProvider())
            .userDetailsService(customDetailsService)
            .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> {
                httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(authenticationEntryPoint);
                httpSecurityExceptionHandlingConfigurer.accessDeniedHandler(accessDeniedHandler());
            })
            .sessionManagement(httpSecuritySessionManagementConfigurer -> {
                httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            });
        SecurityFilterChain securityFilterChain = http.build();
        securityFilterChain.getFilters().forEach(filter -> {
            logger.info("Security Filter is {}", filter);
        });
        return securityFilterChain;
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDenied();
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        String hierarchy = "ROLE_ADMIN > ROLE_STAFF \n ROLE_STAFF > ROLE_USER";
        roleHierarchy.setHierarchy(hierarchy);
        return roleHierarchy;
    }

    @Bean
    public DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler() {
        DefaultWebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy());
        return expressionHandler;
    }

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }
}
