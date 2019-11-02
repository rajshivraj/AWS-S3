package com.lexisnexis.telematics.cloud.fca.journeyingest.app.security.jwt;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

public interface JwtScopeAuthorityMapper {

	Collection<? extends GrantedAuthority> mapAuthorities(String scope);
	
}
