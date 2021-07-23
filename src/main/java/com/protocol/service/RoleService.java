package com.protocol.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.protocol.model.Role;
import com.protocol.repository.RoleRepository;

@Service
public class RoleService {

	@Autowired
	private RoleRepository roleRepository;

	public List<Role> getRoles() {
		return (List<Role>) roleRepository.findAll();

	}

	public void addRole(Role role) {
		roleRepository.save(role);
	}

	public void updateRole(Role role) {
		roleRepository.save(role);
	}

	public void deleteRoleById(Long id) {
		roleRepository.deleteById(id);
	}

	public Role findByName(String name) {
		return roleRepository.findByName(name);

	}

}
