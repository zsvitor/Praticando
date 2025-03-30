package com.futstore.futstore.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	@Autowired
	private UsuarioDetailsService usuarioDetailsService;

	@Autowired
	private ClienteDetailsService clienteDetailsService;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider adminAuthenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(usuarioDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Bean
	public DaoAuthenticationProvider clienteAuthenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(clienteDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(authorize -> authorize
				.requestMatchers("/", "/home", "/css/**", "/js/**", "/bootstrap-5.1.3-dist/**", "/jquery-3.6.0-dist/**",
						"/fragments/**", "/uploads/**", "/imagens/**", "/produto/detalhe/**", "/carrinho/**",
						"/carrinho", "/carrinho/adicionar", "/carrinho/atualizar", "/carrinho/remover/**",
						"/cliente/cadastro", "/cliente/buscar-cep/**")
				.permitAll()
				.requestMatchers("/administrativo/login", "/usuario/novo", "/usuario/salvar", "/acesso-negado",
						"/cliente/login", "/cliente/cadastro" ,"/cliente/login-success")
				.permitAll().requestMatchers("/auth/administrador/**").hasRole("ADMINISTRADOR")
				.requestMatchers("/auth/estoquista/**").hasRole("ESTOQUISTA").requestMatchers("/estoque/**")
				.hasAnyRole("ESTOQUISTA", "ADMINISTRADOR").requestMatchers("/auth/cliente/**").hasRole("CLIENTE")
				.anyRequest().authenticated())
				.formLogin(form -> form.loginPage("/administrativo/login").loginProcessingUrl("/administrativo/login")
						.defaultSuccessUrl("/administrativo/login-success", true)
						.failureUrl("/administrativo/login?error=true").permitAll())
				.formLogin(form -> form.loginPage("/cliente/login").loginProcessingUrl("/cliente/login")
						.defaultSuccessUrl("/cliente/login-success", true).failureUrl("/cliente/login?error=true")
						.permitAll())
				.logout(logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
						.logoutSuccessUrl("/").permitAll())
				.exceptionHandling(ex -> ex.accessDeniedPage("/acesso-negado"));
		return http.build();
	}

}