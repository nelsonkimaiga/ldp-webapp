package com.mainappldp.mainapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
	@GetMapping("/dashboard")
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	public ResponseEntity<String> getUserDashboard(@AuthenticationPrincipal String email) {
		return ResponseEntity.status(HttpStatus.OK).body("Welcome, " + email+
				"! You have successfully accessed a general user resource.");
	}
}