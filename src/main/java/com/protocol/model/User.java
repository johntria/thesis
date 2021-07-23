package com.protocol.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "USER")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "username")
	private String username;

	@Column(name = "password")
	private String password;

	@Column(name = "active")
	private int active = 1;

	@ManyToOne
	private Role role;

	@ManyToOne
	private Sector sector;

	public User() {
	}

	public User(Long id, String firstName, String lastName, String username, String password, int active, Role role,
			Sector sector) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.password = password;
		this.active = 1;
		this.role = role;
		this.sector = sector;
	}

	public User(String firstName, String lastName, String username, String password, int active, Role role,
			Sector sector) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.password = password;
		this.active = active;
		this.role = role;
		this.sector = sector;
	}

	public User(User user) {
		this.id = user.id;
		this.username = user.username;
		this.firstName = user.firstName;
		this.active = user.active;
		this.password = user.password;
		this.lastName = user.lastName;
		this.role = user.role;
		this.sector = user.sector;

	}

	// Getters && Setters

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Sector getSector() {
		return sector;
	}

	public void setSector(Sector sector) {
		this.sector = sector;
	}

	public String toString() {
		return String.format(this.firstName + " " + this.lastName);
	}

}
