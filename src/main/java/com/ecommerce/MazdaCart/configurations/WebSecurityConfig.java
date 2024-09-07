package com.ecommerce.MazdaCart.configurations;

import com.ecommerce.MazdaCart.security.jwt.JwtAuthEntryPoint;
import com.ecommerce.MazdaCart.security.jwt.JwtAuthTokenFilter;
import com.ecommerce.MazdaCart.security.servicesOfJwt.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.server.csrf.WebSessionServerCsrfTokenRepository;

@EnableWebSecurity
@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {

	@Autowired
	JwtAuthEntryPoint authExceptionHandler;

	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Bean
	public DaoAuthenticationProvider customAuthenticationProvider () {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(defaultPasswordEncoder());
		return authenticationProvider;

	}

	@Autowired
	JwtAuthTokenFilter authTokenFilter;

	@Bean
	public PasswordEncoder defaultPasswordEncoder () {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager (AuthenticationConfiguration authenticationConfiguration)
		throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain mySecurityFilterChain (HttpSecurity httpSecurity) throws Exception {
		httpSecurity
			.csrf(csrf -> csrf.disable())
//			.csrf(csrf->csrf.ignoringRequestMatchers("/api/auth/**","/h2-console/**").csrfTokenRepository(
//				CookieCsrfTokenRepository.withHttpOnlyFalse()).csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()))
			.exceptionHandling(excep -> excep.authenticationEntryPoint(authExceptionHandler))
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(
				requests -> requests.requestMatchers("/h2-console/**").permitAll().requestMatchers("/api/auth/**")
					            .permitAll().requestMatchers("/swagger-ui/**").permitAll()
//					            .requestMatchers("/api/admin/**").hasAuthority("ROLE_ADMIN")
//					            .requestMatchers("/api/public/**").hasAnyAuthority("ROLE_USER", "ROLE_SELLER")
					            .anyRequest().authenticated());
		httpSecurity.authenticationProvider(customAuthenticationProvider());
		httpSecurity.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
		httpSecurity.headers(header -> header.frameOptions(frames -> frames.sameOrigin()));
		return httpSecurity.build();

	}

	/**
	 * This method omits any Urls specified here from any security checks. Can be used for static pages made available
	 * to all
	 */
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer () {
		return (web -> web.ignoring().requestMatchers("/configuration/ui/**", "/swagger-ui.html"));
	}

}
