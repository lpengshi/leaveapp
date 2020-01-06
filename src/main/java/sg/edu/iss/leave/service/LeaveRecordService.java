package sg.edu.iss.leave.service;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.iss.leave.model.LeaveRecord;
import sg.edu.iss.leave.model.Staff;
import sg.edu.iss.leave.repo.LeaveRecordRepository;

@Service
public class LeaveRecordService {

	@Autowired
	private LeaveRecordRepository lrRepo;

	// get staff's leave records for a specific year
	public List<LeaveRecord> getStaffLeavesByYear(int staffId) {
		List<LeaveRecord> staffLeaveRecords = lrRepo.getByStaffIdAndYearOrderByStartDateAsc(staffId, Year.now().getValue());
		return staffLeaveRecords;
	}
	
	public List<LeaveRecord> getStaffLeavesByYearOrderByStaffId(int staffId) {
		List<LeaveRecord> staffLeaveRecords = lrRepo.getByStaffIdAndYearOrderByStaffIdAsc(staffId, Year.now().getValue());
		return staffLeaveRecords;
	}

	// get a specific leave by its id
	public LeaveRecord getLeaveById(int leaveId) {
		LeaveRecord leave = lrRepo.findById(leaveId).orElse(null);
		return leave;
	}

	// cancel leave
	public void cancelLeave(LeaveRecord leave) {
		// get leave, change leave status to cancelled, save to DB
		leave.setStatus("Cancelled");
		lrRepo.save(leave);

	}

	// delete leave
	public void deleteLeave(LeaveRecord leave) {
		leave.setStatus("Deleted");
		lrRepo.save(leave);
	}

	public List<LeaveRecord> findListStaffLeaveAppRecords(List<Staff> myStfList) {
		List<LeaveRecord> myStfLeaRecList = new ArrayList<LeaveRecord>();
		for (Staff stf : myStfList) {
			List<LeaveRecord> stfRecList = this.getStaffLeavesByYearOrderByStaffId(stf.getStaffId());
			for (LeaveRecord leaRec : stfRecList) {
				if (leaRec.getStatus().equals("Applied") || leaRec.getStatus().equals("Updated")) {
					myStfLeaRecList.add(leaRec);
				}
			}
		}
		return myStfLeaRecList;
	}

	public LeaveRecord approveLeave(LeaveRecord leave) {
		leave.setStatus("Approved");
		LeaveRecord updateLeave = lrRepo.save(leave);
		return updateLeave;
	}

	public LeaveRecord rejectLeave(LeaveRecord leave) {
		leave.setStatus("Rejected");
		LeaveRecord updateLeave = lrRepo.save(leave);
		return updateLeave;
	}

	public List<LeaveRecord> getAllMyStaffLeavesByYear(List<Staff> myStfList) {
		List<LeaveRecord> myStfLeaRecList = new ArrayList<LeaveRecord>();
		for (Staff stf : myStfList) {
			List<LeaveRecord> stfRecList = this.getStaffLeavesByYearOrderByStaffId(stf.getStaffId());
			for (LeaveRecord leaRec : stfRecList) {
				myStfLeaRecList.add(leaRec);
			}
		}

		return myStfLeaRecList;
	}

	public void saveLeaveRecord(LeaveRecord leaveRecord) {
		lrRepo.save(leaveRecord);
	}

	//view movement register by selected month and year
	public List<LeaveRecord> getMovementRegister(int month, int year){
		List<LeaveRecord> leaveRecord = lrRepo.getByMonthAndYear(month, year);
		return leaveRecord;
	}

	public List<LeaveRecord> findByOverlapping(LeaveRecord leaRec, Staff staff, List<Staff> staffList) {
		
		int mgrId = staff.getStaffId();
		List<LeaveRecord> fiLeaAppList = new ArrayList<LeaveRecord>();
		List<LeaveRecord> leaveRec = lrRepo.findOverlapping(leaRec.getStartDate(), leaRec.getEndDate());
		for(LeaveRecord leave : leaveRec) {
			
			Staff stf = null;
			
			for (Staff person: staffList) {
				if (leave.getStaffId() == person.getStaffId()) {
					stf = person;
					break;
				}
			}

			if ((stf.getReportTo() == (mgrId)) && (leave.getLeaveId() != leaRec.getLeaveId()) && (leave.getStatus().equals("Applied") || leave.getStatus().equals("Updated") || leave.getStatus().equals("Approved"))) 
			{
				fiLeaAppList.add(leave);
			}
		}		
		return fiLeaAppList;
	}
	
	//find approved leaves by manager
	
		public List<LeaveRecord> findByMgrApproved(Staff staff){
			int mgrId = staff.getStaffId();
			
			List<LeaveRecord> stfRecList = lrRepo.findAll();
			List<LeaveRecord> leaAppList = new ArrayList<LeaveRecord>();
			
			for (LeaveRecord leave : stfRecList) {
					if (leave.getStatus().equals("Approved") && leave.getStaff().getReportTo() == mgrId) {
						leaAppList.add(leave);
					}
			}
			
			return leaAppList;
		}
}
