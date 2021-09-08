package com.protocol;

import java.util.Collection;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

import com.protocol.model.Protocol;
import com.protocol.model.Role;
import com.protocol.model.Sector;
import com.protocol.model.User;
import com.protocol.repository.ProtocolRepository;
import com.protocol.repository.RoleRepository;
import com.protocol.repository.UserRepository;
import com.protocol.service.ProtocolService;
import com.protocol.service.RoleService;
import com.protocol.service.SectorService;
import com.protocol.service.UserService;

@SpringBootTest
class ProtocolFinalApplicationTests {

	@Autowired
	RoleService roleRepository;
	@Autowired
	UserService userService;
	@Autowired
	SectorService sectorService;
	@Autowired
	ProtocolService protocolService;
	@Autowired
	ProtocolRepository protocolRepository;

	@Test
	void contextLoads() {
		System.out.println(protocolRepository.findLatestProtocolByType("Incoming").getId());


	}
}
