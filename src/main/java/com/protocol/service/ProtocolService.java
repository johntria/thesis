package com.protocol.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.protocol.model.Protocol;
import com.protocol.model.Sector;
import com.protocol.model.User;
import com.protocol.repository.ProtocolRepository;

@Service
public class ProtocolService {

	@Autowired
	private ProtocolRepository protocolRepository;

	public void addProtocol(Protocol protocol) {
		protocolRepository.save(protocol);
	}

	public void updateProtocol(Protocol protocol) {
		protocolRepository.save(protocol);
	}

	public void deleteProtocolById(long id) {
		protocolRepository.deleteById(id);
	}

	public List<Protocol> getProtocols() {
		return (List<Protocol>) protocolRepository.findAll();
	}

	public List<Protocol> getProtocolsBySector(Sector sector) {
		return (List<Protocol>) protocolRepository.findProtocolByUserSector(sector);
	}

	public Long getLatestId() {
		List<Protocol> protocol = protocolRepository.findAll();
		long tmp_id = 1;
		if (protocol != null && !protocol.isEmpty()) {
			for (Protocol tmp_protocol : protocol) {
				if (tmp_protocol.getId() > tmp_id)
					tmp_id = tmp_protocol.getId();
			}
			tmp_id++;
			return tmp_id;

		} else
			return tmp_id;
	}

	public List<String> getTimeofProtocols() {

		List<Protocol> protocols = protocolRepository.findAll();

		List<String> times = new ArrayList<String>();

		for (Protocol tmp : protocols) {
			times.add(tmp.getdateCreated());
		}

		return times;

	}

	public List<String> getTimeofProtocolsOfSpecificProtocols(List<Protocol> protocols) {

		List<String> times = new ArrayList<String>();

		for (Protocol tmp : protocols) {
			times.add(tmp.getdateCreated());
		}

		return times;

	}

	public Protocol getProtocol(long id) {
		return protocolRepository.findById(id);
	}

	public List<Protocol> getProtocolBycreatedFromUser(User user) {
		return protocolRepository.getProtocolBycreatedFromUser(user);
	}

}
