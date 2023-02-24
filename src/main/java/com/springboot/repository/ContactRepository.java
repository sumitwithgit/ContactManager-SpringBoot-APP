package com.springboot.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.springboot.entities.Contact;
import com.springboot.entities.User;

public interface ContactRepository extends JpaRepository<Contact, Integer> 
{
	@Query("select c from Contact c where c.user.id=:id")
	public Page<Contact> findContactByUser(@Param("id") int id,Pageable pageable);
	
	@Query(value="delete from Contact c where c.cId=:contactId and c.user.id=:userId")
	public void deleteByContactIdAndUserId(@Param("contactId") int contactId,@Param("userId") int userId);

	public List<Contact> findByNameContainingAndUser(String name,User user);

}


