package com.springboot.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.springboot.entities.User;
import com.springboot.helper.Message;
import com.springboot.repository.UserRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


@Controller
public class HomeController {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;;
	
	@Autowired
	public UserRepository userRepository;

	@RequestMapping(value = { "/", "/home" })
	public String home(Model m) {
		m.addAttribute("title", "Home - Smart Contact Manager");
		return "home";
	}

	@RequestMapping("/about")
	public String about(Model m) {
		m.addAttribute("title", "About - Smart Contact Manager");
		return "about";
	}

	//open register form handler
	
	@RequestMapping("/signup")
	public String signup(Model m) {
		m.addAttribute("title", "Register - Smart Contact Manager");
		m.addAttribute("user", new User());

		return "signup";
	}

	//register user form handler
	
	@PostMapping("/do_register")
	public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result,
			@RequestParam(value = "agreement", defaultValue = "false") boolean agreement, Model m,
			HttpSession session) {
		try {
			if (result.hasErrors()) {
				m.addAttribute("user", user);
				return "signup";
			}
			if (!agreement) {
				System.out.println("please check tac.");
				m.addAttribute("user", user);
				session.setAttribute("message", new Message("Please Check Terms And Conditions", "alert-danger"));
			}

			else {
				user.setImage("default.png");
				user.setRole("ROLE_User");
				user.setEnabled(true);
				user.setPassword(passwordEncoder.encode(user.getPassword()));
				
				User save = this.userRepository.save(user);
				m.addAttribute("user", new User());
				session.setAttribute("message", new Message("Successfully Registered", "alert-success"));
			}

			System.out.println(user);
			System.out.println(agreement);

		} catch (Exception e) {
			e.printStackTrace();
			m.addAttribute("user", user);
			session.setAttribute("message", new Message("This User Already Registered.Try With Another One....!!!", "alert-danger"));
		}
		return "signup";
	}
	
	
	@GetMapping("/signin")
	public String login(Model m)
	{
		m.addAttribute("title","Login - Smart Contact Manager");
		return "login";
	}
	

}
