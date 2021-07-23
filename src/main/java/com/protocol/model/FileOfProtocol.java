package com.protocol.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "FILES")
public class FileOfProtocol {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "file_name")
	private String fileName;

	@Column(name = "file_size")
	private long size;

	public FileOfProtocol(String fileName, long size) {
		this.fileName = fileName;
		this.size = size;
	}

	public FileOfProtocol() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String toString() {
		return fileName;

	}

}
