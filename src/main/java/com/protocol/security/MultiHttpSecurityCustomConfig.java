package com.protocol.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class MultiHttpSecurityCustomConfig {

	@Configuration
	public static class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
		@Autowired
		private SecurityHandler sucessHandler;

		@Override
		protected void configure(HttpSecurity http) throws Exception {

			http.csrf().disable().authorizeRequests()

					.antMatchers("/GODUSER/**").hasRole("GODUSER").antMatchers("/SUPERUSER/**").hasRole("SUPERUSER")
					.antMatchers("/USER/**").hasRole("USER").and().formLogin().successHandler(sucessHandler);

		}

		@Override
		@Bean
		public UserDetailsService userDetailsService() {
			return new UserPrincipalDetailsService();
		}

		@Bean
		DaoAuthenticationProvider authenticationProvider() {
			DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
			daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
			daoAuthenticationProvider.setUserDetailsService(userDetailsService());

			return daoAuthenticationProvider;
		}

		@Bean
		PasswordEncoder passwordEncoder() {
			return new BCryptPasswordEncoder();
		}

	}

	@Configuration
	@Order(1)
	public static class ApiSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
		protected void configure(HttpSecurity http) throws Exception {
			http.csrf().disable().antMatcher("/api/**").authorizeRequests().anyRequest()
					.hasAnyRole("GODUSER", "SUPERUSER", "USER").and().httpBasic();

		}
	}

}
