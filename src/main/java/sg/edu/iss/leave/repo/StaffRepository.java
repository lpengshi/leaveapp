package sg.edu.iss.leave.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sg.edu.iss.leave.model.Staff;
@Repository
public interface StaffRepository extends JpaRepository<Staff, Integer>{

	Optional<Staff> findByUsername(String username);

	List<Staff> findByRole(String role);
	
	List<Staff> findByReportTo(int staffId);
}
