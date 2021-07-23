package com.protocol.service;

import java.util.List;
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

}
