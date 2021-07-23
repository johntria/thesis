package com.protocol.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.protocol.ProtocolFinalApplication;
import com.protocol.model.FileOfProtocol;
import com.protocol.model.Protocol;
import com.protocol.model.Role;
import com.protocol.model.Sector;
import com.protocol.model.User;
import com.protocol.service.FileService;
import com.protocol.service.ProtocolService;
import com.protocol.service.RoleService;
import com.protocol.service.SectorService;
import com.protocol.service.UserService;
import org.springframework.ui.Model;

@Controller
public class ControllerSUPERUSER {

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

	@GetMapping("/SUPERUSER/home")
	public String home() {

		return "SUPERUSER_TEMPLATE/homeSuper";
	}

	@GetMapping("/SUPERUSER/list")
	public String getUsers(@CurrentSecurityContext(expression = "authentication.name") String name, Model model) {
		User tmp_user = userService.getUserByName(name);
		List<User> tmp_users = userService.getUsersBySectorAndRole(tmp_user.getSector());
		model.addAttribute("users", tmp_users);
		Role tmp_role = roleService.findByName("USER");
		model.addAttribute("roles", tmp_role);
		model.addAttribute("sectors", tmp_user.getSector());

		return "SUPERUSER_TEMPLATE/users";

	}

	@RequestMapping(value = "/SUPERUSER/addNewUser", method = { RequestMethod.POST, RequestMethod.PUT,
			RequestMethod.GET })
	public String addUser(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName,
			@RequestParam("username") String username, @RequestParam("password") String password,
			@RequestParam("active") int active, @RequestParam("role") Role role,
			@RequestParam("sector") Sector sector) {

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String encodedPassword = encoder.encode(password);
		User tmp_user = new User(firstName, lastName, username, encodedPassword, active, role, sector);
		userService.addUser(tmp_user);
		return "redirect:/SUPERUSER/list";

	}

	@GetMapping("/SUPERUSER/deleteUser")
	public String deleteUser(@RequestParam("id") long id) {
		userService.deleteUserById((long) id);
		return "redirect:/SUPERUSER/list";
	}

	@GetMapping("/SUPERUSER/editUser")
	public String editUser(@RequestParam("id") long id, Model model) {
		User tmp_user = userService.getUserById(id);

		model.addAttribute("id", id);
		model.addAttribute("user", tmp_user);
		model.addAttribute("roles", roleService.findByName("USER"));
		model.addAttribute("sectors", tmp_user.getSector());
		return "SUPERUSER_TEMPLATE/editUserForm";
	}

	@RequestMapping(value = "/SUPERUSER/updateUser", method = { RequestMethod.POST, RequestMethod.PUT,
			RequestMethod.GET })
	public String update(@RequestParam("id") long id, @RequestParam("firstName") String firstName,
			@RequestParam("lastName") String lastName, @RequestParam("username") String username,
			@RequestParam("active") int active, @RequestParam("role") Role role,
			@RequestParam("sector") Sector sector) {
		userService.updateUserByVariables(id, firstName, lastName, username, active, role, sector);
		return "redirect:/SUPERUSER/list";
	}

	@GetMapping("/SUPERUSER/logout")
	public String logout() {
		return "redirect:/logout";
	}

	// Protocol Controller for SuperUser

	@RequestMapping(value = "/SUPERUSER/createprotocol")
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

		return "redirect:/SUPERUSER/showProtocols";
	}

	@GetMapping("/SUPERUSER/showProtocols")
	public String showProtocols(Model model) {

		model.addAttribute("protocols", protocolService.getProtocols());

		return "SUPERUSER_TEMPLATE/showprotocolSUPERUSER";

	}

	@GetMapping("/SUPERUSER/editprotocol/{id}")
	public String editProtocol(@PathVariable("id") long id, Model model) {
		model.addAttribute("protocol", protocolService.getProtocol(id));
		return "SUPERUSER_TEMPLATE/editProtocol";
	}

	@RequestMapping(value = "/SUPERUSER/saveEditedProtocol")
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

		return "redirect:/SUPERUSER/showProtocols";
	}

	@GetMapping("/SUPERUSER/deleteprotocol/{id}")
	public String deleteProtocol(@PathVariable("id") long id) {

		protocolService.deleteProtocolById(id);
		return "redirect:/SUPERUSER/showProtocols";
	}

	@RequestMapping("/SUPERUSER/file/{fileName}")
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

}
