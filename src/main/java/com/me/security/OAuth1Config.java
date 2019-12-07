package com.me.security;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth.provider.OAuthProcessingFilterEntryPoint;
import org.springframework.security.oauth.provider.token.InMemoryProviderTokenServices;
import org.springframework.security.oauth.provider.token.OAuthProviderTokenServices;
import org.springframework.security.web.savedrequest.RequestCacheAwareFilter;

import com.me.security.oauth1.MyConsumerDetailsService;
import com.me.security.oauth1.MyOAuthAuthenticationHandler;
import com.me.security.oauth1.MyOAuthNonceServices;
import com.me.security.oauth1.ZeroLeggedOAuthProviderProcessingFilter;


@Order(value = 2)
@EnableWebSecurity
public class OAuth1Config extends WebSecurityConfigurerAdapter{

	private ZeroLeggedOAuthProviderProcessingFilter zeroLeggedOAuthProviderProcessingFilter;
	@Autowired
	MyConsumerDetailsService oauthConsumerDetailsService;
	@Autowired
	MyOAuthNonceServices oauthNonceServices;
	@Autowired
	MyOAuthAuthenticationHandler oauthAuthenticationHandler;
	@Autowired
	OAuthProcessingFilterEntryPoint oauthProcessingFilterEntryPoint;
	@Autowired
	OAuthProviderTokenServices oauthProviderTokenServices;

	@PostConstruct
	public void init() {
		// NOTE: have to build the filter here:
		// http://stackoverflow.com/questions/24761194/how-do-i-stop-spring-filterregistrationbean-from-registering-my-filter-on/24762970
		zeroLeggedOAuthProviderProcessingFilter = new ZeroLeggedOAuthProviderProcessingFilter(
				oauthConsumerDetailsService, oauthNonceServices, oauthProcessingFilterEntryPoint,
				oauthAuthenticationHandler, oauthProviderTokenServices, true);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception { 

		http.antMatcher("/oauth1/**")
				// added filters must be ordered: see 
				// http://docs.spring.io/spring-security/site/docs/3.2.0.RELEASE/apidocs/org/springframework/security/config/annotation/web/HttpSecurityBuilder.html#addFilter%28javax.servlet.Filter%29
				.addFilterBefore(zeroLeggedOAuthProviderProcessingFilter, RequestCacheAwareFilter.class)
				.authorizeRequests().anyRequest().hasRole("OAUTH").and().csrf().disable(); // see above
	}

	@Bean(name = "oauthProviderTokenServices")
	public OAuthProviderTokenServices oauthProviderTokenServices() {
		// NOTE: we don't use the OAuthProviderTokenServices for 0-legged but it cannot
		// be null
		return new InMemoryProviderTokenServices();
	}

	
	
}
