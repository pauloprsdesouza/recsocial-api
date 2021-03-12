package br.com.api.authorization;

import br.com.api.authorization.userdetails.UserServiceImpl;
import br.com.api.infrastructure.filters.JwtAuthenticationFilter;
import br.com.api.infrastructure.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

@EnableWebSecurity
public class SpringSecurityWebAppConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserServiceImpl _userService;

    @Autowired
    private JwtService _jwtService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public OncePerRequestFilter jwtFilter() {
        return new JwtAuthenticationFilter(_jwtService, _userService);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(_userService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests().antMatchers("/domains/**")
                .hasRole("ADMIN").and().authorizeRequests().antMatchers("/entities/**")
                .hasRole("ADMIN").and().authorizeRequests().antMatchers("/evaluations/**")
                .hasRole("ADMIN").and().authorizeRequests().antMatchers("/recommendations/**")
                .hasRole("ADMIN").and().authorizeRequests().antMatchers("/users/view-instructions")
                .hasRole("ADMIN").and().authorizeRequests().antMatchers("/users/logout")
                .hasRole("ADMIN").and().authorizeRequests().antMatchers("/users/details")
                .hasRole("ADMIN").and().authorizeRequests().antMatchers("/users/login").permitAll()
                .and().authorizeRequests().antMatchers("/evaluations/**").permitAll().and()
                .authorizeRequests().antMatchers("/twitter/**").permitAll().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs", "/swagger-resources/**", "/webjars/**",
                "/swagger-ui/**");
    }
}
