package com.lexisnexis.telematics.cloud.fca.journeyingest.app.security.jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class JwtScopeAuthorityMapperImpl implements JwtScopeAuthorityMapper {
	
	private Map<String, List<String>> scopeAuthorityMap = new HashMap<>();

	public void setScopeAuthorityMap(Map<String, List<String>> scopeAuthorityMap) {
		this.scopeAuthorityMap = scopeAuthorityMap;
	}

	@Override
	public Collection<? extends GrantedAuthority> mapAuthorities(String scope) {
		List<String> scopeAuthorities = scopeAuthorityMap.getOrDefault(scope, new ArrayList<String>());
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(); 
		scopeAuthorities.forEach(scopeAuthority -> authorities.add(new SimpleGrantedAuthority(scopeAuthority)));
		
		return authorities;
	}

}
