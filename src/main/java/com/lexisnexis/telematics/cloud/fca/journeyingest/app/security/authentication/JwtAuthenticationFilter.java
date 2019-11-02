package com.lexisnexis.telematics.cloud.fca.journeyingest.app.security.authentication;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import com.lexisnexis.telematics.cloud.fca.journeyingest.app.security.jwt.FCAJwt;
import com.lexisnexis.telematics.cloud.fca.journeyingest.app.security.jwt.JwtDecodeException;
import com.lexisnexis.telematics.cloud.fca.journeyingest.app.security.jwt.JwtFormatException;
import com.lexisnexis.telematics.cloud.fca.journeyingest.app.security.jwt.JwtService;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();
	
	private AuthenticationManager authenticationManager;
	
	private AuthenticationEntryPoint authenticationEntryPoint;
	
	private JwtService jwtService;
	
	public JwtAuthenticationFilter(AuthenticationManager authenticationManager,
			AuthenticationEntryPoint authenticationEntryPoint,
			JwtService jwtService) {
		this.authenticationManager = authenticationManager;
		this.authenticationEntryPoint = authenticationEntryPoint;
		this.jwtService = jwtService;
	}

	@Override
	public void afterPropertiesSet() {
		Assert.notNull(this.authenticationManager, "An AuthenticationManager is required");
		Assert.notNull(this.authenticationEntryPoint, "An AuthenticationEntryPoint is required");
		Assert.notNull(this.jwtService, "A JwtService is required");
	}
	
	protected void doFilterInternal(HttpServletRequest request,	HttpServletResponse response, FilterChain chain) throws IOException, ServletException  {
		String header = request.getHeader("Authorization");
		if (header == null 
				|| !header.startsWith("Bearer ")) {
			chain.doFilter(request, response);
			return;
		}
		try {
			String token = header.substring(7);			
			FCAJwt fcaJwt = extractJwtFromHeader(token);
			
			if (authenticationIsRequired(token)) {
				JwtAuthenticationToken authRequest = new JwtAuthenticationToken(token,												 
						fcaJwt.getUsername());   
				authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
				Authentication authResult = authenticationManager.authenticate(authRequest);
	
				SecurityContextHolder.getContext().setAuthentication(authResult);
				
				onSuccessfulAuthentication(request, response, authResult);
			}
		} 
		catch (JwtFormatException failed) {
			SecurityContextHolder.clearContext();
			
			onUnsuccessfulAuthentication(request, response, new BadCredentialsException("SecurityOauth2Jwt invalid authentication token format", failed));
			
			chain.doFilter(request, response);
			
			return;
		}
		catch (AuthenticationException failed) {
			SecurityContextHolder.clearContext();

			onUnsuccessfulAuthentication(request, response, failed);
			
			authenticationEntryPoint.commence(request, response, failed);
			
			return;
		}

		chain.doFilter(request, response);
	}
	
	private FCAJwt extractJwtFromHeader(String token) {
		try {
			return jwtService.decodeAccessToken(token);
		}		
		catch (JwtDecodeException e) {
			throw new BadCredentialsException("Failed to decode FCAJwt authentication token", e);
		}
	}
	
	private boolean authenticationIsRequired(String token) {
		Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();

		if (existingAuth == null || !existingAuth.isAuthenticated()) {
			return true;
		}
		
		if (existingAuth instanceof JwtAuthenticationToken
				&& !((JwtAuthenticationToken)existingAuth).getToken().equals(token)) {
			return true;
		}

		if (existingAuth instanceof AnonymousAuthenticationToken) {
			return true;
		}

		return false;
	}
	
	protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult) throws IOException {
	}

	protected void onUnsuccessfulAuthentication(HttpServletRequest request,	HttpServletResponse response, AuthenticationException failed) throws IOException {
	}
   
}