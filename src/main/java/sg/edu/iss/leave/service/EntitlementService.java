package sg.edu.iss.leave.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.iss.leave.javabean.LeaveEntitlement;
import sg.edu.iss.leave.model.Entitlement;
import sg.edu.iss.leave.model.LeaveType;
import sg.edu.iss.leave.repo.EntitlementRepository;

@Service
public class EntitlementService {

	@Autowired
	private EntitlementRepository eRepo;
	
	public Entitlement findEntitlementById(int id) {
		Entitlement entitlement = eRepo.findById(id).orElse(null);
		return entitlement;
	}
	
	public List<Entitlement> findAllEntitlement() {
		
		List<Entitlement> eList = eRepo.findAll();
		
		return eList;
	}

	public Entitlement createEntitlement(Entitlement ent) {
		Entitlement entitlement = eRepo.save(ent);
		
		return entitlement;
	}


	public Entitlement updateEntitlement(Entitlement ent) {
		Entitlement entitlement = eRepo.save(ent);
		return entitlement;
	}

	public void deleteEntitlement(Entitlement ent) {
		eRepo.delete(ent);
		return;
	}

	public List<LeaveEntitlement> ConvertToLeaveEntitlement(List<Entitlement> entList, List<LeaveType> ltList) {
		List<LeaveEntitlement> lEntList = new ArrayList<LeaveEntitlement>();

		for (Entitlement ent : entList) {
			LeaveEntitlement lEnt = new LeaveEntitlement();
			lEnt.setEntitlement(ent);
			
			for (LeaveType lt : ltList) {
				if (ent.getLeaveTypeId() == lt.getId()) {
					lEnt.setLeaveType(lt);
					break;
				} 
			}
			lEntList.add(lEnt);
		}
		
		return lEntList;
	}

}
