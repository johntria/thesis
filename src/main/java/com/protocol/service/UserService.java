package com.protocol.service;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.protocol.model.Protocol;
import com.protocol.model.Role;
import com.protocol.model.Sector;
import com.protocol.model.User;
import com.protocol.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProtocolService protocolService;

	public List<User> getUsers() {
		return userRepository.findAll();
	}

	public void addUser(User user) {
		userRepository.save(user);
	}

	public void updateUser(User user) {
		userRepository.save(user);
	}

	public void updateUserByVariables(long id, String firstName, String lastName, String username, int active,
			Role role, Sector sector) {

		User tmp_user = userRepository.getOne(id);
		tmp_user.setFirstName(firstName);
		tmp_user.setLastName(lastName);
		tmp_user.setUsername(username);
		tmp_user.setActive(active);
		tmp_user.setRole(role);
		tmp_user.setSector(sector);

		updateUser(tmp_user);

	}

	public void deleteUserById(Long id) {
		List<Protocol> protocolsOfUser = protocolService.getProtocolBycreatedFromUser(this.getUserById(id));
		for (Protocol tmp_p : protocolsOfUser) {
			protocolService.deleteProtocolById(tmp_p.getId());
		}

		userRepository.deleteById(id);

	}

	public Long getLatestId() {
		List<User> user = userRepository.findAll();
		long tmp_id = 1;
		if (user != null && !user.isEmpty()) {
			for (User tmp_user : user) {
				if (tmp_user.getId() > tmp_id)
					tmp_id = tmp_user.getId();
			}
			tmp_id++;
			return tmp_id;

		} else
			return tmp_id;
	}

	public User getUserById(long id) {
		return userRepository.getOne(id);
	}

	public List<User> getUsersBySectorAndRole(Sector sector) {
		List<User> tmp_user = userRepository.getUserBySector(sector);
		Iterator<User> it = tmp_user.iterator();
		while (it.hasNext()) {
			User user = it.next();
			if (user.getRole().getId() <= 2)
				it.remove();

		}

		return tmp_user;

	}

	public Collection<User> getUserBySector(Sector sector) {
		Collection<User> users = userRepository.getUserBySector(sector);
		return users;
	}

	public User getUserByName(String name) {
		return userRepository.getUserByUsername(name);
	}

}
