package com.protocol.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.protocol.model.Protocol;
import com.protocol.model.Sector;
import com.protocol.model.User;

@Repository
public interface ProtocolRepository extends JpaRepository<Protocol, Long> {
	Protocol findById(long id);

	List<Protocol> getProtocolBycreatedFromUser(User user);

	@Query("SELECT u FROM Protocol u WHERE u.createdFromUser.sector=:sector")
	Collection<Protocol> findProtocolByUserSector(@Param("sector") Sector sector);

	@Query(value ="SELECT * FROM protocol u WHERE u.type = ?1 ORDER BY u.id DESC LIMIT 1",nativeQuery = true)
	Protocol findLatestProtocolByType(String type);



}
