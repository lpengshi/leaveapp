package sg.edu.iss.leave.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.iss.leave.model.Entitlement;
import sg.edu.iss.leave.model.LeaveBalance;
import sg.edu.iss.leave.model.LeaveRecord;
import sg.edu.iss.leave.model.Staff;
import sg.edu.iss.leave.repo.LeaveBalanceRepository;

@Service
public class LeaveBalanceService {

	@Autowired
	private LeaveBalanceRepository lbRepo;

	public LeaveBalance findLeaveBalanceById(int id) {
		LeaveBalance leaveBalance = lbRepo.findById(id).orElse(null);
		return leaveBalance;
	}
	
	public LeaveBalance findLeaveBalanceByStaffIdAndLeaveTypeId(int staffId, int leaveTypeId) {
		LeaveBalance leaveBalance = lbRepo.findByStaffIdAndLeaveTypeId(staffId, leaveTypeId);
		if (leaveBalance == null) {
			return null;
		}
		return leaveBalance;
	}
	
	public LeaveBalance updateLeaveBalance(LeaveBalance lb) {
		LeaveBalance leaveBalance = lbRepo.save(lb);
		return leaveBalance;
	}
	
	public List<LeaveBalance> findAllLeaveBalance() {
		List<LeaveBalance> leaBalList = lbRepo.findAll();
		return leaBalList;
	}
	
	// get the staff's balance for a leave type
	public int getLeaveBalance(int staffId, int leaveTypeId) {
		LeaveBalance leaveBalance = lbRepo.findByStaffIdAndLeaveTypeId(staffId, leaveTypeId);
		if (leaveBalance == null) {
			return 0;
		}
		return leaveBalance.getBalance();
	}
	
	public void refreshLeaveBalace(List<Staff> staffList, List<Entitlement> entList) {
		for (Staff staff : staffList) {
			for (Entitlement ent : entList) {
				if (staff.getRole().equals(ent.getRole())) {
					LeaveBalance leaBal = this.findLeaveBalanceByStaffIdAndLeaveTypeId(staff.getStaffId(), ent.getLeaveTypeId());
					if (leaBal != null) {
						leaBal.setBalance(ent.getEntitledDays());
						lbRepo.save(leaBal);
					}
				}
			}
		}
	}

	public void returnLeaveBalance(LeaveRecord leave) {
		LeaveBalance leaveBalance = lbRepo.findByStaffIdAndLeaveTypeId(leave.getStaffId(), leave.getLeaveTypeId());
		leaveBalance.setBalance(leaveBalance.getBalance() + leave.getDuration());
		lbRepo.save(leaveBalance);
	}

}
