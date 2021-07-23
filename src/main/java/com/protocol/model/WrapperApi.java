package com.protocol.model;

public class WrapperApi {

	private String title;
	private String type;
	private String description;

	public WrapperApi() {
		super();
	}

	public WrapperApi(String title, String type, String description) {
		this.title = title;
		this.type = type;
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean validateData() {
		if (title == null || title.isEmpty() || type == null || type.isEmpty() || description == null
				|| description.isEmpty()) {
			return false;
		}
		return true;
	}
}
