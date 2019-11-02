package com.lexisnexis.telematics.cloud.fca.journeyingest.app.security.authentication;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.cache.NullUserCache;
import org.springframework.util.Assert;

public class JwtAuthenticationProvider implements AuthenticationProvider, InitializingBean {

	private UserCache userCache = new NullUserCache();
	
	private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();
	
	private UserDetailsService userDetailsService;

	public void setUserCache(UserCache userCache) {
		this.userCache = userCache;
	}

	public void setAuthoritiesMapper(GrantedAuthoritiesMapper authoritiesMapper) {
		this.authoritiesMapper = authoritiesMapper;
	}

	public void setUserDetailsService(UserDetailsService userDetailsServiceResolver) {
		this.userDetailsService = userDetailsServiceResolver;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.userCache, "A UserCache must be set");
		Assert.notNull(this.userDetailsService, "A UserDetailsService must be set");
	}
	
	@Override
	public boolean supports(Class<?> authentication) {
		return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
	}
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		Assert.isInstanceOf(JwtAuthenticationToken.class, authentication, "Only JwtAuthenticationToken is supported");

		JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;		
		String username = jwtAuthenticationToken.getName();
						
		boolean cacheWasUsed = true;
		UserDetails user = this.userCache.getUserFromCache(username);

		if (user == null) {
			cacheWasUsed = false;
			try {
				user = retrieveUser(username);
			}
			catch (UsernameNotFoundException notFound) {
				//HideUserNotFoundExceptions) {
				throw new BadCredentialsException("Bad credentials");
			}
	
			Assert.notNull(user, "retrieveUser returned null - a violation of the interface contract");
		}
		
		if (!cacheWasUsed) {
			this.userCache.putUserInCache(user);
		}
		
		return createSuccessAuthentication(user, jwtAuthenticationToken, user);

	}
		
	protected UserDetails retrieveUser(String username) {
		UserDetails loadedUser;		
		
		try {				
			//Load UserDetails from UserDetailsService.
			loadedUser = userDetailsService
						.loadUserByUsername(username);				
		}		
		catch (Exception exception) {
			throw new InternalAuthenticationServiceException("Exception received while loading UserDetails from JwtAuthenticationToken", exception);
		}

		if (loadedUser == null) {
			throw new InternalAuthenticationServiceException("UserCredentialsService returned null, which is an interface contract violation");
		}
		return loadedUser;
	}
	
	protected Authentication createSuccessAuthentication(Object principal, JwtAuthenticationToken authentication, UserDetails user) {
		
		JwtAuthenticationToken result = new JwtAuthenticationToken(	
				authentication.getToken(),
				principal,
				authoritiesMapper.mapAuthorities(user.getAuthorities()));
		result.setDetails(authentication.getDetails());

		return result;
	}
}
