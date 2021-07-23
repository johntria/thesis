package com.protocol.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.protocol.model.FileOfProtocol;

public interface FileRepository extends JpaRepository<FileOfProtocol, Long> {
	FileOfProtocol findById(long id);

}
