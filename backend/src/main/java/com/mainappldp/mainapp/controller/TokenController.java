package com.mainappldp.mainapp.controller;

import com.mainappldp.mainapp.dto.RefreshTokenRequest;
import com.mainappldp.mainapp.dto.TokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/v1")
public class TokenController {
	
	@Value("${idp.refresh-token-uri}")
	private String idpRefreshTokenUri;
	
	private final RestTemplate restTemplate = new RestTemplate();
	
	@PostMapping("/refresh")
	public ResponseEntity<TokenResponse> refreshToken(@RequestBody RefreshTokenRequest refreshToken) {
		ResponseEntity<TokenResponse> response = restTemplate.postForEntity(
				idpRefreshTokenUri,
				refreshToken,
				TokenResponse.class
		);
		return ResponseEntity.status(HttpStatus.OK).body(response.getBody());
	}

}
