package com.protocol.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.web.multipart.MultipartFile;
import com.protocol.model.FileOfProtocol;
import com.protocol.ProtocolFinalApplication;

import com.protocol.model.Protocol;
import com.protocol.model.Role;
import com.protocol.model.Sector;
import com.protocol.model.User;
import com.protocol.service.FileService;
import com.protocol.service.ProtocolService;
import com.protocol.service.RoleService;
import com.protocol.service.SectorService;
import com.protocol.service.UserService;

@Controller
public class ControllerGODUSER {

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private SectorService sectorService;

	@Autowired
	private ProtocolService protocolService;

	@Autowired
	private FileService fileService;

//User Controller for GODUSER

	@GetMapping("/GODUSER/home")
	public String home(Model model) {

		return "GODUSER_TEMPLATE/homeGod";
	}

	@GetMapping("/GODUSER/list")
	public String getUsers(Model model) {
		model.addAttribute("users", userService.getUsers());
		model.addAttribute("roles", roleService.getRoles());
		model.addAttribute("sectors", sectorService.getSectors());

		return "GODUSER_TEMPLATE/users";

	}

	@GetMapping("/GODUSER/edit")
	public String editUser(@RequestParam("id") long id, Model model) {
		User tmp_user = userService.getUserById(id);
		model.addAttribute("id", id);
		model.addAttribute("user", tmp_user);
		model.addAttribute("roles", roleService.getRoles());
		model.addAttribute("sectors", sectorService.getSectors());
		return "GODUSER_TEMPLATE/editUserForm";
	}

	@GetMapping("/GODUSER/delete")
	public String deleteUser(@RequestParam("id") long id) {
		userService.deleteUserById((long) id);
		return "redirect:/GODUSER/list";
	}

	@RequestMapping(value = "/GODUSER/addNew", method = { RequestMethod.POST, RequestMethod.PUT, RequestMethod.GET })
	public String addUser(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName,
			@RequestParam("username") String username, @RequestParam("password") String password,
			@RequestParam("active") int active, @RequestParam("role") Role role,
			@RequestParam("sector") Sector sector) {

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String encodedPassword = encoder.encode(password);
		User tmp_user = new User(firstName, lastName, username, encodedPassword, active, role, sector);
		userService.addUser(tmp_user);
		return "redirect:/GODUSER/list";

	}

	@RequestMapping(value = "/GODUSER/update", method = { RequestMethod.POST, RequestMethod.PUT, RequestMethod.GET })
	public String updateUser(@RequestParam("id") long id, @RequestParam("firstName") String firstName,
			@RequestParam("lastName") String lastName, @RequestParam("username") String username,
			@RequestParam("active") int active, @RequestParam("role") Role role,
			@RequestParam("sector") Sector sector) {

		userService.updateUserByVariables(id, firstName, lastName, username, active, role, sector);
		return "redirect:/GODUSER/list";
	}

//Sector Controller for GODUSER

	@GetMapping("/GODUSER/listOfSector")
	public String getSectors(Model model) {

		model.addAttribute("sectors", sectorService.getSectors());
		return "GODUSER_TEMPLATE/sectors";
	}

	@RequestMapping(value = "/GODUSER/addNewSector", method = { RequestMethod.POST, RequestMethod.PUT,
			RequestMethod.GET })
	public String addSector(@RequestParam("name") String sectorName) {

		Collection<User> NULL = null;
		Sector sector_tmp = new Sector(sectorService.getLatestId(), sectorName, NULL);
		sectorService.addSector(sector_tmp);
		return "redirect:/GODUSER/listOfSector";

	}

	@GetMapping("/GODUSER/deleteSector")
	public String deleteSector(@RequestParam("id") long id) {

		sectorService.deleteSectorsAndUsersAndProtocols(sectorService.getSectorById(id));
		return "redirect:/GODUSER/listOfSector";
	}

	@GetMapping("/GODUSER/editSector")
	public String editSector(@RequestParam("id") long id, Model model) {
		Sector tmp_sector = sectorService.getSectorById(id);
		model.addAttribute("id", id);
		model.addAttribute("sector", tmp_sector);
		return "GODUSER_TEMPLATE/editSector";

	}

	@RequestMapping(value = "/GODUSER/updateSector", method = { RequestMethod.POST, RequestMethod.PUT,
			RequestMethod.GET })
	public String addSector(@RequestParam("id") long id, @RequestParam("name") String name) {
		sectorService.updateSectorByIdANDname(id, name);
		return "redirect:/GODUSER/listOfSector";

	}

//Protocol Controller for GodUser

	@RequestMapping(value = "/GODUSER/createprotocol")
	public String createProtocol(@CurrentSecurityContext(expression = "authentication.name") String name,
			@RequestParam("type") String type, @RequestParam("title") String title,
			@RequestParam("description") String description, @RequestParam("followup") String followup,
			@RequestParam("file") MultipartFile file) throws IllegalStateException, IOException {

		User logeInUser = userService.getUserByName(name);
		Protocol tmp_protocol;

		if (!file.isEmpty()) {
			file.transferTo(new File(ProtocolFinalApplication.fileLocation + file.getOriginalFilename()));

			FileOfProtocol tmp_file = new FileOfProtocol(file.getOriginalFilename(), (long) file.getSize());

			fileService.addFile(tmp_file);

			tmp_protocol = new Protocol(logeInUser, followup, type, title, description, tmp_file);

		} else {
			tmp_protocol = new Protocol(logeInUser, followup, type, title, description);
		}

		protocolService.addProtocol(tmp_protocol);

		return "redirect:/GODUSER/showProtocols";
	}

	@GetMapping("/GODUSER/showProtocols")
	public String showProtocols(Model model) {

		model.addAttribute("protocols", protocolService.getProtocols());

		return "GODUSER_TEMPLATE/showprotocol";

	}

	@GetMapping("/GODUSER/editprotocol/{id}")
	public String editProtocol(@PathVariable("id") long id, Model model) {
		model.addAttribute("protocol", protocolService.getProtocol(id));
		return "GODUSER_TEMPLATE/editProtocol";
	}

	@RequestMapping(value = "/GODUSER/saveEditedProtocol")
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

		return "redirect:/GODUSER/showProtocols";
	}

	@GetMapping("/GODUSER/deleteprotocol/{id}")
	public String deleteProtocol(@PathVariable("id") long id) {

		protocolService.deleteProtocolById(id);
		return "redirect:/GODUSER/showProtocols";
	}

	@RequestMapping("/GODUSER/file/{fileName}")
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

	@GetMapping("/GODUSER/logout")
	public String logout() {
		return "redirect:/logout";
	}

}
