package com.protocol.model;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "ROLE")
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "role_name")
	private String name;

	@OneToMany(mappedBy = "role")
	private Collection<User> user;

	public Role() {
	}

	public Role(String name) {
		super();
		this.name = name;
	}

	public Role(long id, String name, Collection<User> user) {
		super();
		this.id = id;
		this.name = name;
		this.user = user;
	}

	/* Getters & Setters */

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
