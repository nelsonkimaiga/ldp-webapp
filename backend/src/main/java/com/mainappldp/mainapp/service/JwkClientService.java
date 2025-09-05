package com.mainappldp.mainapp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mainappldp.mainapp.model.JwkKeys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Map;

@Service
public class JwkClientService {
	
	private final RestTemplate restTemplate;
	
	@Value("${idp.jwks-uri}")
	private String jwksUri;
	
	private volatile PublicKey publicKey = null;
	
	public JwkClientService() {
		this.restTemplate = new RestTemplate();
	}
	
	@PostConstruct
	@Scheduled(fixedRate = 3600000)
	public void fetchJwk() {
		try {
			String jwksResponse = restTemplate.getForObject(jwksUri, String.class);
			ObjectMapper mapper = new ObjectMapper();
			JwkKeys jwkKeys = mapper.readValue(jwksResponse, JwkKeys.class);
			
			if (jwkKeys != null && !jwkKeys.getKeys().isEmpty()) {
				Map<String, Object> firstKey = jwkKeys.getKeys().get(0);
				String modulusB64 = (String) firstKey.get("n");
				String exponentB64 = (String) firstKey.get("e");
				
				BigInteger modulus = new BigInteger(1, Base64.getUrlDecoder().decode(modulusB64));
				BigInteger exponent = new BigInteger(1, Base64.getUrlDecoder().decode(exponentB64));
				
				RSAPublicKeySpec spec = new RSAPublicKeySpec(modulus, exponent);
				KeyFactory factory = KeyFactory.getInstance("RSA");
				this.publicKey = factory.generatePublic(spec);
			}
		}
		catch (Exception e) {
			System.err.println("Error fetching JWKS: " + e.getMessage());
		}
	}
	
	public PublicKey getPublicKey() {
		if (this.publicKey == null) {
			throw new IllegalStateException("Public key not available.");
		}
		return this.publicKey;
	}
}