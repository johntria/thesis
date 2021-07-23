package com.protocol.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity
@Table(name = "PROTOCOL")
public class Protocol {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "unique_identifier")
	private String uniqueID = UUID.randomUUID().toString() + "-DTG-"
			+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

	@Column(name = "user_identifier")
	private String userIdentifier = LocalDateTime.now().getDayOfYear() + "/"
			+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

	@ManyToOne
	private User createdFromUser;

	@Column(name = "date_created")
	private String dateCreated = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

	@Lob
	private String followUp;

	@Column(name = "type")
	private String type;

	@Column(name = "title")
	private String title;

	@Lob
	private String description;

	@OneToOne
	private FileOfProtocol file;

	public Protocol() {
	};

	public Protocol(User createdFromUser, String type, String title, String description) {
		super();
		this.createdFromUser = createdFromUser;
		this.type = type;
		this.title = title;
		this.description = description;
	}

	public Protocol(User createdFromUser, String followUp, String type, String title, String description) {
		super();
		this.createdFromUser = createdFromUser;
		this.followUp = followUp;
		this.type = type;
		this.title = title;
		this.description = description;
		userIdentifier = this.getType().substring(0, 3) + "-" + LocalDateTime.now().getDayOfYear() + "/"
				+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

	}

	public Protocol(User createdFromUser, String followUp, String type, String title, String description,
			FileOfProtocol file) {
		super();
		this.createdFromUser = createdFromUser;
		this.followUp = followUp;
		this.type = type;
		this.title = title;
		this.description = description;
		this.file = file;
		userIdentifier = this.getType().substring(0, 3) + "-" + LocalDateTime.now().getDayOfYear() + "/"
				+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
	}

	public String getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public FileOfProtocol getFile() {
		return file;
	}

	public void setFile(FileOfProtocol file) {
		this.file = file;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUniqueID() {
		return uniqueID;
	}

	public void setUniqueID(String uniqueID) {
		this.uniqueID = uniqueID;
	}

	public User getCreatedFromUser() {
		return createdFromUser;
	}

	public void setCreatedFromUser(User createdFromUser) {
		this.createdFromUser = createdFromUser;
	}

	public String getdateCreated() {
		return dateCreated;
	}

	public void setdateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getUserIdentifier() {
		return userIdentifier;
	}

	public void setUserIdentifier(String userIdentifier) {
		this.userIdentifier = userIdentifier;
	}

	public String getFollowUp() {
		return followUp;
	}

	public void setFollowUp(String followUp) {
		this.followUp = followUp;
	}

}
