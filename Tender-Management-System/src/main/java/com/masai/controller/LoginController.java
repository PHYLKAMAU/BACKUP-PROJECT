package com.masai.controller;

import com.masai.model.Vendor;
import com.masai.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
public class LoginController {
	
	@Autowired
	private LoginService loginService;
	
//	@PostMapping("/login")
//	public ResponseEntity<String> LoginAccount(@RequestBody LoginDTO dto) throws LoginException{
	
	@PostMapping("/login/{username}/{password}")
	public ResponseEntity<Vendor> LoginAccount(@PathVariable String username,@PathVariable String password) throws Exception{
		Vendor vendor = loginService.LogIntoAccount(username,password);
		return new ResponseEntity<Vendor>(vendor,HttpStatus.OK);
	}
	
	@PostMapping("/logout/{key}")
	public ResponseEntity<String> LogoutAccount(@PathVariable String key) throws Exception{
		String res = loginService.LogOutFromAccount(key);
		return new ResponseEntity<String>(res,HttpStatus.OK);
	}
}
