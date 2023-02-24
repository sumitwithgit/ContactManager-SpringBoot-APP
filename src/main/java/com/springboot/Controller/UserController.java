package com.springboot.Controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.entities.Contact;
import com.springboot.entities.User;
import com.springboot.helper.Message;
import com.springboot.repository.ContactRepository;
import com.springboot.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ContactRepository contactRepository;

	@ModelAttribute
	public void addCommonData(Model m, Principal principal) {
		String userName = principal.getName();
//		System.out.println(userName);
		User user = this.userRepository.getUserByUserName(userName);
//		System.out.println(user);
		m.addAttribute("user", user);

	}

	@RequestMapping("/index")
	public String index(Model m, Principal principal) {
		m.addAttribute("title", "User Dashboard - Smart Contact Manager");
		return "normal/user_dashboard";
	}

	@GetMapping("/add-contact")
	public String openAddContactForm(Model m) {
		m.addAttribute("title", "User DashBoard - Add Contact");
		m.addAttribute("contact", new Contact());
		return "normal/add_contact_form";
	}

	@PostMapping("/addContact")
	public String addContact(@ModelAttribute Contact contact, @RequestParam("profilePic") MultipartFile file, Model m,
			Principal principal, HttpSession session) {
		try {

			String userName = principal.getName();

			User user = this.userRepository.getUserByUserName(userName);

			if (file.isEmpty()) {
				contact.setImage("default.png");
			} else {
				contact.setImage(file.getOriginalFilename());

				File file2 = new ClassPathResource("static/img").getFile();

				Path path = Paths.get(file2.getAbsolutePath() + File.separator + file.getOriginalFilename());

				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

				System.out.println("Image is uploaded.");
			}

			user.getContacts().add(contact);
			contact.setUser(user);

//			System.out.println(file);

			System.out.println("User : " + user);

			System.out.println("Contact : " + contact);

			this.userRepository.save(user);

			m.addAttribute("contact", new Contact());
			session.setAttribute("message", new Message("Contact Added successfully", "alert-success"));

			return "normal/add_contact_form";

		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("message", new Message("Something Went Wrong.", "alert-danger"));
			return "normal/add_contact_form";
		}
	}

	@GetMapping("/viewContacts/{page}")
	public String viewAllContact(@PathVariable("page") Integer page, Model m, Principal principal) {
		String username = principal.getName();
		User user = this.userRepository.getUserByUserName(username);

		int id = user.getId();

		Pageable pageRequest = PageRequest.of(page, 4);

		Page<Contact> list = this.contactRepository.findContactByUser(id, pageRequest);

		m.addAttribute("contact", list);
		m.addAttribute("currentPage", page);
		m.addAttribute("totalPages", list.getTotalPages());
		m.addAttribute("title", "View Contacts - Smart Contact Manager");

		return "normal/viewAllContacts";
	}

	@GetMapping("/contact/{cId}")
	public String showSingleContact(@PathVariable("cId") int cId, Model m, Principal principal, HttpSession session) {

		Optional<Contact> optional = this.contactRepository.findById(cId);
		Contact contact2 = optional.get();

		String username = principal.getName();

		User user = this.userRepository.getUserByUserName(username);

		if (user.getId() == contact2.getUser().getId()) {
			m.addAttribute("title", "User Page - Smart Contact Manager");
			Contact contact = this.contactRepository.findById(cId).get();
			m.addAttribute("contact", contact);
			return "normal/contact_details";
		} else {
			session.setAttribute("message",
					new Message("This Contact is not present in your Contact List.", "alert-danger"));
			return "redirect:/user/viewContacts/0";
		}

	}

	@GetMapping("/deleteUser/{cId}")
	public String deleteUser(@PathVariable("cId") int cId, Model m, Principal principal, HttpSession session) {

		User user = this.userRepository.getUserByUserName(principal.getName());
		
		Contact contact = this.contactRepository.findById(cId).get();
		
//		this.contactRepository.deleteByContactIdAndUserId(cId, user.getId());
		
		
		user.getContacts().remove(contact);
		
		this.userRepository.save(user);

		session.setAttribute("message", new Message("Contact Deleted Successfully.", "alert-success"));			
		
		
		
		
		return "redirect:/user/viewContacts/0";

	}
	
	
	
	@GetMapping("/updateUser/{cId}")
	public String updateFormOpen(@PathVariable("cId") int cId,Model m)
	{
		Contact contact = this.contactRepository.findById(cId).get();
		m.addAttribute("cId", cId);
		m.addAttribute("contact", contact);
		m.addAttribute("title", "Update Contact Form - Smart Contact Manager");
		return "normal/update_contact_form";
	}
	
	
	@PostMapping("/updateContact/{cId}")
	public String updateContact(@ModelAttribute Contact contact,@RequestParam("profilePic") MultipartFile file,@PathVariable("cId") Integer cId,Model m,Principal principal) throws IOException
	{
		if(file.isEmpty())
		{
			contact.setImage("default.png");
		}else
		{
			contact.setImage(file.getOriginalFilename());
			
			File file2 = new ClassPathResource("static/img").getFile();
			
			Path path = Paths.get(file2.getAbsolutePath()+File.separator+file.getOriginalFilename());
			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
		}
		
		
		
		User user = this.userRepository.getUserByUserName(principal.getName());
		
	
		contact.setUser(user);
		
		this.contactRepository.save(contact);
		
		
		
		return "redirect:/user/viewContacts/0";
	}
	
	@GetMapping("/profile")
	public String userProfile(Model m)
	{
		m.addAttribute("title", "User Profile - Smart Contact Manager");
		return "normal/profile";
	}
	
	
	@GetMapping("/settings")
	public String openSetting(Model m)
	{
		m.addAttribute("title", "Settings Page");
		return "normal/settings";
	}
	
	@PostMapping("/changePassword")
	public String changePassword(@RequestParam("oldPassword") String oldPassword,@RequestParam("newPassword") String newPassword,Principal prin,HttpSession session)
	{
		User user = this.userRepository.getUserByUserName(prin.getName());
		String password = user.getPassword();
		if(this.bCryptPasswordEncoder.matches(oldPassword, password))
		{
			user.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
			this.userRepository.save(user);
			session.setAttribute("message", new Message("Password Changed Successfully", "alert-success"));
			System.out.println("password changed");
		}else {
			session.setAttribute("message", new Message("Old Password Doesn't Match.Please try Again.", "alert-danger"));
			System.out.println("Old Password Doesn't Match.Please try Again.");
			return "redirect:/user/settings";
		}
		return "redirect:/user/index";
		
	}
	
	
	

}
