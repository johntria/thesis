package com.protocol.controller;

import com.protocol.model.Sector;
import com.protocol.service.SectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.CurrentSecurityContext;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.bind.annotation.RestController;

import com.protocol.model.Protocol;
import com.protocol.model.User;
import com.protocol.wrapper.WrapperApi;
import com.protocol.service.ProtocolService;

import com.protocol.service.UserService;

import java.text.ParseException;

@RestController
@RequestMapping("/api") // If you want to change the mapping of request you have to change it also in
						// ApiSecurityConfigurerAdapter.java
public class ControllerRestAPI {

	@Autowired
	private UserService userService;

	@Autowired
	private SectorService sectorService;

	@Autowired
	private ProtocolService protocolService;

	@RequestMapping(value = "/createprotocol", method = RequestMethod.POST)
	public String addProtocol(@RequestBody WrapperApi obj,
			@CurrentSecurityContext(expression = "authentication.name") String logedInUser) {

		if (obj.validateData() == false) {
			return "Data is not correct , try again ";

		} else {
			String title = obj.getTitle();
			String type = obj.getType();
			String description = obj.getDescription();
			User logenIn = userService.getUserByName(logedInUser);
			Sector sectorOflogeInUser=sectorService.getSectorById(logenIn.getSector().getId());
			String current_value = "";
			try {
				current_value=sectorService.handleCounterOfProtocolType(sectorOflogeInUser,type);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			Protocol tmp_prot = new Protocol(logenIn, type, title, description,current_value);
			protocolService.addProtocol(tmp_prot);

			return "Protocol created successful .\nYou can find it as:\n" + tmp_prot.getUserIdentifier() + "\n" + "Or\n"
					+ tmp_prot.getUniqueID();

		}

	}

}
