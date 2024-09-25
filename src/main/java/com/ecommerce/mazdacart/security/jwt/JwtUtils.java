package com.ecommerce.mazdacart.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtUtils {

	@Value("${spring.jwt.secret}")
	private String jwtSecret;

	@Value("${spring.jwt.expirations.tms}")
	private long jwtExpirationTms;


	private Key getKey () {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
	}

	public boolean validateJwt (String authToken) {
		try {
			Jwts.parser().verifyWith((SecretKey) getKey()).build().parseSignedClaims(authToken);
			return true;
		} catch (MalformedJwtException e) {
			log.error("Invalid Jwt Exception:{}", e.getMessage());
		} catch (IllegalArgumentException e) {
			log.error("Jwt String is empty:{}", e.getMessage());
		} catch (ExpiredJwtException e) {
			log.error("Jwt token has expired:{}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			log.error("Jwt token is an unsupported format:{}", e.getMessage());
		}

		return false;
	}


	public String getJwtFromHeader (HttpServletRequest httpServletRequest) {
		String bearerToken = httpServletRequest.getHeader("Authorization");
		log.debug("Authorization header:{}", bearerToken);
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}


	public String getJwtForUserName (UserDetails userDetails) {
		String userName = userDetails.getUsername();
		return Jwts.builder().subject(userName).issuedAt(new Date())
			       .expiration(new Date(new Date().getTime() + jwtExpirationTms)).signWith(getKey()).compact();
	}


	public String getUserNameFromJwt (String token) {
		return Jwts.parser().verifyWith((SecretKey) getKey()).build().parseSignedClaims(token).getPayload()
			       .getSubject();
	}

}
