package com.me.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.me.controller.model.UserProfile;

@RestController
public class UserController {

	@RequestMapping("/api/profile")
	public ResponseEntity<?> profile() {
		
		if(true) {
			return ResponseEntity.ok(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		}
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String email = user.getUsername() + "@mailinator.com";
		UserProfile profile = new UserProfile();
		profile.setName(user.getUsername());
		profile.setEmail(email);
		return ResponseEntity.ok(profile);
	}

}
