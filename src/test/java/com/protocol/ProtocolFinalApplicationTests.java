package com.protocol;

import java.text.ParseException;
import java.util.Collection;
import java.util.Objects;

import com.protocol.model.Protocol;
import com.protocol.repository.SectorRepository;
import com.protocol.wrapper.WrapperQuery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.protocol.repository.ProtocolRepository;
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

	@Autowired
	SectorRepository sectorRepository;
	@Test
	void contextLoads() throws ParseException {

	}
}
