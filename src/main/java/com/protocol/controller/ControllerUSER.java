package com.protocol.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.protocol.model.Sector;
import com.protocol.service.SectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.protocol.ProtocolFinalApplication;
import com.protocol.model.FileOfProtocol;
import com.protocol.model.Protocol;
import com.protocol.model.User;
import com.protocol.service.FileService;
import com.protocol.service.ProtocolService;

import com.protocol.service.UserService;

@Controller
public class ControllerUSER {

	@Autowired
	private SectorService sectorService;

	@Autowired
	private UserService userService;

	@Autowired
	private ProtocolService protocolService;

	@Autowired
	private FileService fileService;

	@GetMapping("/USER/home")
	public String home() {
		return "USER_TEMPLATE/homeUser";
	}

	@GetMapping("/USER/logout")
	public String logout() {
		return "redirect:/logout";
	}

	@GetMapping("/USER/showProtocols")
	public String showProtocols(Model model, @CurrentSecurityContext(expression = "authentication.name") String name) {
		User tmp_user = userService.getUserByName(name);
		List<Protocol> protocols = protocolService.getProtocolBySector(tmp_user.getSector().getId());
		model.addAttribute("user", tmp_user);
		model.addAttribute("protocols", protocols);

		return "USER_TEMPLATE/showprotocolUSER";

	}
	@GetMapping("/USER/showProtocols/incoming")
	public String showIncomingProtocols(Model model, @CurrentSecurityContext(expression = "authentication.name") String name) {
		User tmp_user = userService.getUserByName(name);
		List<Protocol> protocols = protocolService.getIncomingProtocolBySector(tmp_user.getSector().getId());
		model.addAttribute("user", tmp_user);
		model.addAttribute("protocols", protocols);

		return "USER_TEMPLATE/showprotocolUSER";

	}
	@GetMapping("/USER/showProtocols/outgoing")
	public String showOutgoingProtocols(Model model, @CurrentSecurityContext(expression = "authentication.name") String name) {
		User tmp_user = userService.getUserByName(name);
		List<Protocol> protocols = protocolService.getOutgoingProtocolBySector(tmp_user.getSector().getId());
		model.addAttribute("user", tmp_user);
		model.addAttribute("protocols", protocols);

		return "USER_TEMPLATE/showprotocolUSER";

	}

	@RequestMapping("/USER/file/{fileName}")
	@ResponseBody
	public void downloadFile(@PathVariable("fileName") String fileName, HttpServletResponse response) {

		if (fileName.indexOf(".doc") > -1)
			response.setContentType("application/msword");
		if (fileName.indexOf(".docx") > -1)
			response.setContentType("application/msword");
		if (fileName.indexOf(".xls") > -1)
			response.setContentType("application/vnd.ms-excel");
		if (fileName.indexOf(".csv") > -1)
			response.setContentType("application/vnd.ms-excel");
		if (fileName.indexOf(".ppt") > -1)
			response.setContentType("application/ppt");
		if (fileName.indexOf(".pdf") > -1)
			response.setContentType("application/pdf");
		if (fileName.indexOf(".zip") > -1)
			response.setContentType("application/zip");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		response.setHeader("Content-Transfer-Encoding", "binary");
		try {
			BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
			FileInputStream fis = new FileInputStream(ProtocolFinalApplication.fileLocation + fileName);
			int len;
			byte[] buf = new byte[1024];
			while ((len = fis.read(buf)) > 0) {
				bos.write(buf, 0, len);
			}
			bos.close();
			response.flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();

		}
	}

	@RequestMapping(value = "/USER/createprotocol")
	public String createProtocol(@CurrentSecurityContext(expression = "authentication.name") String name,
			@RequestParam("type") String type, @RequestParam("title") String title,
			@RequestParam("description") String description, @RequestParam("followup") String followup,
			@RequestParam("file") MultipartFile file) throws IllegalStateException, IOException {

		User logeInUser = userService.getUserByName(name);
		Sector sectorOflogeInUser= sectorService.getSectorById(logeInUser.getSector().getId());
		Protocol tmp_protocol;
		String current_value = "";
		try {
			current_value=sectorService.handleCounterOfProtocolType(sectorOflogeInUser,type);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		if (!file.isEmpty()) {
			file.transferTo(new File(ProtocolFinalApplication.fileLocation + file.getOriginalFilename()));
			FileOfProtocol tmp_file = new FileOfProtocol(file.getOriginalFilename(), (long) file.getSize());
			fileService.addFile(tmp_file);

			tmp_protocol = new Protocol(logeInUser, followup, type, title, description, tmp_file,current_value);

		} else {
			tmp_protocol = new Protocol(logeInUser, followup, type, title, description,current_value);
		}

		protocolService.addProtocol(tmp_protocol);

		return "redirect:/USER/showProtocols";
	}

	@GetMapping("/USER/editprotocol/{id}")
	public String editProtocol(@PathVariable("id") long id, Model model) {
		model.addAttribute("protocol", protocolService.getProtocol(id));
		return "USER_TEMPLATE/editProtocol";
	}

	@RequestMapping(value = "USER/saveEditedProtocol")
	public String savedEditedProtocol(@RequestParam("id") long id, @RequestParam("title") String title,
			@RequestParam("description") String description, @RequestParam("followUp") String followup,
			@RequestParam("option") String option, @RequestParam("file") MultipartFile file)
			throws IllegalStateException, IOException {

		Protocol tmp_protocol = protocolService.getProtocol(id);
		tmp_protocol.setTitle(title);
		tmp_protocol.setDescription(description);
		tmp_protocol.setFollowUp(followup);

		/*
		 * Declare option file with value option1 we Keep existing file, with value
		 * option2 we delete file from DB, with value option 3 we Proceed with out
		 * attach file with value option 4 we Upload file and removed previous one
		 * 
		 */

		if (option.equals("option1") || option.equals("option3")) {

		} else if (option.equals("option2")) {
			tmp_protocol.setFile(null);

		} else {
			if (!file.isEmpty()) {
				file.transferTo(new File(ProtocolFinalApplication.fileLocation + file.getOriginalFilename()));

				FileOfProtocol tmp_file = new FileOfProtocol(file.getOriginalFilename(), (long) file.getSize());

				fileService.addFile(tmp_file);
				tmp_protocol.setFile(tmp_file);

			}
		}

		protocolService.addProtocol(tmp_protocol);

		return "redirect:/USER/showProtocols";
	}

	@GetMapping("/USER/deleteprotocol/{id}")
	public String deleteProtocol(@PathVariable("id") long id) {

		protocolService.deleteProtocolById(id);
		return "redirect:/USER/showProtocols";
	}

}
