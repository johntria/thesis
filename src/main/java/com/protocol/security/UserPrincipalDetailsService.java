package com.protocol.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.protocol.model.Role;
import com.protocol.model.User;
import com.protocol.repository.RoleRepository;
import com.protocol.repository.UserRepository;

public class UserPrincipalDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = userRepository.getUserByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("Username Not Found");

		}

		Role role = roleRepository.findByUserId(user.getId());
		UserPrincipal userPrincipal = new UserPrincipal(user, role);

		return userPrincipal;

	}

}
