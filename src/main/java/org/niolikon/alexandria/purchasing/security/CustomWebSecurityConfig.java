package org.niolikon.alexandria.purchasing.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class CustomWebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    
    @Autowired
    JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    
    private static final String[] SWAGGER_WHITELIST = {
            // -- Swagger UI v2
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            // -- Swagger UI v3 (OpenAPI)
            "/v3/api-docs/**",
            "/swagger-ui/**"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
    		.and()
    		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    		.and()
            .authorizeRequests()

            .antMatchers("/cart/**").hasAnyAuthority("ROLE_USER")
            .antMatchers("/cart").hasAnyAuthority("ROLE_USER")
            
            .antMatchers(HttpMethod.POST, "/orders/**").hasAuthority("ROLE_USER")
            .antMatchers(HttpMethod.POST, "/orders").hasAuthority("ROLE_USER")
            .antMatchers(HttpMethod.GET, "/orders/**").hasAnyAuthority("ROLE_USER", "ROLE_OPERATOR", "ROLE_ADMIN")
            .antMatchers(HttpMethod.GET, "/orders").hasAnyAuthority("ROLE_USER", "ROLE_OPERATOR", "ROLE_ADMIN")
            .antMatchers(HttpMethod.PUT, "/orders/**").hasAnyAuthority("ROLE_OPERATOR", "ROLE_ADMIN")
            .antMatchers(HttpMethod.PUT, "/orders").hasAnyAuthority("ROLE_OPERATOR", "ROLE_ADMIN")
            .antMatchers(HttpMethod.DELETE, "/orders/**").hasAnyAuthority("ROLE_OPERATOR", "ROLE_ADMIN")
            .antMatchers(HttpMethod.DELETE, "/orders").hasAnyAuthority("ROLE_OPERATOR", "ROLE_ADMIN")
            
            
            .anyRequest().authenticated();
        

        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }
    
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
            .antMatchers(SWAGGER_WHITELIST)

            .antMatchers(HttpMethod.OPTIONS, "/**")
            .antMatchers(HttpMethod.GET, "/h2-console/**")
            .antMatchers(HttpMethod.POST, "/h2-console/**");
    }
}
