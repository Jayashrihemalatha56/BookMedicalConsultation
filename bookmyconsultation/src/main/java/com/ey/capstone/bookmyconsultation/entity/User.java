package com.ey.capstone.bookmyconsultation.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

//Mark it with Data, Entity, Builder, NoArgsConstructor, AllArgsConstructor
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
//create a class named User
public class User {
	@Id
	private String userId;
	//create firstName of type String
	private String firstName;
	//create lastName of type String
	private String lastName;
	//create dob of type String
	private String dob;
	//create mobile of type String
	private String mobile;
	//create primary key 'emailId' of type String
	private String emailId;
	//create password of type String
	private String password;
	//create createdDate of type String
	private String createdDate;
	//create salt of type String
	private String salt;
	//all the mentioned members must be private
}
