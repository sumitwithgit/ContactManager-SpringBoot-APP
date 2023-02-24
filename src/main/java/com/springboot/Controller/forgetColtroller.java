package com.springboot.Controller;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.springboot.entities.User;
import com.springboot.helper.Message;
import com.springboot.repository.UserRepository;
import com.springboot.service.EmailService;

import jakarta.servlet.http.HttpSession;

@Controller
public class forgetColtroller 
{
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
	
	@Autowired
	private EmailService emailService;
	
	
	@GetMapping("/forget")
	public String openEmailForm()
	{
		return "forget_email_from";
	}

	@PostMapping("/sendOTP")
	public String sendOTP(@RequestParam("email") String email,Model m,HttpSession session)
	{
		int otp = new Random().nextInt(10000);
		String message="OTP : "+otp;
		String subject="OTP for Email Verification ";
		
		User user = this.userRepository.getUserByUserName(email);
		
		
		if(user==null)
		{
			session.setAttribute("message", new com.springboot.helper.Message("Please Enter Valid Email. This user not Registered.", "alert-danger"));
			return "redirect:/forget";
		}
		else 
		{
			boolean f = this.emailService.sendEmail(message, subject, email);
			
			if(f) {
				session.setAttribute("otp", otp);
				m.addAttribute("email", email);
			}
			return "verify_otp";			
		}
		
	}
	

	@PostMapping("/verifyOTP")
	public String verifyOTP(@RequestParam("otp") int otp,@RequestParam("email") String email,HttpSession session,Model m)
	{
		int verifyotp = (int) session.getAttribute("otp");
		
		if(verifyotp==otp)
		{
			m.addAttribute("email", email);
			return "generateNewPassword";
		}else {
			session.setAttribute("message", new com.springboot.helper.Message("OTP is Wrong.Try Again.", "alert-danger"));
			return "redirect:/forget";
		}
		
	}
	
	
	
	@PostMapping("/savePassword")
	public String savePassword(@RequestParam("email") String email,@RequestParam("newPassword") String newpassword,HttpSession session)
	{
		User user = this.userRepository.getUserByUserName(email);
		
		user.setPassword(this.bCryptPasswordEncoder.encode(newpassword));
		
		this.userRepository.save(user);
		
		session.setAttribute("message", new Message("Your New Password is Setup. Login With New Password.", "alert-success"));
		
		return "redirect:/signin";
	}
	
	
}
