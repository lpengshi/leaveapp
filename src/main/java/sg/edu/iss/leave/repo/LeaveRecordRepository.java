package sg.edu.iss.leave.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import sg.edu.iss.leave.model.LeaveRecord;
@Repository
public interface LeaveRecordRepository extends JpaRepository<LeaveRecord, Integer> {

	@Query("select l from LeaveRecord l where l.staffId = ?1 and year(l.startDate) = ?2 order by startDate asc")
	List<LeaveRecord> getByStaffIdAndYearOrderByStartDateAsc(int staffId, int year);

	@Query("select l from LeaveRecord l where l.staffId = ?1 and year(l.startDate) = ?2 order by staffId asc")
	List<LeaveRecord> getByStaffIdAndYearOrderByStaffIdAsc(int staffId, int value);

	//get list of leave of all staff, for the selected month and year, where status=approved
	@Query("select l from LeaveRecord l where (month(l.startDate) = ?1 or month(l.endDate) = ?1) and year(l.endDate) = ?2 and l.status = 'Approved' order by l.startDate")
	List<LeaveRecord> getByMonthAndYear(int month, int year);

	@Query(value = "SELECT * FROM leave_record lr WHERE (lr.end_date >= :startDate) AND (start_date <= :endDate)", nativeQuery = true)
	List<LeaveRecord> findOverlapping(@Param("startDate") LocalDate startDate, @Param("endDate")LocalDate endDate);
}
	
