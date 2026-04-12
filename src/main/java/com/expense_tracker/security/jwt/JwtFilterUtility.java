package com.expense_tracker.security.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.expense_tracker.entities.User;
import com.expense_tracker.repositories.UserRepo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.cors.CorsConfigurationSource;

@Component
public class JwtFilterUtility {

	

	@Value("${jwt.secret}")
	private String stringkey;

	private UserRepo userRepo;

	private UserDetailsService userDetailsService;

	

	@Autowired
	public void setUserRepo(UserRepo userRepo) {
		this.userRepo = userRepo;
	}

	@Autowired
	public void setUserDetailsService(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	public ResponseCookie generateToken(String userEmail,HttpServletRequest request) {

		User user = userRepo.findByEmail(userEmail).orElseThrow(()->new BadCredentialsException("Invalid email"));

		String jwtToken = Jwts.builder().claim("role", "ROLE_USER").setSubject(user.getEmail())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15))
				.signWith(this.getKey(this.stringkey), SignatureAlgorithm.HS256).compact();

		ResponseCookie accessCookie =
			    ResponseCookie.from("accessToken", jwtToken)
			        .httpOnly(true)
			        .secure(false)
			        .path(request.getContextPath()+ "/")
			         .maxAge(15 * 60)
			        .build();
        return accessCookie;


	}

	public ResponseCookie generateToken(Authentication authentication,HttpServletRequest request) {

		UserDetails user = (UserDetails) authentication.getPrincipal();

		String jwtToken = Jwts.builder()
				.setSubject(user.getUsername()).claim("role", "ROLE_USER")
				.setIssuedAt(new Date(System.currentTimeMillis()))
			    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15))
				.signWith(this.getKey(this.stringkey), SignatureAlgorithm.HS256).compact();
       
		ResponseCookie accessCookie =
			    ResponseCookie.from("accessToken", jwtToken)
			        .httpOnly(true)
			        .secure(false)
			        .path(request.getContextPath() + "/")
			        .maxAge(15 * 60)
			        .build();
        return accessCookie;
		
	}

	private Key getKey(String strignKey) {

		Key privateKey = Keys.hmacShaKeyFor(strignKey.getBytes(StandardCharsets.UTF_8));

		return privateKey;

	}

	public Claims extractAllClaims(String jwtToken) throws JwtException {

		return Jwts.parserBuilder().setSigningKey(getKey(this.stringkey)).build().parseClaimsJws(jwtToken).getBody();
	}

	public UserDetails autheticateToken(String token) {

		Claims claims;
		try {
			claims = this.extractAllClaims(token);

		} catch (JwtException e) {

			throw new BadCredentialsException("Invalid or expired JWT");
		}

		String usernamefromToken = claims.getSubject();

		return userDetailsService.loadUserByUsername(usernamefromToken);

		

		

	}
    
	public String extractAcessToken(HttpServletRequest request) {
		
		 
		if(request.getCookies()==null) {
			
			
			
			return null;
		}
		
		for(Cookie cookie:request.getCookies()) {
			 
			if(cookie.getName().equals("accessToken")) {
				
				return cookie.getValue();
			}
		}
		
		
		return null;
	}
}
