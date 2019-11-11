package cn.luxu.bootstrap.config;

import cn.luxu.utils.PermitAllUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

import javax.servlet.http.HttpServletResponse;

/**
 * 资源服务配置
 * 
 */
@EnableResourceServer
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
	@Autowired
	private DiscoveryClient discoveryClient;
	@Value("${serviceId}")
	private String serviceId;
	@Value("${security.oauth2.client.client-id}")
	private String clientId;
	@Value("${security.oauth2.client.client-secret}")
	private String clientSecret;

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http
			.csrf()
			.disable()
			.exceptionHandling()
			.authenticationEntryPoint(
					(request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
			.and()
			.authorizeRequests()
			.antMatchers(PermitAllUrl.permitAllUrl("/users-anon/**"))
			.permitAll() // 放开权限的url
			.anyRequest()
			.authenticated()
			.and()
			.httpBasic();
	}

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.tokenServices(customTokenServices());
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Primary
	@Bean
	public CustomTokenServices customTokenServices() {
		CustomTokenServices customTokenServices = new CustomTokenServices();
		customTokenServices.setClientId(clientId);
		customTokenServices.setClientSecret(clientSecret);
		customTokenServices.setDiscoveryClient(discoveryClient);
		customTokenServices.setServiceId(serviceId);
		return customTokenServices;
	}

}
