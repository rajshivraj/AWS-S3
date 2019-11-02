package com.lexisnexis.telematics.cloud.fca.journeyingest.app.security.authentication;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String token;
	
	private Object principal;	

	public String getToken() {
		return token;
	}
	
	@Override
	public Object getCredentials() {		
		return "none";
	}

	@Override
	public Object getPrincipal() {
		return principal;
	}

	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		if (isAuthenticated) {
			throw new IllegalArgumentException("Cannot set property to true - pass GrantedAuthority list to constructor instead");
		}

		super.setAuthenticated(false);
	}
	
	public JwtAuthenticationToken(String token, Object principal) {
		super(AuthorityUtils.NO_AUTHORITIES);
		this.token = token;
		this.principal = principal;
	}
	
	public JwtAuthenticationToken(String token, Object principal, Collection<? extends GrantedAuthority> anAuthorities) {
		super(anAuthorities);
		this.token = token;
		this.principal = principal;
		super.setAuthenticated(true);
	}
	
}