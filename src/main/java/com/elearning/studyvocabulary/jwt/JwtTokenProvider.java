package com.elearning.studyvocabulary.jwt;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.elearning.studyvocabulary.security.CustomUserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtTokenProvider {
	@Value("${jwt.secret.key}")
	private String JWT_SECRET;
//	private final String JWT_SECRET = "qưertyuiopasdfgh";
//	private final long JWT_EXPIRATION = 86400000L;
	@Value(("${jwt.expiration}"))
	private Long JWT_EXPIRATION;
	
	//tao jwt tu thong tin user
	public String generateToken(CustomUserDetails customUserDetails) {
		Date now=new Date();
		Date dateExp = new Date(now.getTime()+JWT_EXPIRATION);//thoi gian het han
		System.out.println("bbbb");
		// tao chuoi jwt tu username
//		return Jwts.builder().setSubject(customUserDetails.getUsername())
//				.setIssuedAt(now)
//				.setExpiration(dateExp)
//				.signWith(SignatureAlgorithm.HS512,JWT_SECRET).compact();
		return Jwts.builder()
                .setSubject(customUserDetails.getUsername())
                .setIssuedAt(now)
                .setExpiration(dateExp)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
		
	}
	//Lay thong tin user tu jwt
	public String getUserNameFromJwt(String token) {
		Claims claims  = Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody();
		return claims.getSubject();
	}
	
	public boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token);
			return true;
		}
		catch (SecurityException | MalformedJwtException e) {
//			System.out.println("Invalid JWT signature. \n" + e.getMessage() + "");
			log.error("Invalid JWT Token");
		}
		catch (ExpiredJwtException e) {
//			System.out.println("Expired JWT token. \n" + e.getMessage() + "");
			log.error("Expired JWT token");
		}
		catch (UnsupportedJwtException e) {
//			System.out.println("Unsupported JWT token. \n" + e.getMessage() + "");
			log.error("Unsupported JWT token");
		}
		catch (IllegalArgumentException e) {
//			System.out.println("JWT claims string is empty. \n" + e.getMessage() + "");
			log.error("JWT claims string is empty");
		}
		return false;
	}
	
}

