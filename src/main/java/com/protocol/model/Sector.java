package com.protocol.model;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "SECTOR")
public class Sector {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "sector_name")
	private String name;

	@OneToMany(mappedBy = "sector")
	private Collection<User> user = new ArrayList<>();

	public Sector(long id, String name, Collection<User> user) {

		this.id = id;
		this.name = name;
		this.user = user;
	}

	public Sector(String name) {
		super();
		this.name = name;
	}

	public Sector() {
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

	public Collection<User> getUser() {
		return user;
	}

	public void setUser(Collection<User> user) {
		this.user = user;
	}

}