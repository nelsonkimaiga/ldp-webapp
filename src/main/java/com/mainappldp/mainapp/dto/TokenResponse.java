package com.mainappldp.mainapp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenResponse {
	private String accessToken;
	private String refreshToken;

}
