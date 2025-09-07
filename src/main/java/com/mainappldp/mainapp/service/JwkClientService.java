package com.mainappldp.mainapp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

@Service
public class JwkClientService {
	
	@Value("${idp.jwks-uri}")
	private String jwkUri;
	
	private final RestTemplate restTemplate = new RestTemplate();
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	private PublicKey cachedPublicKey;
	private long lastFetchedTime = 0;
	private static final long CACHE_EXPIRATION_MILLIS = 3600 * 1000;
	
	public PublicKey getPublicKey() {
		if (cachedPublicKey == null || isCacheExpired()) {
			fetchAndCachePublicKey();
		}
		return cachedPublicKey;
	}
	
	private boolean isCacheExpired() {
		return (System.currentTimeMillis() - lastFetchedTime) > CACHE_EXPIRATION_MILLIS;
	}
	
	private void fetchAndCachePublicKey() {
		try {
			String jwksJson = restTemplate.getForObject(jwkUri, String.class);
			JsonNode jwks = objectMapper.readTree(jwksJson);
			JsonNode key = jwks.get("keys").get(0);
			
			String nStr = key.get("n").asText();
			String eStr = key.get("e").asText();
			
			BigInteger modulus = new BigInteger(1, Base64.getUrlDecoder().decode(nStr));
			BigInteger publicExponent = new BigInteger(1, Base64.getUrlDecoder().decode(eStr));
			
			RSAPublicKeySpec spec = new RSAPublicKeySpec(modulus, publicExponent);
			KeyFactory factory = KeyFactory.getInstance("RSA");
			
			cachedPublicKey = factory.generatePublic(spec);
			lastFetchedTime = System.currentTimeMillis();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalStateException("Failed to get public key from IDP", e);
		}
	}
}