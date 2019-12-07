package com.me.security;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth.provider.OAuthProcessingFilterEntryPoint;
import org.springframework.security.oauth.provider.token.InMemoryProviderTokenServices;
import org.springframework.security.oauth.provider.token.OAuthProviderTokenServices;
import org.springframework.security.web.savedrequest.RequestCacheAwareFilter;

import com.me.security.oauth1.MyConsumerDetailsService;
import com.me.security.oauth1.MyOAuthAuthenticationHandler;
import com.me.security.oauth1.MyOAuthNonceServices;
import com.me.security.oauth1.ZeroLeggedOAuthProviderProcessingFilter;

@EnableWebSecurity
@Order(value = 20)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	private final DataSource dataSource;

	private PasswordEncoder passwordEncoder;
	private UserDetailsService userDetailsService;

	public WebSecurityConfiguration(final DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		if (passwordEncoder == null) {
			passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		}
		return passwordEncoder;
	}

	@Bean
	public UserDetailsService userDetailsService() {
		if (userDetailsService == null) {
			userDetailsService = new JdbcDaoImpl();
			((JdbcDaoImpl) userDetailsService).setDataSource(dataSource);
		}
		return userDetailsService;
	}

	 

//        @Override
//        public void configure(WebSecurity web) {
//            web.ignoring().antMatchers("/api/**");
//        }

//   //for oauth 1
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.addFilterAfter(
//            this.oauthConsumerContextFilter(),
//            SwitchUserFilter.class
//        );
//        http.addFilterAfter(
//            this.oauthConsumerProcessingFilter(),
//            OAuthConsumerContextFilter.class
//        );
//    }
//
//    
// // IMPORTANT: this must not be a Bean
//    OAuthConsumerContextFilter oauthConsumerContextFilter() {
//        OAuthConsumerContextFilter filter = new OAuthConsumerContextFilter();
//        filter.setConsumerSupport(this.consumerSupport());
//        return filter;
//    }
//
//    // IMPORTANT: this must not be a Bean
//    OAuthConsumerProcessingFilter oauthConsumerProcessingFilter() {
//        OAuthConsumerProcessingFilter filter = new OAuthConsumerProcessingFilter();
//        filter.setProtectedResourceDetailsService(this.prds());
//
//        LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> map =
//            new LinkedHashMap<>();
//
//        // one entry per oauth:url element in xml
//        map.put(
//            // 1st arg is equivalent of url:pattern in xml
//            // 2nd arg is equivalent of url:httpMethod in xml
//            new AntPathRequestMatcher("/oauth1/**", null),
//            // arg is equivalent of url:resources in xml
//            // IMPORTANT: this must match the ids in prds() and prd() below
//            Collections.singletonList(new SecurityConfig("/oauth1/**"))
//        );
//
//        filter.setObjectDefinitionSource(
//            new DefaultFilterInvocationSecurityMetadataSource(map)
//        );
//
//        return filter;
//    }
//
//    @Bean // optional, I re-use it elsewhere, hence the Bean
//    OAuthConsumerSupport consumerSupport() {
//        CoreOAuthConsumerSupport consumerSupport = new CoreOAuthConsumerSupport();
//        consumerSupport.setProtectedResourceDetailsService(prds());
//        return consumerSupport;
//    }
//
//    @Bean // optional, I re-use it elsewhere, hence the Bean
//    ProtectedResourceDetailsService prds() {
//        return (String id) -> {
//            switch (id) {
//            // this must match the id in prd() below
//            case "/oauth1/**":
//                return prd();
//            }
//            throw new RuntimeException("Invalid id: " + id);
//        };
//    }
//
//    ProtectedResourceDetails prd() {
//        BaseProtectedResourceDetails details = new BaseProtectedResourceDetails();
//
//        // this must be present and match the id in prds() and prd() above
//        details.setId("/oauth1/**");
//
//        details.setConsumerKey("OAuth_Key");
//        details.setSharedSecret(new SharedConsumerSecretImpl("OAuth_Secret"));
//
//        details.setRequestTokenURL("http://localhost:8080/oauth/request_token");
//        details.setUserAuthorizationURL("http://localhost:8080/oauth/authorize");
//        details.setAccessTokenURL("http://localhost:8080/oauth/access_token");
//
//        // any other service-specific settings
//
//        return details;
//    }

}
