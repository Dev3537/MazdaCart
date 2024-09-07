package com.ecommerce.MazdaCart.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

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
			logger.error("Invalid Jwt Exception:{}", e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("Jwt String is empty:{}", e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.error("Jwt token has expired:{}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.error("Jwt token is an unsupported format:{}", e.getMessage());
		}

		return false;
	}


	public String getJwtFromHeader (HttpServletRequest httpServletRequest) {
		String bearerToken = httpServletRequest.getHeader("Authorization");
		logger.debug("Authorization header:{}", bearerToken);
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
