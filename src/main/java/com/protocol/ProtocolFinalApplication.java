package com.protocol;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.protocol.model.Role;
import com.protocol.model.Sector;
import com.protocol.model.User;
import com.protocol.service.RoleService;
import com.protocol.service.SectorService;
import com.protocol.service.UserService;

@SpringBootApplication
public class ProtocolFinalApplication {

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private SectorService sectorService;

	public static final String fileLocation = System.getProperty("user.dir") + "/src/main/resources/static/Files";

	public static void main(String[] args) {

		SpringApplication.run(ProtocolFinalApplication.class, args);

	}

	@Bean
	InitializingBean sendDatabase() {
		final String sector = "helpdesk";
		final String firstname = "helpdesk";
		final String lastname = "helpdesk";
		final String username = "helpdesk1";
		final String password = "helpdeskPassword123#@!";
		final int active = 1;
		return () -> {
			System.out.println(fileLocation);
			if (roleService.getRoles().isEmpty()) {
				roleService.addRole(new Role("GODUSER"));
				roleService.addRole(new Role("SUPERUSER"));
				roleService.addRole(new Role("USER"));
			}
			if (sectorService.getSectors().isEmpty()) {
				sectorService.addSector(new Sector(sector));
			}

			if (userService.getUserByName(username) == null) {
				BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
				String encodedPassword = encoder.encode(password);
				userService.addUser(new User(firstname, lastname, username, encodedPassword, active,
						roleService.findByName("GODUSER"), sectorService.getSectorByName(sector)));
			}

		};
	}

}
