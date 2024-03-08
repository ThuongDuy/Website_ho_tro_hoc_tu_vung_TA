package com.elearning.studyvocabulary.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


//@Configuration
//@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Bean
	public UserDetailsService userDetailsService() {
		return new CustomUserDetailsService();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
	    return authenticationConfiguration.getAuthenticationManager();
	}
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors()
			.and().csrf().disable()
			.authorizeRequests().antMatchers("/").hasAnyAuthority("USER", "CREATOR", "EDITOR", "ADMIN")
			.antMatchers("/manage/**").hasAuthority("ADMIN")
			.antMatchers("/app/**").permitAll()
			.antMatchers("/test/**").permitAll().antMatchers("/images/**").permitAll()
			.antMatchers("/static-admin/**").permitAll().antMatchers("/static-user/**").permitAll().anyRequest()
			.authenticated().and()
			.formLogin().permitAll()
			.loginProcessingUrl("/signin")
	        .loginPage("/app/login").permitAll()
	        .defaultSuccessUrl("/app/")
	        .failureUrl("/app/login")
	        .usernameParameter("username")
	        .passwordParameter("password")
			.and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
			.logoutSuccessUrl("/app/");
//			.and().exceptionHandling()
//			.accessDeniedPage("/app/403");
	}
	@Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/resources/**");
    }
}
