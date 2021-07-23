package com.protocol.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.protocol.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	Role findById(long id);

	Role findByUserId(long userId);

	Role findByName(String name);
}
