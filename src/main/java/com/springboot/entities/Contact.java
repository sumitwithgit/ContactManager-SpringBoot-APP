package com.springboot.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Contact
{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int cId;
	private String name;
	private String nickName;
	private String work;
	private String email;
	private String image;
	@Column(length = 2000)
	private String description;
	private String phone;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JsonIgnore
	private User user;

	public Contact() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Contact(int cId, String name, String nickName, String work, String email, String image, String description,
			String phone, User user) {
		super();
		this.cId = cId;
		this.name = name;
		this.nickName = nickName;
		this.work = work;
		this.email = email;
		this.image = image;
		this.description = description;
		this.phone = phone;
		this.user = user;
	}

	public int getcId() {
		return cId;
	}

	public String getName() {
		return name;
	}

	public String getNickName() {
		return nickName;
	}

	public String getWork() {
		return work;
	}

	public String getEmail() {
		return email;
	}

	public String getImage() {
		return image;
	}

	public String getDescription() {
		return description;
	}

	public String getPhone() {
		return phone;
	}

	public User getUser() {
		return user;
	}

	public void setcId(int cId) {
		this.cId = cId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public void setWork(String work) {
		this.work = work;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Contact [cId=" + cId + ", name=" + name + ", nickName=" + nickName + ", work=" + work + ", email="
				+ email + ", image=" + image + ", description=" + description + ", phone=" + phone + ", user=" + user
				+ "]";
	}

	@Override
	public boolean equals(Object obj) {
		return this.cId==((Contact)obj).getcId();
	}
	
	
	
	
}
