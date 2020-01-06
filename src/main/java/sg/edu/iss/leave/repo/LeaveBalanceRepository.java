package sg.edu.iss.leave.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sg.edu.iss.leave.model.LeaveBalance;
@Repository
public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance, Integer> {
	
	LeaveBalance findByStaffIdAndLeaveTypeId(int staffId, int leaveTypeId);
	
}
