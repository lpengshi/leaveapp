package sg.edu.iss.leave.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sg.edu.iss.leave.model.LeaveType;
@Repository
public interface LeaveTypeRepository extends JpaRepository<LeaveType, Integer> {
	
}
