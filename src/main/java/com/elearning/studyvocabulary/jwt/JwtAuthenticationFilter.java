package com.elearning.studyvocabulary.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.elearning.studyvocabulary.security.CustomUserDetailsService;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter{
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	private String getJwtFromRequest(HttpServletRequest request) {
		String bearerToken =request.getHeader("Authorization");
		//kiem tra xem header Author cos chua thong tin jwt ko
		if(StringUtils.hasText(bearerToken)&& bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String jwt=getJwtFromRequest(request);
			if(StringUtils.hasText(jwt)&&jwtTokenProvider.validateToken(jwt)) {
				//lay username tu token
				String userName =jwtTokenProvider.getUserNameFromJwt(jwt);
				//lay thong tin nguoi dung tu username
				UserDetails userDetails = customUserDetailsService.loadUserByUsername(userName);
				if(userDetails!=null) {
					//Neu nguoi dung hop le thi set thong tin cho security context
					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}
		}catch(Exception ex) {
			log.error("Fail on set user authentication", ex);
		}
		filterChain.doFilter(request, response);
	}
	

}

