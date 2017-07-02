package com.tz.basis.config;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.session.SessionManagementFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.tz.basis.security.SUnauthorizedEntryPoint;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ComponentScan(basePackages = { "com.tz.basis.config" })
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

	public SecurityConfig() {
		super();
		logger.info("loading SecurityConfig ................................................ ");
	}

	@Autowired
	DataSource dataSource;

	@Autowired
	Environment env;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private SUnauthorizedEntryPoint restAuthenticationEntryPoint;

	@Autowired
	private AccessDeniedHandler restAccessDeniedHandler;

	@Autowired
	private AuthenticationSuccessHandler restAuthenticationSuccessHandler;

	@Autowired
	private AuthenticationFailureHandler restAuthenticationFailureHandler;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(11);
	}

	@Bean
	SimpleCorsFilter corsFilter() {
		SimpleCorsFilter filter = new SimpleCorsFilter();
		return filter;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		String[] patterns = null;

		if (env.getProperty("uiType").equalsIgnoreCase("postman")) {
			patterns = new String[] { "/**" };
		} else {
			patterns = new String[] { "/swagger-ui.html", "/v2/api-docs", "/webjars/springfox-swagger-ui/**",
					"/swagger-resources/**", "/index.html", "/tzUI/**", "/changeLocale", "/logout",
					"/authenticate", "/resources/**", "/", "/login_process/user/insert",
					"/login_process/**", "/favicon.ico", 
					"/bower_components/**"};
		}

		//For Korean input, SpringSecurity 사용시. CsrfFilter 앞에 CharacterEncodingFilter를 놓아야 한다.
		CharacterEncodingFilter filter = new CharacterEncodingFilter();
		filter.setEncoding("UTF-8");
		filter.setForceEncoding(true);
		http.addFilterBefore(filter, CsrfFilter.class);

		logger.debug("==== uiType:" + env.getProperty("uiType"));

		//logout setting
		//http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());

		if (env.getProperty("uiType").equalsIgnoreCase("postman")) { //local env
			http.csrf().disable().authorizeRequests().antMatchers("/**").permitAll();
		} else { // server env (uiType = angular)
			http.authorizeRequests().antMatchers(patterns).permitAll().antMatchers("/**").hasAnyAuthority("ROLE_USER");

			http.addFilterBefore(corsFilter(), SessionManagementFilter.class);
			http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED).sessionFixation()
					.migrateSession();

			http.csrf().disable().authenticationProvider(authenticationProvider()).exceptionHandling()
					.authenticationEntryPoint(restAuthenticationEntryPoint).accessDeniedHandler(restAccessDeniedHandler);

			http.formLogin().permitAll().loginProcessingUrl("/authenticate").successHandler(restAuthenticationSuccessHandler)
					.failureHandler(restAuthenticationFailureHandler).failureUrl("/login?error=loginFail")
					.usernameParameter("username").passwordParameter("password").permitAll();
		}

		http.rememberMe().userDetailsService(userDetailsService).rememberMeParameter("remember_me")
				.tokenRepository(persistentTokenRepository()).tokenValiditySeconds(259200);// 3 days

		http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))
				.invalidateHttpSession(false).deleteCookies("JSESSIONID").permitAll().and().sessionManagement()
				.invalidSessionUrl("/");
	}

	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		JdbcTokenRepositoryImpl db = new JdbcTokenRepositoryImpl();
		db.setDataSource(dataSource);
		return db;
	}

	@Bean
	public HttpSessionEventPublisher httpSessionEventPublisher() {
		return new HttpSessionEventPublisher();
	}

	//	@Bean
	//	public FilterRegistrationBean corsFilter() {
	//		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	//		CorsConfiguration config = new CorsConfiguration();
	//		config.setAllowCredentials(true);
	//		config.addAllowedOrigin("*");
	//		config.addAllowedHeader("*");
	//		config.addAllowedMethod("*");
	//		source.registerCorsConfiguration("/**", config);
	//		FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
	//		bean.setOrder(0);
	//		return bean;
	//	}	

}
