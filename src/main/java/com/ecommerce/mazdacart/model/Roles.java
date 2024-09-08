package com.ecommerce.mazdacart.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Roles {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer roleId;


	@Enumerated(EnumType.STRING) // By default, enums are persisted as integers, this is persisting it as String
	private AppRole roleName;

	@JsonIgnore
	@ManyToMany(fetch = FetchType.EAGER, mappedBy = "roles")
	private Set<Users> users = new HashSet<>();

	public Roles (AppRole roleName) {
		this.roleName = roleName;
	}
}
