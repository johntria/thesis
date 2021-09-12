package com.protocol.repository;

import com.protocol.wrapper.WrapperQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.protocol.model.Sector;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;


@Repository
public interface SectorRepository extends JpaRepository<Sector, Long> {
	Sector findByName(String name);

	@Modifying
	@Transactional
	@Query(value = "INSERT INTO sector_protocol_type VALUES(?1,1,?2)",nativeQuery = true)
	void insertTypeAndNumberInSector(Long id,String type);

	@Query(value="SELECT protocol_type FROM sector_protocol_type u WHERE u.sector_id=?1 AND u.protocol_type_key=?2",nativeQuery = true)
	Integer getValueOfProtocolType(Long id,String type);


	@Transactional
	@Modifying
	@Query(value = "UPDATE sector_protocol_type u SET u.protocol_type=?3 WHERE u.sector_id=?1 AND u.protocol_type_key=?2",nativeQuery = true)
	void updateSectorProtocolType(Long id,String key,int value);

	@Query(value = "SELECT " +
			"    sector_protocol_type.sector_id as sector," +
			"    sector_protocol_type.protocol_type as counttimes," +
			"    sector_protocol_type.protocol_type_key as type," +
			"    protocol.date_created as datecreated" +
			" FROM" +
			"    sector_protocol_type" +
			"        INNER JOIN" +
			"    protocol ON sector_protocol_type.sector_id = protocol.sector_id" +
			"        AND sector_protocol_type.protocol_type_key = protocol.type" +
			"        AND sector_protocol_type.protocol_type_key = ?2" +
			"        AND sector_protocol_type.sector_id = ?1 " +
			"ORDER BY date_created DESC " +
			"LIMIT 1",nativeQuery = true)
	WrapperQuery innerJoinProtocolSector(Long id, String type);

}
