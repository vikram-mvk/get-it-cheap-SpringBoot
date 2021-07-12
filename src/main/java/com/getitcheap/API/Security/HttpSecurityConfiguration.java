package com.getitcheap.API.Security;

import com.getitcheap.API.Items.ItemRoutes;
import com.getitcheap.API.Users.UserRoutes;
import com.getitcheap.API.Users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@EnableWebSecurity
@Configuration
@Order(1)
public class HttpSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    UserService userService;


    @Override // Sign In Logic
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userService)
                .passwordEncoder(getPasswordEncoder());
    }

    @Override     //HttpSecurity is used for Authorization
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        String unauthorizedUrls[] = new String[] {
                "/", "/unauthorized",
                UserRoutes.SIGNIN,
                UserRoutes.SIGNUP,
                ItemRoutes.GET_ITEMS,
                ItemRoutes.GET_ITEM,
                ItemRoutes.SEARCH_ITEMS
        };

        httpSecurity.
                cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers(unauthorizedUrls).permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint())
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(new RequestFilter(new JwtTokenService()), UsernamePasswordAuthenticationFilter.class);

    }

    // Required Beans
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint(){
        return new GetItCheapAuthenticationEntryPoint();
    }

    @Bean
    public BCryptPasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("X-Requested-With", "Origin", "Content-Type", "Accept", "Authorization"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
