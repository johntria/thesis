package com.protocol.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.protocol.wrapper.WrapperQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.protocol.model.Protocol;
import com.protocol.model.Sector;
import com.protocol.model.User;
import com.protocol.repository.SectorRepository;

@Service
public class SectorService {

	@Autowired
	private SectorRepository sectorRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private ProtocolService protocolService;

	public List<Sector> getSectors() {
		return sectorRepository.findAll();

	}

	public void addSector(Sector sector) {
		sectorRepository.save(sector);
	}

	public void updateSector(Sector sector) {
		sectorRepository.save(sector);
	}

	public void deleteById(Long id) {
		sectorRepository.deleteById(id);
	}

	public Long getLatestId() {
		List<Sector> sector = sectorRepository.findAll();
		long tmp_id = 1;
		if (sector != null && !sector.isEmpty()) {
			for (Sector tmp_sector : sector) {
				if (tmp_sector.getId() > tmp_id)
					tmp_id = tmp_sector.getId();
			}
			tmp_id++;
			return tmp_id;

		} else
			return tmp_id;
	}

	public Sector getSectorById(long id) {

		return sectorRepository.getOne(id);
	}

	public void updateSectorByIdANDname(long id, String name) {
		Sector tmp_sector = sectorRepository.getOne(id);
		tmp_sector.setName(name);
		sectorRepository.save(tmp_sector);
	}

	public void deleteSectorsAndUsersAndProtocols(Sector sector) {

		List<User> users_of_sector = (List<User>) userService.getUserBySector(sector);

		for (User tmp : users_of_sector) {

			List<Protocol> protocol_of_user = protocolService.getProtocolBycreatedFromUser(tmp);
			for (Protocol tmp_protocol : protocol_of_user) {
				protocolService.deleteProtocolById(tmp_protocol.getId());
			}

			userService.deleteUserById(tmp.getId());

		}

		deleteById(sector.getId());

	}

	public Sector getSectorByName(String sector) {
		return sectorRepository.findByName(sector);
	}


	public String handleCounterOfProtocolType(Sector sector,String type) throws ParseException {
		WrapperQuery wrappQuery=sectorRepository.innerJoinProtocolSector(sector.getId(),type);

		if (Objects.isNull(wrappQuery)){
			sectorRepository.insertTypeAndNumberInSector(sector.getId(),type);
			return "1";
		} else{
			int current_value=sectorRepository.getValueOfProtocolType(sector.getId(),type);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			Date latest_year_of_protocol = sdf.parse(wrappQuery.getDateCreated().substring(0, 4));
			Date current_year = sdf.parse(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy")));
			if(latest_year_of_protocol.compareTo(current_year)<0){
				current_value=1;
				sectorRepository.updateSectorProtocolType(sector.getId(),type,current_value);
				return String.valueOf(current_value);
			}else{
				current_value=current_value+1;
				sectorRepository.updateSectorProtocolType(sector.getId(),type,current_value);
				return String.valueOf(current_value);
			}

		}

	}



}
