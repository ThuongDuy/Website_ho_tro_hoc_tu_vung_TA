package com.elearning.studyvocabulary.controller;


import java.util.HashSet;


import java.util.Set;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import com.elearning.studyvocabulary.common.MyEmailService;
import com.elearning.studyvocabulary.common.RandomString;

import com.elearning.studyvocabulary.model.entity.ERole;
import com.elearning.studyvocabulary.model.entity.Roles;

import com.elearning.studyvocabulary.model.entity.Users;
import com.elearning.studyvocabulary.model.service.RoleService;

import com.elearning.studyvocabulary.model.service.UserService;

import com.elearning.studyvocabulary.payload.request.UserRequest;


@CrossOrigin(origins = "*")
@Controller
@RequestMapping(value = "/app/")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private MyEmailService emailService;
	
	@GetMapping("/register")
	public String registerForm(Model model) {
		model.addAttribute("registrationRequest", new UserRequest());
		return "admin/pages/samples/register";
	}

	@PostMapping("/register")
	public String registerUser(@Valid UserRequest registrationRequest, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			return "admin/pages/samples/register";
		}
		if(!(registrationRequest.getPassword().equals(registrationRequest.getConfirmPassword()))) {
			bindingResult.rejectValue("confirmPassword", "registrationRequest.confirmPassword","Mật khẩu không khớp.");
			return "admin/pages/samples/register";
		}

		if (userService.existsByUsername(registrationRequest.getUsername())) {
			bindingResult.rejectValue("username", "registrationRequest.username","Tên đăng nhập đã được sử dụng");
			return "admin/pages/samples/register";
		}
		if (userService.existsByEmail(registrationRequest.getEmail())) {
			bindingResult.rejectValue("email", "registrationRequest.email","Email đã được sử dụng");
			return "admin/pages/samples/register";
		}
		Users user = new Users();
		user.setUsername(registrationRequest.getUsername());
		user.setEmail(registrationRequest.getEmail());
		user.setCreated(registrationRequest.getCreated());
		user.setUserStatus(true);
		user.setPassword(encoder.encode(registrationRequest.getPassword()));
		Set<String> strRoles = registrationRequest.getListRoles();
		Set<Roles> listRole = new HashSet<>();
		if (strRoles == null) {
			Roles userRole = roleService.findByRoleName(ERole.USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found"));
			listRole.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Roles adminRole = roleService.findByRoleName(ERole.ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found"));
					listRole.add(adminRole);
				case "user":
					Roles userRole = roleService.findByRoleName(ERole.USER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found"));
					listRole.add(userRole);
				}

			});
		}
		user.setListRoles(listRole);
		userService.save(user);
		return "redirect:/app/login";
	}
	@GetMapping("/login")
	public String loginForm(Model model) {
		return "admin/pages/samples/login";
	}
	@GetMapping("/forgotPassword")
	public String forgot() {
		return "admin/pages/samples/Forgot-password";
	}
	@PostMapping("/forgotPassword")
	public String forgotPassword(Model model, @RequestParam("email") String email) {
		if(userService.existsByEmail(email)) {
			Users user = userService.findByEmail(email);
			RandomString rand = new RandomString();
	    	String pass=rand.randomAlphaNumeric(8);
			user.setPassword(encoder.encode(pass));
			userService.save(user);
			emailService.sendEmail(user.getEmail(), "Mật khẩu mới của bạn",
			user.getUsername(),pass);
			model.addAttribute("status", 1);
		}else {
			model.addAttribute("status", 0);
		}
		return "admin/pages/samples/respond";
	}
}
