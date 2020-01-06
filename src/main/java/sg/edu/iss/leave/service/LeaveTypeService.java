package sg.edu.iss.leave.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.iss.leave.model.LeaveType;
import sg.edu.iss.leave.repo.LeaveTypeRepository;

@Service
public class LeaveTypeService {

	@Autowired
	private LeaveTypeRepository ltRepo;
	
	public LeaveType findLeaveTypeById(int id) {
		LeaveType leaveType = ltRepo.findById(id).orElse(null);
		return leaveType;
	}
	
	public List<LeaveType> findAllLeaveType() {
		List<LeaveType> ltList = ltRepo.findAll();
		return ltList;
	}

	public LeaveType updateLeaveType(LeaveType lt) {
		LeaveType leaveType = ltRepo.save(lt);
		return leaveType;
	}

	public void deleteLeaveType(LeaveType lt) {
		ltRepo.delete(lt);
		return;
	}

}
