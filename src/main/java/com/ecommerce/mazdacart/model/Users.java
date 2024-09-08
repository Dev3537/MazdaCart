package com.ecommerce.mazdacart.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "userName"), @UniqueConstraint(columnNames = "emailId")})
public class Users {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;

	@NotBlank(message = "UserName cannot be blank")
	@Size(min = 5, message = "UserName cannot be letter than 5 characters")
	private String userName;

	@NotBlank(message = "E-mail cannot be blank")
	@Size(min = 5, message = "E-mail id cannot be letter than 5 characters")
	@Email
	private String emailId;

	@NotBlank(message = "Password cannot be blank")
	@Size(min = 5, message = "Password cannot be letter than 5 characters")
	private String password;


	@ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
	@JoinTable(name = "User_Roles_Join_Table", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns =
	@JoinColumn(name = "role_id"))
	private Set<Roles> roles = new HashSet<>();


	@OneToMany(mappedBy = "users", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
	private Set<Product> products = new HashSet<>();
	//orphanRemoval marks "child" entity to be removed when it's no longer referenced from the "parent" entity, e.g.
	// when you remove the child entity from the corresponding collection of the parent entity. When Seller is deleted
	//all products associated with the seller is also deleted


	@ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.EAGER)
	@JoinTable(name = "User_Address_Join_Table", joinColumns = @JoinColumn(name = "userId"), inverseJoinColumns =
	@JoinColumn(name = "addressId"))
	private List<Address> addresses = new ArrayList<>();


	@OneToOne(mappedBy = "users", cascade = {CascadeType.MERGE}, orphanRemoval = true)
	private Cart cart;

	public Users (String userName, String emailId, String password, Set<Roles> roles) {
		this.userName = userName;
		this.emailId = emailId;
		this.password = password;
		this.roles = roles;
	}

	public Users (String userName, String emailId, String password) {
		this.userName = userName;
		this.emailId = emailId;
		this.password = password;
	}
}
