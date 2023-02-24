package com.springboot.Controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.entities.Contact;
import com.springboot.entities.User;
import com.springboot.repository.ContactRepository;
import com.springboot.repository.UserRepository;

@RestController
public class searchController 
{
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepository;
	
	
	@GetMapping("/search/{query}")
	public ResponseEntity<?> search(@PathVariable("query") String query,Principal principal)
	{
		User user = this.userRepository.getUserByUserName(principal.getName());
		
		List<Contact> list = this.contactRepository.findByNameContainingAndUser(query, user);
		
		return ResponseEntity.ok(list);
		
	}
}
