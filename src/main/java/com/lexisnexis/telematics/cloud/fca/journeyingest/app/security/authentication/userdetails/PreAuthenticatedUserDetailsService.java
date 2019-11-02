package com.lexisnexis.telematics.cloud.fca.journeyingest.app.security.authentication.userdetails;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.Assert;

import com.lexisnexis.telematics.cloud.fca.journeyingest.app.security.jwt.JwtScopeAuthorityMapper;

public class PreAuthenticatedUserDetailsService implements UserDetailsService, InitializingBean {

    protected static final Logger LOGGER = LoggerFactory.getLogger(PreAuthenticatedUserDetailsService.class);

	private JwtScopeAuthorityMapper jwtScopeAuthorityMapper;
	
	public void setJwtScopeAuthorityMapper(JwtScopeAuthorityMapper jwtScopeAuthorityMapper) {
		this.jwtScopeAuthorityMapper = jwtScopeAuthorityMapper;
	}

	@Override
    public void afterPropertiesSet() throws Exception {
    	Assert.notNull(this.jwtScopeAuthorityMapper, "jwtScopeAuthorityMapper");
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
    	return new User(username, "N/A", true, true, true, true, jwtScopeAuthorityMapper.mapAuthorities(username));
    }

}
