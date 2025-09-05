package com.mainappldp.mainapp.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mainappldp.mainapp.service.JwkClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;

@Service
public class JwtUtil {
	
	private final JwkClientService jwkClientService;
	
	@Autowired
	public JwtUtil(JwkClientService jwkClientService) {
		this.jwkClientService = jwkClientService;
	}
	
	public String getUserId(String token) {
		return decodeToken(token).getClaim("userId").asString();
	}
	
	public String getEmail(String token) {
		return decodeToken(token).getSubject();
	}
	
	public String getRole(String token) {
		return decodeToken(token).getClaim("role").asString();
	}
	
	public boolean isTokenExpired(String token) {
		return decodeToken(token).getExpiresAt().before(new Date());
	}
	
	private DecodedJWT decodeToken(String token) {
		PublicKey publicKey = jwkClientService.getPublicKey();
		Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) publicKey, null);
		try {
			return JWT.require(algorithm).build().verify(token);
		} catch (JWTVerificationException e) {
			throw new IllegalStateException("Token verification failed: " + e.getMessage(), e);
		}
	}
}
