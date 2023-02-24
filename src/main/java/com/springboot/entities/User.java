package com.springboot.entities;

import java.util.List;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class User 
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@NotBlank(message = "User Name Cann't be Blank.")
	@Size(min = 3,max = 20,message = "Username contains 3-20 charcater.")
	private String name;
	@Column(unique = true)
	@NotBlank(message="Please Enter Email")
	@Email(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$",message = "Please Enter Valid Format")
	private String email;
//	@Size(max = 16,min = 8,message = "Password Contains 8-16 character.")
	@NotBlank(message = "Please Enter Password")
	private String password;
	private String image;
	private String role;
	@Column(length = 2000)
	private String about;
	private boolean enabled;
	
	@Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
	@Fetch(FetchMode.SUBSELECT)
	@OneToMany(cascade = CascadeType.ALL,mappedBy = "user",orphanRemoval = true)
	private List<Contact> contacts;
	
	
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public User(int id, String name, String email, String password, String image, String role, String about,
			boolean enabled, List<Contact> contacts) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.image = image;
		this.role = role;
		this.about = about;
		this.enabled = enabled;
		this.contacts = contacts;
	}

	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getEmail() {
		return email;
	}
	public String getPassword() {
		return password;
	}
	public String getImage() {
		return image;
	}
	public String getRole() {
		return role;
	}
	public String getAbout() {
		return about;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public void setAbout(String about) {
		this.about = about;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public List<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", image=" + image
				+ ", role=" + role + ", about=" + about + ", enabled=" + enabled + "]";
	}

	
	
	
	
}
