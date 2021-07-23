package com.protocol.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.protocol.model.FileOfProtocol;
import com.protocol.repository.FileRepository;

@Service
public class FileService {

	@Autowired
	private FileRepository FileRepository;

	public void addFile(FileOfProtocol file) {
		FileRepository.save(file);
	}

	public void updateFile(FileOfProtocol file) {
		FileRepository.save(file);
	}

	public void deleteFile(long id) {
		FileRepository.deleteById(id);
	}

	public List<FileOfProtocol> getAnswers() {
		return (List<FileOfProtocol>) FileRepository.findAll();
	}

	public Long getLatestId() {
		List<FileOfProtocol> files = FileRepository.findAll();
		long tmp_id = 1;
		if (files != null && !files.isEmpty()) {
			for (FileOfProtocol tmp_file : files) {
				if (tmp_file.getId() > tmp_id)
					tmp_id = tmp_file.getId();
			}
			tmp_id++;
			return tmp_id;
		} else
			return tmp_id;
	}

	public FileOfProtocol getFile(long id) {
		return FileRepository.findById(id);
	}

}
