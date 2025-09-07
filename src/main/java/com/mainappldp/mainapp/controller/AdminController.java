package com.mainappldp.mainapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
	@GetMapping("/dashboard")
	public ResponseEntity<String> getAdminDashboard(@AuthenticationPrincipal String email) {
		return ResponseEntity.status(HttpStatus.OK).body("Welcome, " + email+
				"! You have successfully accessed the ADMIN dashboard.");
	}
}